package com.bloomberryspecial;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;
import java.util.Collections;

@Singleton
@Slf4j
public class BloomberrySpecialVolumeOverlay extends OverlayPanel {
    private final BloomberrySpecialPlugin plugin;
    private final BloomberrySpecialConfig config;

    @Inject
    public BloomberrySpecialVolumeOverlay(BloomberrySpecialPlugin plugin, BloomberrySpecialConfig config) {
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
            plugin.setConfig("volumeGraphWidth", preferredSize.width);
            plugin.setConfig("volumeGraphHeight", preferredSize.height);
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

        graph = new GraphEntity(itemModel, plugin.getConfig(), Lists.newArrayList(DataSelector.BUY_VOLUME, DataSelector.SELL_VOLUME), Collections.emptyList(), "Volume", new Dimension(config.volumeGraphWidth(), config.volumeGraphHeight()));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph != null && config.showVolumeChart()) panelComponent.getChildren().add(graph);
        return super.render(graphics);
    }
}
