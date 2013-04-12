package jp.scid.bio.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.swing.AbstractListModel;

import org.jooq.impl.Factory;

abstract class AbstractPersistentList<E> extends AbstractListModel {
    final Factory create;
    private final List<E> elements;
    private long modificationValue = Long.MIN_VALUE;
    
    public AbstractPersistentList(Factory factory) {
        this.create = factory;
        elements = new ArrayList<E>();
    }

    public void fetch() {
        fetch(false);
    }
    public void fetch(boolean ignoreIfNotModified) {
        boolean isStoreModified = checkModification();
        
        if (!isStoreModified && ignoreIfNotModified) {
            return;
        }
        
        List<E> newElements = retrieve();
        
        for (ListIterator<Boolean> it = getContainsMap(elements, getIdSet(newElements)).listIterator(); it.hasNext(); ) {
            boolean isDeletion = !it.next().booleanValue();
            if (isDeletion) {
                int index = it.previousIndex();
                removeElement(index);
            }
        }
        
        for (ListIterator<Boolean> it = getContainsMap(newElements, getElementIdSet()).listIterator(); it.hasNext(); ) {
            int index = it.nextIndex();
            E retrievedElement = newElements.get(index);
            boolean isInsertion = !it.next().booleanValue();
            
            // insert
            if (isInsertion) {
                addElement(index, retrievedElement);
            }
            // update
            else {
                setElement(index, retrievedElement);
            }
        }
    }

    private void addElement(int index, E element) {
        elements.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    private E setElement(int index, E element) {
        E old = elements.set(index, element);
        fireContentsChanged(this, index, index);
        return old;
    }
    
    private E removeElement(int index) {
        E element = elements.remove(index);
        fireIntervalRemoved(this, index, index);
        return element;
    }

    private List<Boolean> getContainsMap(List<? extends E> elements, Set<Long> idSet) {
        List<Boolean> map = new ArrayList<Boolean>(elements.size());
        for (E element: elements) {
            Long key = getId(element);
            Boolean value = Boolean.valueOf(idSet.contains(key));
            map.add(value);
        }
        return map;
    }
    
    private Set<Long> getIdSet(Collection<E> elements) {
        Set<Long> set = new HashSet<Long>(elements.size(), 1f);
        for (E e: elements) {
            set.add(getId(e));
        }
        return set;
    }

    private Set<Long> getElementIdSet() {
        return getIdSet(elements);
    }
    
    protected abstract Long getId(E element);
    
    protected abstract List<E> retrieve();
    
    public abstract E createElement();

    abstract boolean deleteFromStore(E element);
    
    abstract boolean insertToStore(E element);
    
    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    @Override
    public int getSize() {
        return elements.size();
    }
    
    public boolean removeElement(E element) {
        boolean result = deleteFromStore(element);
        fetch();
        return result;
    }
    
    public E removeElementAt(int index) {
        E element = elements.get(index);
        removeElement(element);
        return element;
    }
    
    public void addElement(E element) {
        insertToStore(element);
        fetch();
    }
    
    public final boolean checkModification() {
        long newVal = retrieveModificationValue();
        return modificationValue < (modificationValue = newVal);
    }
    
    protected long retrieveModificationValue() {
        return Long.MIN_VALUE;
    }
    
    static class PersistentElement<E> {
        private final long id;
        private final E element;
        
        public PersistentElement(long id, E element) {
            super();
            this.id = id;
            this.element = element;
        }

        public long getId() {
            return id;
        }
        
        public E getElement() {
            return element;
        }
    }
}
