package com.cgi.flightbooking.controllers;

import com.cgi.flightbooking.models.Seat;
import com.cgi.flightbooking.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeatsForFlight(@PathVariable Long flightId) {
        return seatService.getAllSeatsForFlight(flightId);
    }

    @GetMapping("/flight/{flightId}/available")
    public List<Seat> getAvailableSeatsForFlight(@PathVariable Long flightId) {
        return seatService.getAvailableSeatsForFlight(flightId);
    }

    @GetMapping("/flight/{flightId}/recommend")
    public List<Seat> recommendSeats(
            @PathVariable Long flightId,
            @RequestParam(defaultValue = "1") int numberOfSeats,
            @RequestParam(defaultValue = "false") boolean preferWindow,
            @RequestParam(defaultValue = "false") boolean preferAisle,
            @RequestParam(defaultValue = "false") boolean preferExtraLegroom,
            @RequestParam(defaultValue = "false") boolean seatsTogetherRequired) {

        return seatService.recommendSeats(
                flightId,
                numberOfSeats,
                preferWindow,
                preferAisle,
                preferExtraLegroom,
                seatsTogetherRequired
        );
    }

    @PostMapping("/flight/{flightId}/generate")
    public void generateRandomOccupancy(
            @PathVariable Long flightId,
            @RequestParam(defaultValue = "0.5") double occupancyRate) {

        seatService.generateRandomOccupancy(flightId, occupancyRate);
    }
}