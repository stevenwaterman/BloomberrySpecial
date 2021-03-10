package com.bloomberryspecial;

import com.bloomberryspecial.transformers.MovingAvg;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.ClientTick;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class GraphEntity implements LayoutableRenderableEntity {
    private final ItemModel itemModel;
    private final BloomberrySpecialConfig config;
    private final MovingAvg avgTransformer;

    public GraphEntity(ItemModel itemModel, BloomberrySpecialConfig config) {
        this.itemModel = itemModel;
        this.config = config;
        this.avgTransformer = new MovingAvg(DataSelector.BUY_PRICE.getData(itemModel), 25);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(config.graphWidth(), config.graphHeight());
    }

    @Override
    public void setPreferredLocation(Point position) {}

    @Override
    public void setPreferredSize(Dimension dimension) {}

    @Override
    public Dimension render(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color backgroundColor = new Color(
                config.backgroundColor().getRed(),
                config.backgroundColor().getGreen(),
                config.backgroundColor().getBlue(),
                (int) ((1 - (double) config.backgroundTransparency() / 100) * 255)
        );

        // Background
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, config.graphWidth(), config.graphHeight());

        // Border
        graphics.setColor(config.graphColor());
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawRect(0, 0, config.graphWidth(), config.graphHeight());
        graphics.drawRect(config.marginLeft(), config.marginRight(), contentWidth(), contentHeight());

        // x axis marks
        itemModel.getTimeMarks().forEach(mark -> {
            int x = getXLocation(itemModel.getTimeBounds().getFraction(mark));

            FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
            String label = Instant.ofEpochSecond(mark).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM HH:mm"));
            graphics.drawString(label, x - metrics.stringWidth(label) + 10, config.graphHeight() - 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(x, contentEndBottom(), x, contentEndBottom() + config.markHeight());

            graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{5f}, 0f));
            graphics.drawLine(x, contentEndBottom(), x, config.marginTop());
        });

        // y axis marks
        drawYAxisMarks(graphics, DataSelector.BUY_PRICE);

        // graph line
        graphics.setStroke(new BasicStroke(1f));
        drawGraphLine(graphics, DataSelector.BUY_PRICE);
        drawGraphLine(graphics, DataSelector.SELL_PRICE);

        graphics.setStroke(new BasicStroke(2f));
        drawGraphLine(graphics, avgTransformer.getData(), DataSelector.BUY_PRICE.getBounds(itemModel), config.analysisColor(), config.analysisDrawStyle());

        return new Dimension(config.graphWidth(), config.graphHeight());
    }

    private void drawYAxisMarks(Graphics2D graphics, DataSelector selector) {
        graphics.setColor(config.graphColor());
        selector.getMarks(itemModel).forEach(mark -> {
            FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
            String label = mark.toString();
            if (mark > 1E9) {
                int millions = mark / 1000000;
                double billions = (double) millions / 1000;
                label = billions + "b";
            } else if (mark > 1E6) {
                int thousands = mark / 1000;
                double millions = (double) thousands / 1000;
                label = millions + "m";
            } else if (mark > 1E3) {
                double thousands = (double) mark / 1000;
                label = thousands + "k";
            }

            int y = getYLocation(selector.getBounds(itemModel).getFraction(mark));
            graphics.drawString(label, config.marginLeft() - config.markWidth() - metrics.stringWidth(label) - 2, y + metrics.getHeight() / 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(config.marginLeft() - config.markWidth(), y, config.marginLeft(), y);

            graphics.setStroke(new BasicStroke(1f));
            graphics.drawLine(config.marginLeft(), y, contentEndRight(), y);
        });
    }

    private void drawGraphLine(Graphics2D graphics, DataSelector selector) {
        drawGraphLine(graphics, selector.getData(itemModel), selector.getBounds(itemModel), selector.getColor(config), config.drawStyle());
    }

    private void drawGraphLine(Graphics2D graphics, java.util.List<Pair<Integer, Integer>> data, ItemModel.Range bounds, Color color, DrawStyle drawStyle) {
        Integer lastX = null;
        Integer lastY = null;

        for (Pair<Integer, Integer> point : data) {
            int x = getXLocation(itemModel.getTimeBounds().getFraction(point.getLeft()));
            int y = getYLocation(bounds.getFraction(point.getRight()));

            graphics.setColor(color);
            if (drawStyle.isPointsEnabled()) graphics.fillOval(x - config.pointSize() / 2, y - config.pointSize() / 2, config.pointSize(), config.pointSize());
            if (drawStyle.isLinesEnabled() && lastX != null) graphics.drawLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;
        }
    }

    private int contentWidth() {
        return config.graphWidth() - config.marginLeft() - config.marginRight();
    }

    private int contentHeight() {
        return config.graphHeight() - config.marginTop() - config.marginBottom();
    }

    private int contentEndRight() {
        return config.graphWidth() - config.marginRight();
    }

    private int contentEndBottom() {
        return config.graphHeight() - config.marginBottom();
    }

    private int getXLocation(double graphFraction) {
        return (int) (config.marginLeft() + graphFraction * contentWidth());
    }

    private int getYLocation(double graphFraction) {
        return (int) (contentEndBottom() - graphFraction * contentHeight());
    }
}
