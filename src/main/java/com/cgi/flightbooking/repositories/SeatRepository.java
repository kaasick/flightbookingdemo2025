package com.cgi.flightbooking.repositories;

import com.cgi.flightbooking.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isOccupied = :isOccupied")
    List<Seat> findByFlightIdAndIsOccupied(
            @Param("flightId") Long flightId,
            @Param("isOccupied") boolean isOccupied
    );

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId")
    List<Seat> findByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isWindow = :isWindow AND s.isOccupied = :isOccupied")
    List<Seat> findByFlightIdAndIsWindowAndIsOccupied(
            @Param("flightId") Long flightId,
            @Param("isWindow") boolean isWindow,
            @Param("isOccupied") boolean isOccupied
    );

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isAisle = :isAisle AND s.isOccupied = :isOccupied")
    List<Seat> findByFlightIdAndIsAisleAndIsOccupied(
            @Param("flightId") Long flightId,
            @Param("isAisle") boolean isAisle,
            @Param("isOccupied") boolean isOccupied
    );

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isEmergencyExit = :isEmergencyExit AND s.isOccupied = :isOccupied")
    List<Seat> findByFlightIdAndIsEmergencyExitAndIsOccupied(
            @Param("flightId") Long flightId,
            @Param("isEmergencyExit") boolean isEmergencyExit,
            @Param("isOccupied") boolean isOccupied
    );

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatClass = :seatClass AND s.isOccupied = :isOccupied")
    List<Seat> findByFlightIdAndSeatClassAndIsOccupied(
            @Param("flightId") Long flightId,
            @Param("seatClass") String seatClass,
            @Param("isOccupied") boolean isOccupied
    );

    // More complex JPQL query for advanced seat recommendations
    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.isOccupied = false AND " +
            "(:preferWindow = false OR s.isWindow = true) AND " +
            "(:preferAisle = false OR s.isAisle = true) AND " +
            "(:preferExtraLegroom = false OR s.isEmergencyExit = true OR s.hasExtraLegroom = true) " +
            "ORDER BY " +
            "CASE WHEN :preferWindow = true AND s.isWindow = true THEN 3 ELSE 0 END + " +
            "CASE WHEN :preferAisle = true AND s.isAisle = true THEN 2 ELSE 0 END + " +
            "CASE WHEN :preferExtraLegroom = true AND (s.isEmergencyExit = true OR s.hasExtraLegroom = true) THEN 4 ELSE 0 END DESC")
    List<Seat> findRecommendedSeats(
            @Param("flightId") Long flightId,
            @Param("preferWindow") boolean preferWindow,
            @Param("preferAisle") boolean preferAisle,
            @Param("preferExtraLegroom") boolean preferExtraLegroom
    );
}
