package com.kleim.eventmanager.notification;

import java.util.Objects;

public class FieldChange<T> {

    private T oldField;
    private T newField;

    public FieldChange(){}

    public FieldChange(T oldField, T newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    @Override
    public String toString() {
        return "FieldChange{" +
                "oldField=" + oldField +
                ", newField=" + newField +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldChange<?> that = (FieldChange<?>) o;
        return Objects.equals(oldField, that.oldField) && Objects.equals(newField, that.newField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldField, newField);
    }

    public T getOldField() {
        return oldField;
    }

    public void setOldField(T oldField) {
        this.oldField = oldField;
    }

    public T getNewField() {
        return newField;
    }

    public void setNewField(T newField) {
        this.newField = newField;
    }
}
