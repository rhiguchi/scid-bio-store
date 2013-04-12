package jp.scid.bio.store;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;

abstract public class AbstractPersistentList<E> extends AbstractListModel {
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
    
    private void addElementsAt(int index, Collection<? extends E> newElements) {
        if (newElements.isEmpty()) {
            return;
        }
        
        for (E element: newElements) {
            Long key = getId(element);
            E old = elementMap.put(key, element);
            
            if (old != null) {
                elementMap.put(key, old);
                throw new IllegalStateException(format("id %d element already exists", key));
            }
        }
        
        elements.addAll(index, newElements);
        fireIntervalAdded(this, index, index + newElements.size() - 1);
    }

    private void addElementAt(int index, E element) {
        addElementsAt(index, Collections.singleton(element));
    }

    private E setElementAt(int index, E element) {
        elementMap.put(getId(element), element);
        
        E old = elements.set(index, element);
        fireContentsChanged(this, index, index);
        return old;
    }
    
    private List<E> setElementsAt(int startIndex, Collection<? extends E> changes) {
        List<E> result = new ArrayList<E>(changes.size());
        if (changes.isEmpty()) {
            return result;
        }
        
        int end = startIndex + changes.size();
        
        List<E> subList = elements.subList(startIndex, end);
        result.addAll(subList);
        subList.clear();
        
        elements.addAll(startIndex, changes);
        fireContentsChanged(this, startIndex, end - 1);
        
        return subList;
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
    
    protected void add(int index, E element) {
        addElementAt(index, element);
        insertIntoStore(element);
    }
    
    protected void add(E element) {
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
        
        for (int index: getIndicesForNotContaining(elements, getIdSet(newElements))) {
            removeElementAt(index);
        }
        
        List<int[]> ranges = toRangeList(getIndicesForNotContaining(newElements, getElementIdSet()));
        int lastInsertEnd = 0;
        
        for (int[] insertRange: ranges) {
            int insertStart = insertRange[0];
            int insertEnd = insertRange[1];
            
            List<E> changedElements = newElements.subList(lastInsertEnd, insertStart);
            setElementsAt(lastInsertEnd, changedElements);
            
            List<E> retrievedElements = newElements.subList(insertStart, insertEnd);
            addElementsAt(insertStart, retrievedElements);
            
            lastInsertEnd = insertEnd;
        }
        
        setElementsAt(lastInsertEnd, newElements.subList(lastInsertEnd, newElements.size()));
    }
    
    private static List<int[]> toRangeList(List<Integer> indices) {
        if (indices.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<int[]> ranges = new LinkedList<int[]>();
        
        Iterator<Integer> it = indices.iterator();
        int lastStart = it.next();
        int lastEnd = lastStart + 1;
        
        while (it.hasNext()) {
            int index = it.next();
            if (index == lastEnd) {
                lastEnd++;
                continue;
            }
            
            int[] range = new int[]{lastStart, lastEnd};
            ranges.add(range);
            
            lastStart = index;
            lastEnd = lastStart + 1;
        }
        
        int[] range = new int[]{lastStart, lastEnd};
        ranges.add(range);
        
        return ranges;
    }

    private static List<Integer> getIndicesForNotContaining(Iterator<Long> it, Set<Long> idSet) {
        List<Integer> indices = new LinkedList<Integer>();
        
        for (int index = 0; it.hasNext(); index++) {
            Long key = it.next();
            if (!idSet.contains(key)) {
                indices.add(index);
            }
        }
        return indices;
    }
    
    private List<Integer> getIndicesForNotContaining(Iterable<? extends E> collection, Set<Long> idSet) {
        ElementIdIterator ite = new ElementIdIterator(collection.iterator());
        return getIndicesForNotContaining(ite, idSet);
    }
    
    private class ElementIdIterator implements Iterator<Long> {
        private final Iterator<? extends E> elementIterator;
        
        public ElementIdIterator(Iterator<? extends E> elementIterator) {
            this.elementIterator = elementIterator;
        }

        @Override
        public boolean hasNext() {
            return elementIterator.hasNext();
        }

        @Override
        public Long next() {
            E e = elementIterator.next();
            return getId(e);
        }

        @Override
        public void remove() {
            elementIterator.remove();
        }
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
}
