package com.bloomberryspecial.graphs;

import com.bloomberryspecial.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;
import java.util.List;

@Singleton
@Slf4j
public abstract class BloomberrySpecialChartOverlay extends Overlay {
    private final BloomberrySpecialConfig config;
    protected static final Color buyColor = new Color(124, 226, 64);
    protected static final Color sellColor = new Color(231, 119, 45);

    @Inject
    public BloomberrySpecialChartOverlay(BloomberrySpecialConfig config) {
        this.config = config;

        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.TOP_LEFT);
        setResizable(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension s = super.getPreferredSize();
        if(s == null) {
            return new Dimension(300, 200);
        } else {
            return s;
        }
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        if (graph != null) graph.setPreferredSize(getPreferredSize());
    }

    private GraphEntity graph = null;

    public void itemChanged(ItemModel itemModel) {
        if (itemModel == null) {
            graph = null;
            return;
        }
        List<DataSeries> series = getSeries(itemModel);
        graph = new GraphEntity(config, series, getTitle());
        graph.setPreferredSize(getPreferredSize());
    }

    abstract protected String getTitle();

    abstract protected List<DataSeries> getSeries(ItemModel itemModel);

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph == null) return null;
        return graph.render(graphics);
    }

    public static BloomberrySpecialChartOverlay create(String configKey, BloomberrySpecialConfig config) {
        switch (configKey) {
            case "priceChart":
                return new PriceChart(config);
            case "volumeChart":
                return new VolumeChart(config);
            case "dailyChart":
                return new DailyChart(config);
            case "weeklyChart":
                return new WeeklyChart(config);
            case "marginChart":
                return new MarginChart(config);
            case "abnormalityChart":
                return new AbnormalityChart(config);
        }
        throw new RuntimeException("Unknown chart " + configKey);
    }

    protected static DataSeries buyPriceSeries(ItemModel itemModel) {
        return new DataSeries(
                "Buy Price",
                DataSelector.BUY_PRICE.getData(itemModel),
                buyColor,
                new BasicStroke(1f),
                DrawStyle.LINE
        );
    }

    protected static DataSeries sellPriceSeries(ItemModel itemModel) {
        return new DataSeries(
                "Sell Price",
                DataSelector.SELL_PRICE.getData(itemModel),
                sellColor,
                new BasicStroke(1f),
                DrawStyle.LINE
        );
    }

    protected static DataSeries buyVolumeSeries(ItemModel itemModel) {
        return new DataSeries(
                "Buy Volume",
                DataSelector.BUY_VOLUME.getData(itemModel),
                buyColor,
                new BasicStroke(1f),
                DrawStyle.POINTS
        );
    }

    protected static DataSeries sellVolumeSeries(ItemModel itemModel) {
        return new DataSeries(
                "Sell Volume",
                DataSelector.SELL_VOLUME.getData(itemModel),
                sellColor,
                new BasicStroke(1f),
                DrawStyle.POINTS
        );
    }
}
