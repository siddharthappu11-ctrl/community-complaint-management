package com.wardconnect.ccms.repository;

import com.wardconnect.ccms.enums.Category;
import com.wardconnect.ccms.enums.ComplaintStatus;
import com.wardconnect.ccms.model.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Complaint entity database access.
 */
@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    Optional<Complaint> findByTrackingId(String trackingId);

    List<Complaint> findByResidentIdOrderByCreatedAtDesc(String residentId);

    List<Complaint> findAllByOrderByCreatedAtDesc();

    long countByStatus(ComplaintStatus status);

    @Query("{ '$and': [ " +
            "?0 != null ? { 'status': ?0 } : {}, " +
            "?1 != null ? { 'category': ?1 } : {}, " +
            "?2 != null ? { '$or': [ " +
            "  { 'title': { '$regex': ?2, '$options': 'i' } }, " +
            "  { 'trackingId': { '$regex': ?2, '$options': 'i' } }, " +
            "  { 'location': { '$regex': ?2, '$options': 'i' } }, " +
            "  { 'description': { '$regex': ?2, '$options': 'i' } } " +
            "] } : {} " +
            "] }")
    List<Complaint> filterComplaints(ComplaintStatus status, Category category, String regexSearch);
}
