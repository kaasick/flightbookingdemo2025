# Flight Booking Application Documentation - BackEnd



## Architecture
The application follows a standard Spring Boot architecture with clear separation of concerns:

- **Models**: Data entities (Flight, Aircraft, Seat)
- **Repositories**: Database access using JPQL queries
- **Services**: Business logic
- **Controllers**: REST API endpoints
- **Configuration**: App setup and sample data

## Key Components

### Models
- **Aircraft**: Defines planes with seat layout information
- **Flight**: Contains flight details (number, origin, destination, times, price)
- **Seat**: Represents individual seats with properties (window, aisle, emergency exit)

### Repositories
- **AircraftRepository**: Basic CRUD for aircraft
- **FlightRepository**: Queries for finding and filtering flights
- **SeatRepository**: Queries for seat operations and recommendations

### Controllers
- **FlightController**: Endpoints for flight data and search
- **SeatController**: Endpoints for seat information and recommendations

## Core Services

### FlightService
The `FlightService` handles flight-related operations:

- **getAllFlights()**: Returns all available flights
- **getFlightById()**: Finds a specific flight by ID
- **getFlightsByDestination()**: Filters flights by destination
- **getFlightsByDate()**: Filters flights occurring on a specific date
- **getFlightsByMaxPrice()**: Filters flights by maximum price
- **searchFlights()**: Combined search with multiple filter options

The `searchFlights()` accepts optional parameters and applies different filters based on which parameters are provided. This allows for different search queries from a single endpoint.

### SeatService
The `SeatService` handles seat operations and implements the recommendation algorithm:

- **getAllSeatsForFlight()**: Gets all seats for a flight
- **getAvailableSeatsForFlight()**: Gets only unoccupied seats
- **recommendSeats()**: Core algorithm for seat recommendations
- **generateRandomOccupancy()**: Creates random occupied seats for testing

The `recommendSeats()` method works in several steps:
1. Retrieves all available seats for the flight
2. Filters seats based on user preferences (window, aisle, extra legroom)
3. If seats need to be together, finds consecutive seats in the same row
4. Otherwise, scores each seat based on how well it matches preferences
5. Returns the top-scoring seats

The seat scoring algorithm assigns points:
- Window seat: 3 points (if preferred)
- Aisle seat: 2 points (if preferred)
- Extra legroom: 4 points (if preferred)

The `findSeatsGroupedTogether()` helper method tries to find consecutive seats in the same row, which is crucial for group bookings.

## Data Initialization
The application uses `DataInitializer` to create sample flights and randomly occupied seats for testing purposes.

## API Endpoints

### Flight Endpoints
- `GET /api/flights`: List all flights
- `GET /api/flights/{id}`: Get specific flight
- `GET /api/flights/search`: Search with filters (destination, date, price)

### Seat Endpoints
- `GET /api/seats/flight/{flightId}`: Get all seats for a flight
- `GET /api/seats/flight/{flightId}/available`: Get available seats
- `GET /api/seats/flight/{flightId}/recommend`: Get seat recommendations
- `POST /api/seats/flight/{flightId}/generate`: Generate random occupancy
