package jp.scid.bio.store;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;

abstract class AbstractPersistentList<E> extends AbstractListModel {
    private final Map<Long, E> elementMap;
    private final List<E> elements;
    private long modificationValue = Long.MIN_VALUE;
    
    public AbstractPersistentList() {
        elements = new ArrayList<E>();
        
        elementMap = new HashMap<Long, E>();
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    private void addElement(int index, E element) {
        Long key = getId(element);
        E old = elementMap.put(key, element);
        if (old != null) {
            elementMap.put(key, old);
            throw new IllegalStateException(format("id %d element already exists", key));
        }
        
        elements.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    private E setElementAt(int index, E element) {
        elementMap.put(getId(element), element);
        
        E old = elements.set(index, element);
        fireContentsChanged(this, index, index);
        return old;
    }
    
    private E removeElementAt(int index) {
        E element = elements.remove(index);
        elementMap.remove(getId(element));
        
        fireIntervalRemoved(this, index, index);
        return element;
    }
    
    public E getElement(long id) {
        return elementMap.get(id);
    }
    
    public E remove(int index) {
        E element = removeElementAt(index);
        deleteFromStore(element);
        return element;
    }
    
    public void add(int index, E element) {
        addElement(index, element);
        insertIntoStore(element);
    }
    
    public void add(E element) {
        add(getSize(), element);
    }
    
    public void updated(int index) {
        E e = getElementAt(index);
        update(e);
        setElementAt(index, e);
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
        
        for (ListIterator<Boolean> it = getMapForContaining(elements, getIdSet(newElements)).listIterator(); it.hasNext(); ) {
            boolean isDeletion = !it.next().booleanValue();
            if (isDeletion) {
                int index = it.previousIndex();
                removeElementAt(index);
            }
        }
        
        for (ListIterator<Boolean> it = getMapForContaining(newElements, getElementIdSet()).listIterator(); it.hasNext(); ) {
            int index = it.nextIndex();
            E retrievedElement = newElements.get(index);
            boolean isInsertion = !it.next().booleanValue();
            
            // insert
            if (isInsertion) {
                addElement(index, retrievedElement);
            }
            // update
            else {
                setElementAt(index, retrievedElement);
            }
        }
    }

    private List<Boolean> getMapForContaining(List<? extends E> elements, Set<Long> idSet) {
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
        return elementMap.keySet();
    }
    
    protected abstract Long getId(E element);
    
    protected abstract List<E> retrieve();
    
    abstract protected boolean deleteFromStore(E element);
    
    abstract protected boolean insertIntoStore(E element);
    
    abstract protected boolean update(E element);
    
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
