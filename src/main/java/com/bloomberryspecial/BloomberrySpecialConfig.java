package com.bloomberryspecial;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("BloomberrySpecial")
public interface BloomberrySpecialConfig extends Config {
	@ConfigItem(
			position = 0,
			keyName = "graphWidth",
			name = "Graph Width",
			description = "Configures the width of the graph."
	)
	default int graphWidth() {
		return 600;
	}

	@ConfigItem(
			position = 1,
			keyName = "graphHeight",
			name = "Graph Height",
			description = "Configures the height of the graph."
	)
	default int graphHeight() {
		return 250;
	}

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
			position = 5,
			keyName = "buyColor",
			name = "Buy Line Color",
			description = "Color of the buy graph line"
	)
	default Color buyColor() {
		return new Color(64, 210, 55);
	}

	@ConfigItem(
			position = 5,
			keyName = "sellColor",
			name = "Sell Line Color",
			description = "Color of the sell graph line"
	)
	default Color sellColor() {
		return new Color(234, 117, 37);
	}

	@ConfigItem(
			position = 6,
			keyName = "analysisColor",
			name = "Analysis Line Color",
			description = "Color of the analysis graph line"
	)
	default Color analysisColor() {
		return new Color(37, 178, 234);
	}

	@ConfigItem(
			position = 7,
			keyName = "drawStyle",
			name = "Graph Style",
			description = "How to draw the graph"
	)
	default DrawStyle drawStyle() {
		return DrawStyle.POINTS;
	}

	@ConfigItem(
			position = 8,
			keyName = "analysisDrawStyle",
			name = "Analysis Style",
			description = "How to draw the analysis graph"
	)
	default DrawStyle analysisDrawStyle() {
		return DrawStyle.LINE;
	}

	@ConfigItem(
			keyName = "marginTop",
			name = "Top Margin",
			description = "The space above the content",
			secret = true
	)
	default int marginTop() {
		return 10;
	}

	@ConfigItem(
			keyName = "marginRight",
			name = "Top Right",
			description = "The space to the right of the content",
			secret = true
	)
	default int marginRight() {
		return 10;
	}

	@ConfigItem(
			keyName = "marginBottom",
			name = "Bottom Margin",
			description = "The space below the content",
			secret = true
	)
	default int marginBottom() {
		return 30;
	}

	@ConfigItem(
			keyName = "marginLeft",
			name = "Left Margin",
			description = "The space to the left of the content",
			secret = true
	)
	default int marginLeft() {
		return 50;
	}

	@ConfigItem(
			keyName = "pointSize",
			name = "Point Size",
			description = "The size of the points when in point display mode",
			secret = true
	)
	default int pointSize() {
		return 2;
	}

	@ConfigItem(
			keyName = "markHeight",
			name = "Mark Height",
			description = "The height of the x-axis marks",
			secret = true
	)
	default int markHeight() {
		return 10;
	}

	@ConfigItem(
			keyName = "markWidth",
			name = "Mark Width",
			description = "The width of the y-axis marks",
			secret = true
	)
	default int markWidth() {
		return 5;
	}
}
