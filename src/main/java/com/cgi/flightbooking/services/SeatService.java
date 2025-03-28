package com.cgi.flightbooking.services;

import com.cgi.flightbooking.models.Seat;
import com.cgi.flightbooking.repositories.FlightRepository;
import com.cgi.flightbooking.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService {


    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    public List<Seat> getAllSeatsForFlight(Long flightId) {
        return seatRepository.findByFlightId(flightId);
    }

    public List<Seat> getAvailableSeatsForFlight(Long flightId) {
        return seatRepository.findByFlightIdAndIsOccupied(flightId, false);
    }

    // Method to recommend seats based on preferences
    public List<Seat> recommendSeats(
            Long flightId,
            int numberOfSeats,
            boolean preferWindow,
            boolean preferAisle,
            boolean preferExtraLegroom,
            boolean seatsTogetherRequired) {

        // Get all available seats for the flight
        List<Seat> availableSeats = getAvailableSeatsForFlight(flightId);

        // No available seats
        if (availableSeats.isEmpty()) {
            return Collections.emptyList();
        }

        // Filter seats based on preferences
        List<Seat> candidateSeats = availableSeats.stream()
                .filter(seat -> !preferWindow || seat.isWindow())
                .filter(seat -> !preferAisle || seat.isAisle())
                .filter(seat -> !preferExtraLegroom || seat.isEmergencyExit() || seat.isHasExtraLegroom())
                .collect(Collectors.toList());

        // If no seats match the criteria, return all available seats
        if (candidateSeats.isEmpty()) {
            candidateSeats = availableSeats;
        }

        // If seats together required, and we need more than one seat
        if (seatsTogetherRequired && numberOfSeats > 1) {
            return findSeatsGroupedTogether(candidateSeats, numberOfSeats);
        }
        candidateSeats.sort((s1, s2) -> {
            int score1 = calculateSeatScore(s1, preferWindow, preferAisle, preferExtraLegroom);
            int score2 = calculateSeatScore(s2, preferWindow, preferAisle, preferExtraLegroom);
            return Integer.compare(score2, score1); // Higher score first
        });

        // Return the top N seats based on score
        return candidateSeats.stream()
                .limit(numberOfSeats)
                .collect(Collectors.toList());
    }

    private int calculateSeatScore(Seat seat, boolean preferWindow, boolean preferAisle, boolean preferExtraLegroom) {
        int score = 0;
        if (preferWindow && seat.isWindow()) score += 3;
        if (preferAisle && seat.isAisle()) score += 2;
        if (preferExtraLegroom && (seat.isEmergencyExit() || seat.isHasExtraLegroom())) score += 4;
        return score;
    }

    private List<Seat> findSeatsGroupedTogether(List<Seat> seats, int numberOfSeats) {
        // Group seats by row
        Map<Integer, List<Seat>> seatsByRow = seats.stream()
                .collect(Collectors.groupingBy(Seat::getRow));

        // Check each row to find consecutive seats
        for (Map.Entry<Integer, List<Seat>> entry : seatsByRow.entrySet()) {
            List<Seat> rowSeats = entry.getValue();

            // Sort seats in the row by column
            rowSeats.sort(Comparator.comparing(Seat::getColumn));

            // Find consecutive seats
            List<Seat> consecutiveSeats = findConsecutiveSeats(rowSeats, numberOfSeats);
            if (!consecutiveSeats.isEmpty()) {
                return consecutiveSeats;
            }
        }

        // If we couldn't find enough consecutive seats, just return the top scoring seats
        return seats.stream().limit(numberOfSeats).collect(Collectors.toList());
    }

    private List<Seat> findConsecutiveSeats(List<Seat> rowSeats, int numberOfSeats) {
        if (rowSeats.size() < numberOfSeats) {
            return Collections.emptyList();
        }

        for (int i = 0; i <= rowSeats.size() - numberOfSeats; i++) {
            boolean consecutive = true;
            // Check if columns are consecutive (might need tweaking based on your column naming)
            for (int j = 0; j < numberOfSeats - 1; j++) {
                char current = rowSeats.get(i + j).getColumn().charAt(0);
                char next = rowSeats.get(i + j + 1).getColumn().charAt(0);
                if (next - current != 1) {
                    consecutive = false;
                    break;
                }
            }

            if (consecutive) {
                return rowSeats.subList(i, i + numberOfSeats);
            }
        }

        return Collections.emptyList();
    }

    // Generate random seat occupancy for a flight
    public void generateRandomOccupancy(Long flightId, double occupancyRate) {
        List<Seat> seats = seatRepository.findByFlightId(flightId);
        Random random = new Random();

        for (Seat seat : seats) {
            if (random.nextDouble() < occupancyRate) {
                seat.setOccupied(true);
                seatRepository.save(seat);
            }
        }
    }
}


