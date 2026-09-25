package com.wardconnect.ccms.model;

import com.wardconnect.ccms.enums.Role;
import org.springframework.data.annotation.TypeAlias;

/**
 * OOP Concept: Inheritance & Polymorphism
 * Subclass extending User for Admin role. Overrides abstract methods.
 */
@TypeAlias("ADMIN")
public class Admin extends User {

    // OOP Concept: Default Constructor
    public Admin() {
        super();
        setRole(Role.ADMIN);
    }

    // OOP Concept: Parameterized Constructor
    public Admin(String id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.ADMIN);
    }

    // OOP Concept: Polymorphism (Method Overriding)
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    // OOP Concept: Polymorphism (Method Overriding)
    @Override
    public String getDashboardPath() {
        return "/admin-dashboard.html";
    }

    @Override
    public String toString() {
        return "Admin{" + super.toString() + '}';
    }
}
