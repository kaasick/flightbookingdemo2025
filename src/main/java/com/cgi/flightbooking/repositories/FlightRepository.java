package com.cgi.flightbooking.repositories;

import com.cgi.flightbooking.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.destination = :destination")
    List<Flight> findByDestination(@Param("destination") String destination);

    @Query("SELECT f FROM Flight f WHERE f.departureTime BETWEEN :startTime AND :endTime")
    List<Flight> findByDepartureTimeBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT f FROM Flight f WHERE f.price <= :maxPrice")
    List<Flight> findByPriceLessThanEqual(@Param("maxPrice") double maxPrice);

    @Query("SELECT f FROM Flight f WHERE f.destination = :destination AND f.departureTime BETWEEN :startDate AND :endDate")
    List<Flight> findByDestinationAndDepartureTimeBetween(
            @Param("destination") String destination,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
