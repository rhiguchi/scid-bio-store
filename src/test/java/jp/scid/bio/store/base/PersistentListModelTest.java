package jp.scid.bio.store.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Before;
import org.junit.Test;

public class PersistentListModelTest {
    List<TestElement> source;
    PersistentListModel<TestElement> model;
    TestElement e1 = new TestElement(1, "elm1");
    TestElement e2 = new TestElement(2, "elm2");
    TestElement e3 = new TestElement(3, "elm3");
    TestElement e4 = new TestElement(4, "elm4");
    TestElement e5 = new TestElement(5, "elm5");
    
    @Before
    public void setUp() throws Exception {
        source = new ArrayList<TestElement>();
        model = new TestPersistentListModel(source);
    }

    public void setElements(TestElement... elements) {
        source.clear();
        Collections.addAll(source, elements);
        model.fetch();
    }
    
    @Test
    public void fetch() {
        // insert single
        setElements(e1);
        assertEquals("count", 1, model.getSize());
        assertEquals("insert", source, model.getElements());
        
        // delete first
        setElements();
        assertEquals("delete", 0, model.getSize());
        assertEquals(source, model.getElements());
        
        // insert multiple
        setElements(e1, e2, e3);
        assertEquals("count", 3, model.getSize());
        assertEquals(source, model.getElements());
        
        // insert tail
        setElements(e1, e2, e3, e4, e5);
        assertEquals("count", 5, model.getSize());
        assertEquals(source, model.getElements());
        
        // delete middle
        setElements(e1, e5);
        assertEquals("count", 2, model.getSize());
        assertEquals(source, model.getElements());
        
        // insert head
        setElements(e2, e3, e1, e5);
        assertEquals("count", 4, model.getSize());
        assertEquals(source, model.getElements());
        
        // delete tail
        setElements(e2, e3);
        assertEquals("count", 2, model.getSize());
        assertEquals(source, model.getElements());

        // insert head and middle
        setElements(e1, e2, e4, e5, e3);
        assertEquals("count", 5, model.getSize());
        assertEquals(source, model.getElements());
        
        // delete head and tail
        setElements(e2, e4);
        assertEquals("count", 2, model.getSize());
        assertEquals(source, model.getElements());
        
        // insert and delete
        setElements(e1, e3, e4);
        assertEquals("count", 3, model.getSize());
        assertEquals(source, model.getElements());
        
        // delete all
        setElements();
        assertEquals("count", 0, model.getSize());
        assertEquals(source, model.getElements());
    }

    @Test
    public void fetch_event() {
        ListDataEventCheck checker = new ListDataEventCheck();
        model.addListDataListener(checker);
        
        // insert single
        setElements(e1);
        assertEquals("type", ListDataEvent.INTERVAL_ADDED, checker.event.getType());
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 0, checker.event.getIndex1());
        
        // delete first
        setElements();
        assertEquals("type", ListDataEvent.INTERVAL_REMOVED, checker.event.getType());
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 0, checker.event.getIndex1());

        // insert multiple
        setElements(e1, e2, e3);
        assertEquals("type", ListDataEvent.INTERVAL_ADDED, checker.event.getType());
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 2, checker.event.getIndex1());
    }
    
    @Test
    public void findElement() {
        setElements(e1, e2);
        assertEquals(e1, model.findElement(1));
        assertEquals(e2, model.findElement(2));
        
        setElements();
        assertNull(model.findElement(1));
        assertNull(model.findElement(2));
    }
    
    @Test
    public void add() {
        model = spy(model);
        
        model.add(e1);
        assertEquals(e1, model.getElementAt(0));
        verify(model).insertIntoStore(e1);
        
        model.add(e2);
        assertEquals(e2, model.getElementAt(1));
        verify(model).insertIntoStore(e2);
        
        model.add(0, e3);
        assertEquals(e3, model.getElementAt(0));
        verify(model).insertIntoStore(e3);
    }
    
    @Test
    public void add_event() {
        ListDataEventCheck checker = new ListDataEventCheck();
        model.addListDataListener(checker);
        
        model.add(e1);
        assertEquals("type", ListDataEvent.INTERVAL_ADDED, checker.event.getType());
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 0, checker.event.getIndex1());
        
        model.add(e2);
        assertEquals("start", 1, checker.event.getIndex0());
        assertEquals("end", 1, checker.event.getIndex1());
        
        model.add(0, e3);
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 0, checker.event.getIndex1());
    }
    
    @Test
    public void remove() {
        model = spy(model);
        setElements(e1, e2, e3);
        
        model.remove(0);
        assertEquals(e2, model.getElementAt(0));
        assertEquals(e3, model.getElementAt(1));
        verify(model).deleteFromStore(e1);
        
        model.remove(1);
        assertEquals(e2, model.getElementAt(0));
        assertEquals(1, model.getSize());
        verify(model).deleteFromStore(e3);
    }
    
    @Test
    public void remove_event() {
        ListDataEventCheck checker = new ListDataEventCheck();
        model.addListDataListener(checker);
        setElements(e1, e2, e3);
        
        model.remove(0);
        assertEquals("type", ListDataEvent.INTERVAL_REMOVED, checker.event.getType());
        assertEquals("start", 0, checker.event.getIndex0());
        assertEquals("end", 0, checker.event.getIndex1());
        
        model.remove(1);
        assertEquals("type", ListDataEvent.INTERVAL_REMOVED, checker.event.getType());
        assertEquals("start", 1, checker.event.getIndex0());
        assertEquals("end", 1, checker.event.getIndex1());
    }

    @Test
    public void addToInternalList() {
        model.addToInternalList(0, e1);
    }
    
    // objects
    static class TestElement {
        long id;
        public String name;
        
        public TestElement(String name) {
            this(0, name);
        }
        
        public TestElement(long id, String name) {
            super();
            this.id = id;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    static class TestPersistentListModel extends PersistentListModel<TestElement> {
        private final List<TestElement> source;
        
        public TestPersistentListModel(List<TestElement> source) {
            this.source = source;
        }

        @Override
        protected Long getId(TestElement element) {
            return element.id;
        }

        @Override
        protected List<TestElement> retrieve() {
            return source;
        }

        @Override
        protected boolean deleteFromStore(TestElement element) {
            return source.remove(element);
        }

        @Override
        protected boolean insertIntoStore(TestElement element) {
            return source.add(element);
        }

        @Override
        protected boolean update(TestElement element) {
            return true;
        }
    }

    static class ListDataEventCheck implements ListDataListener {
        ListDataEvent event = null;
        
        @Override
        public void intervalAdded(ListDataEvent e) {
            event = e;
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            event = e;
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            event = e;
        }
    }
}
