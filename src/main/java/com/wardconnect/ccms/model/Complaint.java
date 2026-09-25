package com.wardconnect.ccms.model;

import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.enums.Priority;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * OOP Concept: Encapsulation
 * Represents a community complaint document in MongoDB "complaints" collection.
 */
@Document(collection = "complaints")
public class Complaint {

    @Id
    private String id;

    @Indexed(unique = true)
    private String trackingId;

    private String title;
    private Category category;
    private String location;
    private Priority priority;
    private String description;
    private String photoPath;
    private ComplaintStatus status;
    private String adminResponse;
    private String residentId;
    private String residentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // OOP Concept: Default Constructor
    public Complaint() {
        this.status = ComplaintStatus.PENDING;
        this.adminResponse = "";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // OOP Concept: Parameterized Constructor
    public Complaint(String id, String trackingId, String title, Category category, String location,
                     Priority priority, String description, String photoPath, ComplaintStatus status,
                     String adminResponse, String residentId, String residentName) {
        this.id = id;
        this.trackingId = trackingId;
        this.title = title;
        this.category = category;
        this.location = location;
        this.priority = priority;
        this.description = description;
        this.photoPath = photoPath;
        this.status = status != null ? status : ComplaintStatus.PENDING;
        this.adminResponse = adminResponse != null ? adminResponse : "";
        this.residentId = residentId;
        this.residentName = residentName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // OOP Concept: Encapsulation (Handwritten Getters and Setters)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // OOP Concept: toString()
    @Override
    public String toString() {
        return "Complaint{" +
                "id='" + id + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", location='" + location + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", residentName='" + residentName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
