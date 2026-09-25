package com.wardconnect.ccms.dto;

import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new complaint.
 */
public class ComplaintCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Category is required")
    private Category category;

    @NotBlank(message = "Location is required")
    private String location;

    private Priority priority = Priority.MEDIUM;

    @NotBlank(message = "Description is required")
    private String description;

    private String photoPath;

    public ComplaintCreateRequest() {}

    public ComplaintCreateRequest(String title, Category category, String location, Priority priority, String description) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.description = description;
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
}
