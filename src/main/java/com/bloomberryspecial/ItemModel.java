package com.bloomberryspecial;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

import java.util.List;

@Value
public class ItemModel {
    ItemComposition item;
    List<RLHistoricalDatapoint> historicalData;
    AsyncBufferedImage image;
}
