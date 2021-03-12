package com.bloomberryspecial;

import com.bloomberryspecial.transformers.TransformerType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("BloomberrySpecial")
public interface BloomberrySpecialConfig extends Config {
	@ConfigItem(
			position = 0,
			keyName = "priceGraphWidth",
			name = "Price Graph Width",
			description = "Configures the width of the price graph."
	)
	default int priceGraphWidth() {
		return 300;
	}

	@ConfigItem(
			position = 1,
			keyName = "priceGraphHeight",
			name = "Price Graph Height",
			description = "Configures the height of the price graph."
	)
	default int priceGraphHeight() {
		return 200;
	}

	@ConfigItem(
			position = 0,
			keyName = "volumeGraphWidth",
			name = "Volume Graph Width",
			description = "Configures the width of the volume graph."
	)
	default int volumeGraphWidth() {
		return 300;
	}

	@ConfigItem(
			position = 1,
			keyName = "volumeGraphHeight",
			name = "Volume Graph Height",
			description = "Configures the height of the volume graph."
	)
	default int volumeGraphHeight() {
		return 200;
	}

	@ConfigItem(
			position = 0,
			keyName = "analysisGraphWidth",
			name = "Analysis Graph Width",
			description = "Configures the width of the analysis graph."
	)
	default int analysisGraphWidth() {
		return 300;
	}

	@ConfigItem(
			position = 1,
			keyName = "analysisGraphHeight",
			name = "Analysis Graph Height",
			description = "Configures the height of the analysis graph."
	)
	default int analysisGraphHeight() {
		return 200;
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
		return 20;
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
		return 60;
	}

	@ConfigItem(
			keyName = "pointSize",
			name = "Point Size",
			description = "The size of the points when in point display mode",
			secret = true
	)
	default int pointSize() {
		return 4;
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

	@ConfigItem(
			keyName = "showPriceChart",
			name = "Show Price Chart",
			description = "Whether to show the price chart",
			secret = true
	)
	default boolean showPriceChart() {
		return true;
	}

	@ConfigItem(
			keyName = "showVolumeChart",
			name = "Show Volume Chart",
			description = "Whether to show the volume chart",
			secret = true
	)
	default boolean showVolumeChart() {
		return true;
	}

	@ConfigItem(
			keyName = "showAnalysisChart",
			name = "Show Analysis Chart",
			description = "Whether to show the analysis chart",
			secret = true
	)
	default boolean showAnalysis() {
		return true;
	}

	@ConfigItem(
			keyName = "transformerType",
			name = "Analysis Type",
			description = "What kind of analysis to do",
			secret = true
	)
	default TransformerType transformerType() {
		return TransformerType.MOVING_AVG;
	}

	@ConfigItem(
			keyName = "movingAvgBaseData",
			name = "Moving Avg Base Data",
			description = "The data that the moving avg chart is based on",
			secret = true
	)
	default DataSelector movingAvgBaseData() {
		return DataSelector.BUY_PRICE;
	}

	@ConfigItem(
			keyName = "movingAvgWindow",
			name = "Moving Avg Window",
			description = "The number of datapoints in the moving average window."
	)
	default int movingAvgWindow() {
		return 25;
	}
}
