import api from './api';
import { Flight, FlightSearchParams } from '../models/Flight';

const flightService = {
    getAllFlights: async (): Promise<Flight[]> => {
        try {
            const response = await api.get<Flight[]>('/flights');
            return response.data;
        } catch (error) {
            console.error('Error fetching flights:', error);
            throw error;
        }
    },

    getFlightById: async (id: number): Promise<Flight> => {
        try {
            const response = await api.get<Flight>(`/flights/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching flight ${id}:`, error);
            throw error;
        }
    },

    searchFlights: async (params: FlightSearchParams): Promise<Flight[]> => {
        try {
            const response = await api.get<Flight[]>('/flights/search', { params });
            return response.data;
        } catch (error) {
            console.error('Error searching flights:', error);
            throw error;
        }
    }
};

export default flightService;