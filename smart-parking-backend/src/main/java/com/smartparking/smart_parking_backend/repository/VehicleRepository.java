package com.smartparking.smart_parking_backend.repository;

import com.smartparking.smart_parking_backend.model.Vehicle;
import com.smartparking.smart_parking_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>
{
    List<Vehicle> findByUser(User user);
}