package jp.scid.bio.store.base;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChangeEventSupport {
    private final List<ChangeListener> sequenceChangeListeners;

    private final Object source;

    public ChangeEventSupport(Object source) {
        if (source == null) throw new IllegalArgumentException("source must not be null");
        this.source = source;
        sequenceChangeListeners = new ArrayList<ChangeListener>();
    }

    public void addChangeListener(ChangeListener listener) {
        sequenceChangeListeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        sequenceChangeListeners.remove(listener);
    }
    
    public void fireStateChange() {
        if (sequenceChangeListeners.isEmpty()) {
            return;
        }
        ChangeEvent e = new ChangeEvent(source);
        for (ChangeListener l: sequenceChangeListeners) {
            l.stateChanged(e);
        }
    }
}