package com.wardconnect.ccms.config;

import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.enums.Priority;
import com.wardconnect.ccms.model.Admin;
import com.wardconnect.ccms.model.Complaint;
import com.wardconnect.ccms.model.Resident;
import com.wardconnect.ccms.repository.ComplaintRepository;
import com.wardconnect.ccms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Startup seeder to populate default admin, demo resident accounts, and sample complaints
 * with sample photo attachments if empty or missing.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final PasswordEncoder passwordEncoder;

    // Sample image URLs for complaint categories
    private static final String PHOTO_STREETLIGHT = "https://images.unsplash.com/photo-1509114397022-ed747cca3f65?auto=format&fit=crop&w=600&q=80";
    private static final String PHOTO_GARBAGE = "https://images.unsplash.com/photo-1530587191325-3db32d826c18?auto=format&fit=crop&w=600&q=80";
    private static final String PHOTO_ROADS = "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?auto=format&fit=crop&w=600&q=80";
    private static final String PHOTO_WATER = "https://images.unsplash.com/photo-1541888946425-d0fbb186a5b3?auto=format&fit=crop&w=600&q=80";
    private static final String PHOTO_DRAINAGE = "https://images.unsplash.com/photo-1504307651254-35680f356dfd?auto=format&fit=crop&w=600&q=80";
    private static final String PHOTO_NOISE = "https://images.unsplash.com/photo-1517457210348-703079e57d4b?auto=format&fit=crop&w=600&q=80";

    public DataSeeder(UserRepository userRepository,
                      ComplaintRepository complaintRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsersIfEmpty();
        seedComplaintsIfEmpty();
        attachPhotosToExistingComplaints();
    }

    private void seedUsersIfEmpty() {
        if (userRepository.count() == 0) {
            Admin admin = new Admin(
                    null,
                    "Ward Administrator",
                    "admin@ward.com",
                    passwordEncoder.encode("admin123")
            );

            Resident resident1 = new Resident(
                    null,
                    "Maya Rao",
                    "maya@example.com",
                    passwordEncoder.encode("demo123")
            );

            Resident resident2 = new Resident(
                    null,
                    "Arun Kapoor",
                    "arun@example.com",
                    passwordEncoder.encode("demo123")
            );

            userRepository.saveAll(List.of(admin, resident1, resident2));
            System.out.println(">>> DataSeeder: Admin and demo Resident users created.");
        }
    }

    private void seedComplaintsIfEmpty() {
        if (complaintRepository.count() == 0) {
            Resident maya = (Resident) userRepository.findByEmail("maya@example.com").orElse(null);
            Resident arun = (Resident) userRepository.findByEmail("arun@example.com").orElse(null);

            String mayaId = maya != null ? maya.getId() : "user-maya";
            String mayaName = maya != null ? maya.getName() : "Maya Rao";
            String arunId = arun != null ? arun.getId() : "user-arun";
            String arunName = arun != null ? arun.getName() : "Arun Kapoor";

            List<Complaint> seedList = Arrays.asList(
                    new Complaint(null, "CMP-4821", "Streetlight out near Lakeview Park", Category.STREETLIGHTS,
                            "Lakeview Park, Sector 4", Priority.HIGH,
                            "The light beside the north entrance has been out for two weeks, making the walkway unsafe after sunset.",
                            PHOTO_STREETLIGHT, ComplaintStatus.IN_PROGRESS, "Electricity team scheduled for inspection on 28 Sep.", mayaId, mayaName),

                    new Complaint(null, "CMP-4818", "Overflowing garbage bins on Market Road", Category.GARBAGE,
                            "Market Road, Block B", Priority.MEDIUM,
                            "Two public bins have been overflowing each evening this week.",
                            PHOTO_GARBAGE, ComplaintStatus.RESOLVED, "Collection frequency increased to twice daily.", mayaId, mayaName),

                    new Complaint(null, "CMP-4807", "Pothole causing traffic slowdown", Category.ROADS,
                            "Cedar Avenue, near bus stop", Priority.HIGH,
                            "A deep pothole has formed in the left lane and is difficult to see at night.",
                            PHOTO_ROADS, ComplaintStatus.PENDING, "", arunId, arunName),

                    new Complaint(null, "CMP-4799", "Low water pressure in apartment lane", Category.WATER,
                            "Greenfield Lane, Ward 12", Priority.MEDIUM,
                            "Water pressure drops significantly between 7 and 9 AM.",
                            PHOTO_WATER, ComplaintStatus.IN_PROGRESS, "Pipeline pressure test is underway with the utility team.", mayaId, mayaName),

                    new Complaint(null, "CMP-4786", "Drain blocked after recent rain", Category.DRAINAGE,
                            "Rose Colony, Lane 3", Priority.HIGH,
                            "The drain is blocked and stagnant water has remained outside several homes.",
                            PHOTO_DRAINAGE, ComplaintStatus.RESOLVED, "Drain cleared and desilted on 12 Sep.", arunId, arunName),

                    new Complaint(null, "CMP-4772", "Late-night construction noise", Category.NOISE,
                            "Hill Road, Building 8", Priority.LOW,
                            "Construction work continues past the permitted hours on most weekdays.",
                            PHOTO_NOISE, ComplaintStatus.PENDING, "", mayaId, mayaName),

                    new Complaint(null, "CMP-4758", "Missing road sign at school crossing", Category.ROADS,
                            "Primary School Crossing", Priority.HIGH,
                            "The pedestrian crossing sign is missing and drivers are not slowing down.",
                            PHOTO_ROADS, ComplaintStatus.RESOLVED, "New reflective sign installed at the school crossing.", arunId, arunName),

                    new Complaint(null, "CMP-4742", "Streetlight flickering outside community hall", Category.STREETLIGHTS,
                            "Community Hall, Ward 12", Priority.LOW,
                            "The lamp flickers on and off throughout the evening.",
                            PHOTO_STREETLIGHT, ComplaintStatus.IN_PROGRESS, "Replacement component ordered.", mayaId, mayaName)
            );

            complaintRepository.saveAll(seedList);
            System.out.println(">>> DataSeeder: 8 sample complaints seeded successfully with photos.");
        }
    }

    private void attachPhotosToExistingComplaints() {
        List<Complaint> all = complaintRepository.findAll();
        boolean updated = false;
        for (Complaint c : all) {
            if (c.getPhotoPath() == null || c.getPhotoPath().trim().isEmpty()) {
                c.setPhotoPath(getPhotoForCategory(c.getCategory()));
                updated = true;
            }
        }
        if (updated) {
            complaintRepository.saveAll(all);
            System.out.println(">>> DataSeeder: Updated existing complaints with sample photo URLs.");
        }
    }

    private String getPhotoForCategory(Category category) {
        if (category == null) return PHOTO_ROADS;
        switch (category) {
            case STREETLIGHTS: return PHOTO_STREETLIGHT;
            case GARBAGE: return PHOTO_GARBAGE;
            case ROADS: return PHOTO_ROADS;
            case WATER: return PHOTO_WATER;
            case DRAINAGE: return PHOTO_DRAINAGE;
            case NOISE: return PHOTO_NOISE;
            default: return PHOTO_ROADS;
        }
    }
}
