package com.wardconnect.ccms.model;

import com.wardconnect.ccms.enums.Role;
import org.springframework.data.annotation.TypeAlias;

/**
 * OOP Concept: Inheritance & Polymorphism
 * Subclass extending User for Resident role. Overrides abstract methods.
 */
@TypeAlias("RESIDENT")
public class Resident extends User {

    // OOP Concept: Default Constructor
    public Resident() {
        super();
        setRole(Role.RESIDENT);
    }

    // OOP Concept: Parameterized Constructor
    public Resident(String id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.RESIDENT);
    }

    // OOP Concept: Polymorphism (Method Overriding)
    @Override
    public Role getRole() {
        return Role.RESIDENT;
    }

    // OOP Concept: Polymorphism (Method Overriding)
    @Override
    public String getDashboardPath() {
        return "/resident-dashboard.html";
    }

    @Override
    public String toString() {
        return "Resident{" + super.toString() + '}';
    }
}
