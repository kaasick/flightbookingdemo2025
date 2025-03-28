import { Flight } from './Flight';

export interface Seat {
    id: number;
    flight?: Flight;
    seatNumber: string;
    row: number;
    column: string;
    isWindow: boolean;
    isAisle: boolean;
    isEmergencyExit: boolean;
    hasExtraLegroom: boolean;
    isOccupied: boolean;
    seatClass: string;
    isRecommended?: boolean;
}

export interface SeatPreferences {
    numberOfSeats: number;
    preferWindow: boolean;
    preferAisle: boolean;
    preferExtraLegroom: boolean;
    seatsTogetherRequired: boolean;
}