package com.kleim.eventmanager.entity;

import com.kleim.eventmanager.auth.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.context.annotation.Configuration;

@Entity
@Table(name = "users")
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    private Integer age;
    @Column(name = "role")
    private String role;

    public UserEntity() {}

    public UserEntity(Long id, String login, String password, Integer age, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
