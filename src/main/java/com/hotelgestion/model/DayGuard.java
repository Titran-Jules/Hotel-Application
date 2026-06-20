package com.hotelgestion.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
public class DayGuard {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private boolean shiftHour;

    public long getHoursWorked() {
        return Duration.between(startTime, endTime).toHours();
    }
}
