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
    private Duration duration;
    private boolean shiftHour;

    public long getHoursWorked() {
        var duration = Duration.between(startTime, endTime);
        if (duration.isNegative()) {
            duration = duration.plusHours(24);
        }

        return duration.toHours();
    }
}
