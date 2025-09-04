package com.kleim.eventmanager.model;
//id	integer($int64)
//readOnly: true
//Уникальный идентификатор локации
//
//name*	string
//Имя локации
//
//address*	string
//Адрес локации
//
//capacity*	integer
//minimum: 5
//Вместительность локации
//
//description	string
//Дополнительное описание локации
public record Location(

        Long id,
        String name,
        String address,
        Integer capacity,
        String description

) {
}
