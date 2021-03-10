package com.bloomberryspecial;

import lombok.Value;

@Value
public class RLHistoricalDatapoint {
    int timestamp;
    int avgHighPrice;
    int avgLowPrice;
    int highPriceVolume;
    int lowPriceVolume;
}
