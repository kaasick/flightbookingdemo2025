package com.cgi.flightbooking.services;

import com.cgi.flightbooking.models.Flight;
import com.cgi.flightbooking.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> getFlightsByDestination(String destination) {
        return flightRepository.findByDestination(destination);
    }

    public List<Flight> getFlightsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay);
    }

    public List<Flight> getFlightsByMaxPrice(double maxPrice) {
        return flightRepository.findByPriceLessThanEqual(maxPrice);
    }

    public List<Flight> searchFlights(
            String destination,
            LocalDate date,
            Double maxPrice) {

        if (destination != null && date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return flightRepository.findByDestinationAndDepartureTimeBetween(
                    destination, startOfDay, endOfDay);
        } else if (destination != null) {
            return flightRepository.findByDestination(destination);
        } else if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay);
        } else if (maxPrice != null) {
            return flightRepository.findByPriceLessThanEqual(maxPrice);
        }

        return flightRepository.findAll();
    }
}
