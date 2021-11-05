package com.womi;

public class Change {
    private Line old = null;
    private Line current = null;

    public Line getOld() {
        return old;
    }

    public void setOld(Line old) {
        this.old = old;
    }

    public Line getCurrent() {
        return current;
    }

    public void setCurrent(Line current) {
        this.current = current;
    }

    public boolean isAddition() {
        return old.getBody().equals(Line.NULL);
    }

    public boolean isDeletion() {
        return current.getBody().equals(Line.NULL);
    }

    public boolean isEdit() {
        return !isAddition() && !isDeletion();
    }

    public boolean isComplete() {
        return old != null && current != null;
    }

    public void reset() {
        old = null;
        current = null;
    }
}
