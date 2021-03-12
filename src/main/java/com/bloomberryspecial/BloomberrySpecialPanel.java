package com.bloomberryspecial;

import com.bloomberryspecial.transformers.*;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Singleton
class BloomberrySpecialPanel extends PluginPanel {
    private BloomberrySpecialPlugin plugin;

    @Inject
    BloomberrySpecialPanel(BloomberrySpecialPlugin plugin, BloomberrySpecialConfig config) {
        setLayout(new DynamicGridLayout(0, 2));
        this.plugin = plugin;
        setBorder(null);

        new Checkbox(this, "Price", false, itemModel -> Lists.newArrayList(
            new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.LINE),
            new DataSeries("Sell Price", DataSelector.SELL_PRICE.getData(itemModel), config.sellColor(), new BasicStroke(1f), DrawStyle.LINE)
        ), "Shows the buy and sell price of the item over time");

        new Checkbox(this, "Volume", false, itemModel -> Lists.newArrayList(
            new DataSeries("Buy Volume", DataSelector.BUY_VOLUME.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.LINE),
            new DataSeries("Sell Volume", DataSelector.SELL_VOLUME.getData(itemModel), config.sellColor(), new BasicStroke(1f), DrawStyle.LINE)
        ), "Shows the buy and sell volume of the item over time");

        new Checkbox(this, "Margin", false, itemModel -> {
            DataSeries buyPrice = new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.LINE);
            DataSeries sellPrice = new DataSeries("Sell Price", DataSelector.SELL_PRICE.getData(itemModel), config.sellColor(), new BasicStroke(1f), DrawStyle.LINE);
            List<DataSeries> margin = new Subtract().getData(buyPrice, sellPrice);
            List<DataSeries> movingAvg = new MovingAvg(25).getData(margin);
            List<DataSeries> emphasise = new Emphasise().getData(movingAvg);
            List<DataSeries> display = new ArrayList<>();
            display.addAll(margin);
            display.addAll(emphasise);
            return display;
        }, "Shows the margin between the buy and sell prices over time");

        new Checkbox(this, "Daily", false, itemModel -> {
            DataSeries buyPrice = new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.POINTS);
            List<DataSeries> removedTrend = new NormaliseTrend().getData(buyPrice);
            List<DataSeries> daily = new Modulo(Modulo.ModuloPeriod.DAY).getData(removedTrend);
            List<DataSeries> normalised = new Every(new NormalisePercentage()).getData(daily);
            List<DataSeries> movingAvg = new Every(new MovingAvg(25)).getData(normalised);
            List<DataSeries> combine = new CombineX().getData(movingAvg);
            List<DataSeries> emphasise = new Emphasise().getData(combine);
            List<DataSeries> display = new ArrayList<>();
            display.addAll(normalised);
            display.addAll(emphasise);
            return display;
        }, "Shows the buy price (%) vs time of day");

        new Checkbox(this, "Weekly", false, itemModel -> {
            DataSeries buyPrice = new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.POINTS);
            List<DataSeries> removedTrend = new NormaliseTrend().getData(buyPrice);
            List<DataSeries> daily = new Modulo(Modulo.ModuloPeriod.WEEK).getData(removedTrend);
            List<DataSeries> normalised = new Every(new NormalisePercentage()).getData(daily);
            List<DataSeries> movingAvg = new Every(new MovingAvg(25)).getData(normalised);
            List<DataSeries> combine = new CombineX().getData(movingAvg);
            List<DataSeries> emphasise = new Emphasise().getData(combine);
            List<DataSeries> display = new ArrayList<>();
            display.addAll(normalised);
            display.addAll(emphasise);
            return display;
        }, "Shows the buy price (%) vs day of week");

        new Checkbox(this, "Abnormality", false, itemModel -> {
            DataSeries buyPrice = new DataSeries("Buy Price", DataSelector.BUY_PRICE.getData(itemModel), config.buyColor(), new BasicStroke(1f), DrawStyle.POINTS);
            List<DataSeries> removedTrend = new NormaliseTrend().getData(buyPrice);
            List<DataSeries> normalised = new NormalisePercentage().getData(removedTrend);
            List<DataSeries> standardError = new StandardError().getData(normalised);
            List<DataSeries> emphasise = new Every(new Emphasise()).getData(standardError);
            List<DataSeries> display = new ArrayList<>();
            display.addAll(normalised);
            display.addAll(emphasise);
            return display;
        }, "Indicates how unusually high/low a price is");
    }

    private static class Checkbox {
        private Runnable remove = () -> {};

        public Checkbox(BloomberrySpecialPanel panel, String name, boolean defaultSelected, Function<ItemModel, List<DataSeries>> generator, String tooltip) {
            JCheckBox priceGraph = new JCheckBox(name, defaultSelected);
            priceGraph.setToolTipText(tooltip);
            panel.add(priceGraph);

            priceGraph.addActionListener(e -> {
                if (priceGraph.isSelected()) {
                    remove = panel.plugin.addGraph(generator, name);
                } else {
                    if(remove != null) remove.run();
                    remove = null;
                }
            });
        }
    }
}

