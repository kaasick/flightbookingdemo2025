package com.cgi.flightbooking.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private int row;
    private String column;
    private boolean isWindow;
    private boolean isAisle;
    private boolean isEmergencyExit;
    private boolean hasExtraLegroom;
    private boolean isOccupied;
    private String seatClass;
    private boolean isRecommended;
}