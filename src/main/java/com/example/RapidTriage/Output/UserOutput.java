package com.example.RapidTriage.Output;

import com.example.RapidTriage.Models.User;

import java.time.LocalDate;

public class UserOutput extends OutputManager {
    private String selfUri;
    private Long id;
    private String name;
    private String username;
    private String email;
    private Integer age;
    private LocalDate birthDate;

    public UserOutput(){}

    public UserOutput(User u){
        this.id = u.getId();
        this.name = u.getName();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.selfUri = getURL() + "/users/" + this.id;
        this.birthDate = u.getBirthDate();
    }

    public String getSelfUri() {
        return selfUri;
    }

    public void setSelfUri(String selfUri) {
        this.selfUri = selfUri;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    //URIs a los endpoints de sus relaciones en la api
}
