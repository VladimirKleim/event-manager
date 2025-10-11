package com.kleim.eventmanager.notification;

import com.kleim.eventmanager.event.domain.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationEvent {

    private Long eventId;
    private List<String> users;
    private Long ownerId;
    private Long changedById;
    private FieldChange<String> name;
    private FieldChange<Integer> maxPlaces;
    private FieldChange<LocalDateTime> date;
    private FieldChange<Integer> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<EventStatus> status;

    public NotificationEvent() {
    }

    public NotificationEvent(Long eventId, List<String> users, Long ownerId, Long changedById, FieldChange<String> name, FieldChange<Integer> maxPlaces, FieldChange<LocalDateTime> date, FieldChange<Integer> cost, FieldChange<Integer> duration, FieldChange<Long> locationId, FieldChange<EventStatus> status) {
        this.eventId = eventId;
        this.users = users;
        this.ownerId = ownerId;
        this.changedById = changedById;
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getChangedById() {
        return changedById;
    }

    public void setChangedById(Long changedById) {
        this.changedById = changedById;
    }

    public FieldChange<String> getName() {
        return name;
    }

    public void setName(FieldChange<String> name) {
        this.name = name;
    }

    public FieldChange<Integer> getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(FieldChange<Integer> maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public FieldChange<LocalDateTime> getDate() {
        return date;
    }

    public void setDate(FieldChange<LocalDateTime> date) {
        this.date = date;
    }

    public FieldChange<Integer> getCost() {
        return cost;
    }

    public void setCost(FieldChange<Integer> cost) {
        this.cost = cost;
    }

    public FieldChange<Integer> getDuration() {
        return duration;
    }

    public void setDuration(FieldChange<Integer> duration) {
        this.duration = duration;
    }

    public FieldChange<Long> getLocationId() {
        return locationId;
    }

    public void setLocationId(FieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public FieldChange<EventStatus> getStatus() {
        return status;
    }

    public void setStatus(FieldChange<EventStatus> status) {
        this.status = status;
    }
}
