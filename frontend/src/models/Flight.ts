import { Aircraft } from './Aircraft';
import { Seat } from './Seat';

export interface Flight {
    id: number;
    flightNumber: string;
    origin: string;
    destination: string;
    departureTime: string;
    arrivalTime: string;
    price: number;
    aircraft?: Aircraft;
    seats?: Seat[];
}

export interface FlightSearchParams {
    destination?: string;
    date?: string;
    maxPrice?: number;
}