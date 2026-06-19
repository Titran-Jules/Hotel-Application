package com.hotelgestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Data
@Getter
@Setter
public class DayGuard {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private boolean shiftHour;

    public long getHoursWorked() {
        return Duration.between(startTime, endTime).toHours();
    }
}
