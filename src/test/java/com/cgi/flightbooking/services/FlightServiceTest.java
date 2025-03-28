package com.cgi.flightbooking.services;

import com.cgi.flightbooking.models.Flight;
import com.cgi.flightbooking.repositories.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        flight1 = new Flight();
        flight1.setId(1L);
        flight1.setFlightNumber("BA123");
        flight1.setOrigin("Tallinn");
        flight1.setDestination("London");
        flight1.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight1.setPrice(new BigDecimal("149.99"));

        flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("AF456");
        flight2.setOrigin("Tallinn");
        flight2.setDestination("Paris");
        flight2.setDepartureTime(LocalDateTime.now().plusDays(2));
        flight2.setPrice(new BigDecimal("179.99"));
    }

    @Test
    public void testGetAllFlights() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1, flight2));

        List<Flight> flights = flightService.getAllFlights();

        assertEquals(2, flights.size());
    }

    @Test
    public void testGetFlightById() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight1));

        Optional<Flight> result = flightService.getFlightById(1L);

        assertTrue(result.isPresent());
        assertEquals("London", result.get().getDestination());
    }

    @Test
    public void testSearchFlightsByDestination() {
        when(flightRepository.findByDestination("London")).thenReturn(Arrays.asList(flight1));

        List<Flight> flights = flightService.searchFlights("London", null, null);

        assertEquals(1, flights.size());
        assertEquals("London", flights.get(0).getDestination());
    }

    @Test
    public void testSearchFlightsByDate() {
        LocalDate date = LocalDate.now().plusDays(1);
        when(flightRepository.findByDepartureTimeBetween(any(), any())).thenReturn(Arrays.asList(flight1));

        List<Flight> flights = flightService.searchFlights(null, date, null);

        assertEquals(1, flights.size());
    }
}