import api from './api';
import { Seat, SeatPreferences } from '../models/Seat';

const seatService = {
    getSeatsForFlight: async (flightId: number): Promise<Seat[]> => {
        try {
            const response = await api.get<Seat[]>(`/seats/flight/${flightId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching seats for flight ${flightId}:`, error);
            throw error;
        }
    },

    getAvailableSeats: async (flightId: number): Promise<Seat[]> => {
        try {
            const response = await api.get<Seat[]>(`/seats/flight/${flightId}/available`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching available seats for flight ${flightId}:`, error);
            throw error;
        }
    },

    recommendSeats: async (flightId: number, preferences: SeatPreferences): Promise<Seat[]> => {
        try {
            const response = await api.get<Seat[]>(`/seats/flight/${flightId}/recommend`, {
                params: preferences
            });
            return response.data;
        } catch (error) {
            console.error(`Error getting seat recommendations for flight ${flightId}:`, error);
            throw error;
        }
    }
};