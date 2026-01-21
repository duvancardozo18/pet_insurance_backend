package com.pet.insurance.policy_service.domain.model;
import java.util.Objects;

public class Owner {

    private final String id;
    private final String name;
    private final String email;

    public Owner(String id, String name, String email) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }
}
