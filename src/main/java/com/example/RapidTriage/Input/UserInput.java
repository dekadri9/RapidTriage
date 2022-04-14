package com.example.RapidTriage.Input;


import java.time.LocalDate;

public class UserInput {
    private String email;
    private String name;
    private String username;
    private String password;
    private LocalDate birthDate;

    public UserInput(){}

    public UserInput(String email, String name, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


}
