package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data

public class DayGuard {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private boolean shiftHour;

    public long getHoursWorked() {
        return Duration.between(startTime, endTime).toHours();
    }
}
