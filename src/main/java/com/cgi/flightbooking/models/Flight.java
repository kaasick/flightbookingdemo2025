package com.cgi.flightbooking.models;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;

    // Many flights can use the same aircraft model
    @ManyToOne
    private Aircraft aircraft;

    // One flight has many seats
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<Seat> seats;
}
