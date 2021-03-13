package com.bloomberryspecial;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("BloomberrySpecial")
public interface BloomberrySpecialConfig extends Config {
	@ConfigItem(
			position = 2,
			keyName = "backgroundColor",
			name = "Background Color",
			description = "The background color of the graph."
	)
	default Color backgroundColor() {
		return new Color(132, 109, 71);
	}

	@Range(max = 100)
	@ConfigItem(
			position = 3,
			keyName = "backgroundTransparency",
			name = "Background Transparency",
			description = "The background transparency %."
	)
	default int backgroundTransparency() {
		return 15;
	}

	@ConfigItem(
			position = 4,
			keyName = "graphColor",
			name = "Graph Line/Text Color",
			description = "The color of the graph lines, text and border"
	)
	default Color graphColor() {
		return new Color(56, 36, 24);
	}

	@ConfigItem(
			keyName = "priceChart",
			name = "Price Chart",
			description = "Shows the buy and sell price over time"
	)
	default boolean priceChart() {
		return true;
	}

	@ConfigItem(
			keyName = "volumeChart",
			name = "Volume Chart",
			description = "Shows the buy and sell volume over time"
	)
	default boolean volumeChart() {
		return true;
	}

	@ConfigItem(
			keyName = "dailyChart",
			name = "Daily Price Chart",
			description = "Shows the daily price trend"
	)
	default boolean dailyChart() {
		return false;
	}

	@ConfigItem(
			keyName = "weeklyChart",
			name = "Weekly Chart",
			description = "Shows the weekly price trend"
	)
	default boolean weeklyChart() {
		return false;
	}

	@ConfigItem(
			keyName = "marginChart",
			name = "Margin Chart",
			description = "Shows the margin between the buy and sell price over time"
	)
	default boolean marginChart() {
		return false;
	}

	@ConfigItem(
			keyName = "abnormalityChart",
			name = "Abnormality Chart",
			description = "Shows the price relative to its expected range. Used to show when the price is spiking / crashing"
	)
	default boolean abnormalityChart() {
		return false;
	}
}
