package com.hotelgestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SuiteRoom extends Room {
    private int numbreOfRoom;
    private boolean jacouzi;

    @Override
    public double getRealPrice() {
        return isJacouzi() ? getBasePrice() + (50*numbreOfRoom) + (100) : getBasePrice() + (50*numbreOfRoom);
    }
}
