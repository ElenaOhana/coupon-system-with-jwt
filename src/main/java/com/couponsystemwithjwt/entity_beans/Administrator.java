package com.couponsystemwithjwt.entity_beans;

import org.springframework.stereotype.Component;

@Component
public class Administrator {

    private long id;
    private String email;
    private String password;

    public Administrator() {
    }

    public Administrator(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
