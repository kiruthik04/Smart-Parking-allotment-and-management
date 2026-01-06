package com.smartparking.smart_parking_backend.repository;

import com.smartparking.smart_parking_backend.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {
    List<Payout> findByOwnerId(Long ownerId);

    @Query("SELECT SUM(p.amount) FROM Payout p WHERE p.owner.id = :ownerId AND p.status = 'PAID'")
    Double getTotalPaidToOwner(Long ownerId);
}
