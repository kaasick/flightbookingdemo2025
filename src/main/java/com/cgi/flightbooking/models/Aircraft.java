package com.cgi.flightbooking.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private int rows;
    private int seatsPerRow;

    // Conf string representing seat layout
    private String seatLayout;

    // One aircraft can do many flights
    @OneToMany(mappedBy = "aircraft")
    private List<Flight> flights;
}
