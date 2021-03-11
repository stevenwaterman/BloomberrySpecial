package com.bloomberryspecial;

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
public class BloomberrySpecialAnalysisOverlay extends OverlayPanel {
    private final Client client;
    private BloomberrySpecialPlugin plugin;
    private BloomberrySpecialPanel bloomberrySpecialPanel;

    @Inject
    public BloomberrySpecialAnalysisOverlay(Client client, BloomberrySpecialPlugin plugin, BloomberrySpecialPanel bloomberrySpecialPanel) {
        this.client = client;
        this.plugin  = plugin;
        this.bloomberrySpecialPanel = bloomberrySpecialPanel;
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

        java.util.List<Integer> marks;
        Range bounds;
        if (bloomberrySpecialPanel.isAnalysingPrices()) {
            marks = itemModel.getPriceMarks();
            bounds = itemModel.getPriceBounds();
        } else {
            marks = itemModel.getVolumeMarks();
            bounds = itemModel.getVolumeBounds();
        }
        graph = new GraphEntity(itemModel, plugin.getConfig(), bounds, marks, bloomberrySpecialPanel.getAnalysisSelectors(), bloomberrySpecialPanel.getAnalysisTransformers(), "Analysis");
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph != null && bloomberrySpecialPanel.isAnalysisGraphEnabled()) panelComponent.getChildren().add(graph);
        return super.render(graphics);
    }
}

