package com.cgi.flightbooking.config;

import com.cgi.flightbooking.models.Aircraft;
import com.cgi.flightbooking.models.Flight;
import com.cgi.flightbooking.models.Seat;
import com.cgi.flightbooking.repositories.AircraftRepository;
import com.cgi.flightbooking.repositories.FlightRepository;
import com.cgi.flightbooking.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final SeatRepository seatRepository;
    private final Random random = new Random();

    @Autowired
    public DataInitializer(
            FlightRepository flightRepository,
            AircraftRepository aircraftRepository,
            SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(String... args) {
        // Define aircraft models
        Aircraft boeing737 = new Aircraft();
        boeing737.setModel("Boeing 737-800");
        boeing737.setRows(30);
        boeing737.setSeatsPerRow(6);
        boeing737.setSeatLayout("ABC-DEF");

        Aircraft airbus320 = new Aircraft();
        airbus320.setModel("Airbus A320");
        airbus320.setRows(28);
        airbus320.setSeatsPerRow(6);
        airbus320.setSeatLayout("ABC-DEF");

        aircraftRepository.save(boeing737);
        aircraftRepository.save(airbus320);

        // Create sample flights
        List<Flight> flights = new ArrayList<>();

        // Flight to London
        Flight londonFlight = new Flight();
        londonFlight.setFlightNumber("BA123");
        londonFlight.setOrigin("Tallinn");
        londonFlight.setDestination("London");
        londonFlight.setDepartureTime(LocalDateTime.now().plusDays(2).withHour(8).withMinute(30));
        londonFlight.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(10).withMinute(45));
        londonFlight.setPrice(new BigDecimal("149.99"));
        londonFlight.setAircraft(boeing737);
        flights.add(londonFlight);

        // Flight to Paris
        Flight parisFlight = new Flight();
        parisFlight.setFlightNumber("AF456");
        parisFlight.setOrigin("Tallinn");
        parisFlight.setDestination("Paris");
        parisFlight.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(15));
        parisFlight.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(30));
        parisFlight.setPrice(new BigDecimal("179.99"));
        parisFlight.setAircraft(airbus320);
        flights.add(parisFlight);

        // Flight to Berlin
        Flight berlinFlight = new Flight();
        berlinFlight.setFlightNumber("LH789");
        berlinFlight.setOrigin("Tallinn");
        berlinFlight.setDestination("Berlin");
        berlinFlight.setDepartureTime(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0));
        berlinFlight.setArrivalTime(LocalDateTime.now().plusDays(3).withHour(11).withMinute(30));
        berlinFlight.setPrice(new BigDecimal("129.99"));
        berlinFlight.setAircraft(boeing737);
        flights.add(berlinFlight);

        // Save all flights
        flightRepository.saveAll(flights);

        // Generate seats for each flight
        for (Flight flight : flights) {
            generateSeatsForFlight(flight);
        }
    }

    private void generateSeatsForFlight(Flight flight) {
        Aircraft aircraft = flight.getAircraft();
        String[] seatLayout = aircraft.getSeatLayout().split("-");

        List<Seat> seats = new ArrayList<>();

        for (int row = 1; row <= aircraft.getRows(); row++) {
            int seatCounter = 0;
            boolean isEmergencyExit = (row == 15); // Example: row 15 is emergency exit

            for (String section : seatLayout) {
                for (char column : section.toCharArray()) {
                    Seat seat = new Seat();
                    seat.setFlight(flight);
                    seat.setRow(row);
                    seat.setColumn(String.valueOf(column));
                    seat.setSeatNumber(row + String.valueOf(column));

                    // Set seat properties
                    seat.setWindow(column == section.charAt(0) || column == section.charAt(section.length() - 1));
                    seat.setAisle(seatLayout.length > 1 && (column == section.charAt(section.length() - 1) || column == section.charAt(0)));
                    seat.setEmergencyExit(isEmergencyExit);
                    seat.setHasExtraLegroom(isEmergencyExit || row == 1);

                    // Randomly set occupancy
                    seat.setOccupied(random.nextDouble() < 0.3); // 30% occupancy

                    // Set seat class
                    if (row <= 2) {
                        seat.setSeatClass("FIRST");
                    } else if (row <= 7) {
                        seat.setSeatClass("BUSINESS");
                    } else {
                        seat.setSeatClass("ECONOMY");
                    }

                    seats.add(seat);
                    seatCounter++;
                }
            }
        }

        seatRepository.saveAll(seats);
    }
}