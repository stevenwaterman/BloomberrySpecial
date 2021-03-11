package com.bloomberryspecial;

import com.bloomberryspecial.transformers.Daily;
import com.bloomberryspecial.transformers.Margin;
import com.bloomberryspecial.transformers.MovingAvg;
import com.bloomberryspecial.transformers.TransformerType;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;

@Singleton
@Slf4j
public class BloomberrySpecialAnalysisOverlay extends OverlayPanel {
    private final BloomberrySpecialPlugin plugin;
    private final BloomberrySpecialConfig config;

    @Inject
    public BloomberrySpecialAnalysisOverlay(BloomberrySpecialPlugin plugin, BloomberrySpecialConfig config) {
        this.plugin  = plugin;
        this.config = config;

        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setWrap(true);
        panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        if(graph != null) {
            graph.setPreferredSize(preferredSize);
            plugin.setConfig("analysisGraphWidth", preferredSize.width);
            plugin.setConfig("analysisGraphHeight", preferredSize.height);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if(graph == null) {
            return new Dimension(0, 0);
        } else {
            return graph.getPreferredSize();
        }
    }

    private GraphEntity graph = null;
    public void updated() {
        final ItemModel itemModel = plugin.getItemModel();
        if (itemModel == null) {
            graph = null;
            return;
        }

        if (config.transformerType() == TransformerType.MOVING_AVG) {
            graph = new GraphEntity(
                    itemModel,
                    plugin.getConfig(),
                    Lists.newArrayList(config.movingAvgBaseData()),
                    Lists.newArrayList(new MovingAvg(config.movingAvgBaseData(), config.movingAvgWindow())),
                    "Moving Average",
                    new Dimension(config.analysisGraphWidth(), config.analysisGraphHeight()));
        } else if (config.transformerType() == TransformerType.MARGIN) {
            graph = new GraphEntity(
                    itemModel,
                    plugin.getConfig(),
                    Lists.newArrayList(),
                    Lists.newArrayList(new Margin()),
                    "Price Margin",
                    new Dimension(config.analysisGraphWidth(), config.analysisGraphHeight()));
        } else if (config.transformerType() == TransformerType.DAILY) {
            graph = new GraphEntity(
                    itemModel,
                    plugin.getConfig(),
                    Lists.newArrayList(),
                    Lists.newArrayList(new Daily(config.movingAvgBaseData())),
                    "Daily",
                    new Dimension(config.analysisGraphWidth(), config.analysisGraphHeight()));
        } else if (config.transformerType() == TransformerType.WEEKLY) {
            graph = new GraphEntity(
                itemModel,
                plugin.getConfig(),
                Lists.newArrayList(),
                Lists.newArrayList(new Daily(config.movingAvgBaseData())),
                "Weekly",
                new Dimension(config.analysisGraphWidth(), config.analysisGraphHeight()));
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph != null && config.showAnalysis()) panelComponent.getChildren().add(graph);
        return super.render(graphics);
    }
}
