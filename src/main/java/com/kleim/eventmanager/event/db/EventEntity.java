package com.kleim.eventmanager.event.db;

import com.kleim.eventmanager.event.domain.EventRegisterEntity;
import com.kleim.eventmanager.event.domain.EventStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "max_place", nullable = false)
    private int maxPlace;
    @OneToMany(mappedBy = "event")
    private List<EventRegisterEntity> registrationList;
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @Column(name = "cost", nullable = false)
    private int cost;
    @Column(name = "duration", nullable = false)
    private int duration;
    @Column(name = "location_id", nullable = false)
    private Long locationId;
    @Column(name = "status", nullable = false)
    private EventStatus status;

    public EventEntity() {}

    public EventEntity(Long id, String name,Long ownerId, Integer maxPlace, List<EventRegisterEntity> registrationList, LocalDateTime date, Integer cost, Integer duration, Long locationId, EventStatus status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlace = maxPlace;
        this.registrationList = registrationList;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlace() {
        return maxPlace;
    }

    public void setMaxPlace(Integer maxPlace) {
        this.maxPlace = maxPlace;
    }

    public List<EventRegisterEntity> getRegistrationList() {
        return registrationList;
    }

    public void setRegistrationList(List<EventRegisterEntity> registrationList) {
        this.registrationList = registrationList;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
