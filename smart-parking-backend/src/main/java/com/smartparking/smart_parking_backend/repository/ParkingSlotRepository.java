package com.smartparking.smart_parking_backend.repository;

import com.smartparking.smart_parking_backend.model.ParkingSlot ;
import com.smartparking.smart_parking_backend.model.User;
//JpaRepository - provides save(), findAll(), findById(), deleteById(), count()
//Spring generates a proxy implementation at runtime, we write ZERO SQL.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//Pessimistic Locking
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository //DB layer, exception translation, register bean in Spring context.

//jpa___<Entity class, Primary key type>
//Spring knows now - which table, ID, mapping.
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long>
{
    //Exclusive lock - says Db, if i select any row, lock it. Dont let anyone to read or write.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    //JAVA PERSISTENCE QUERY LANG - Java style custom Sql DB query. To find specific slot.
    @Query("SELECT s FROM ParkingSlot s WHERE s.id = :id")
    //param - links the method's id to variable (:id) inside the sql query .it prevents SQL injection.
    //Custom repo - method locks the record.

    //doesn't work as The orElseThrow used in service only exist in optional. so remove the below.
//    ParkingSlot findSlotForUpdate(@Param("id") Long id);
    Optional<ParkingSlot> findSlotForUpdate(@Param("id") Long id);

    long countByOwner_Email(String email);

    List<ParkingSlot> findByOwner_Email(String email);

    List<ParkingSlot> findByOwner(User owner);

}