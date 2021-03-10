package com.bloomberryspecial;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.*;

@Slf4j
public class BloomberrySpecialOverlay extends OverlayPanel {
    private final Client client;
    private BloomberrySpecialPlugin plugin;

    @Inject
    private BloomberrySpecialOverlay(Client client, BloomberrySpecialPlugin plugin) {
        this.client = client;
        this.plugin  = plugin;
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setBorder(new Rectangle(0, 0, 0, 0));
        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));
    }

    private GraphEntity graph = null;
    public void updated() {
        final ItemModel itemModel = plugin.getItemModel();
        if (itemModel == null) {
            log.info("Deleting Graph entity");
            graph = null;
            return;
        }

        log.info("Creating Graph entity");
        graph = new GraphEntity(itemModel, plugin.getConfig());
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (graph != null) panelComponent.getChildren().add(graph);
        return super.render(graphics);
    }
}

