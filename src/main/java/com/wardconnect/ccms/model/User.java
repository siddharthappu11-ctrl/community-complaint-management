package com.wardconnect.ccms.model;

import com.wardconnect.ccms.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * OOP Concept: Abstraction & Encapsulation
 * Abstract base class representing a User in the system.
 * Stored in MongoDB "users" collection.
 */
@Document(collection = "users")
public abstract class User {

    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    private Role role;
    private LocalDateTime createdAt;

    // OOP Concept: Default Constructor
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    // OOP Concept: Parameterized Constructor
    public User(String id, String name, String email, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // OOP Concept: Abstraction (Abstract methods to be overridden by subclasses)
    public abstract Role getRole();
    public abstract String getDashboardPath();

    // OOP Concept: Encapsulation (Getters and Setters written by hand)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // OOP Concept: toString() method
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
