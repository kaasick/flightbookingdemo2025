import { Flight } from './Flight';

export interface Aircraft {
    id: number;
    model: string;
    rows: number;
    seatsPerRow: number;
    seatLayout: string;
    flights?: Flight[];
}