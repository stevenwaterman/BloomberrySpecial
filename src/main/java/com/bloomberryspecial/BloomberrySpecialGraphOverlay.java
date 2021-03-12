package com.bloomberryspecial;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.util.List;
import java.awt.*;
import java.util.function.Function;

@Singleton
@Slf4j
public class BloomberrySpecialGraphOverlay extends Overlay {
    private final BloomberrySpecialPlugin plugin;
    private final BloomberrySpecialConfig config;
    private final Function<ItemModel, List<DataSeries>> generator;
    private final String name;

    @Inject
    public BloomberrySpecialGraphOverlay(BloomberrySpecialPlugin plugin, BloomberrySpecialConfig config, Function<ItemModel, List<DataSeries>> generator, String name) {
        this.plugin  = plugin;
        this.config = config;
        this.generator = generator;
        this.name = name;

        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.TOP_LEFT);
        setPreferredSize(new Dimension(100, 100));
        setResizable(true);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        if (graph != null) graph.setPreferredSize(preferredSize);
    }

    private GraphEntity graph = null;
    public void updated() {
        final ItemModel itemModel = plugin.getItemModel();
        if (itemModel == null) {
            graph = null;
            return;
        }
        List<DataSeries> series = generator.apply(itemModel);
        graph = new GraphEntity(config, series, name, getPreferredSize());
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph == null) return null;
        return graph.render(graphics);
    }
}
