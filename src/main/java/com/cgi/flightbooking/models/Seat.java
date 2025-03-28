package com.cgi.flightbooking.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The flight this seat belongs to
    @ManyToOne
    private Flight flight;

    private String seatNumber; // e.g., "12A"
    private int row;
    private String column; // e.g., "A", "B", etc.

    private boolean isWindow;
    private boolean isAisle;
    private boolean isEmergencyExit;
    private boolean hasExtraLegroom;

    private boolean isOccupied;
    private String seatClass; // "ECONOMY", "BUSINESS", "FIRST"
}
