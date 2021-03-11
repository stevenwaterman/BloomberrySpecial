package com.bloomberryspecial;

import com.bloomberryspecial.transformers.Transformer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class GraphEntity extends PanelComponent {
    private final ItemModel itemModel;
    private final BloomberrySpecialConfig config;
    private final Range yBounds;
    private final List<Integer> yMarks;
    private final List<DataSelector> dataSelectors;
    private final List<Transformer> analysisLines;
    private final String title;

    public GraphEntity(ItemModel itemModel, BloomberrySpecialConfig config, Range yBounds, List<Integer> yMarks, List<DataSelector> dataSelectors, List<Transformer> analysisLines, String title) {
        this.itemModel = itemModel;
        this.config = config;
        this.yBounds = yBounds;
        this.yMarks = yMarks;
        this.dataSelectors = dataSelectors;
        this.analysisLines = analysisLines;
        this.title = title;

        setPreferredSize(new Dimension(config.graphWidth(), config.graphHeight()));
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        log.info("Set preferred size {}", dimension);
        super.setPreferredSize(dimension);
    }

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
        graphics.fillRect(0, 0, super.getPreferredSize().width, super.getPreferredSize().height);

        // Border
        graphics.setColor(config.graphColor());
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawRect(0, 0, super.getPreferredSize().width, super.getPreferredSize().height);
        graphics.drawRect(config.marginLeft(), config.marginTop(), contentWidth(), contentHeight());

        // title
        FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
        graphics.drawString(title, super.getPreferredSize().width / 2 - metrics.stringWidth(title) / 2, 2 + metrics.getHeight());

        // x axis marks
        itemModel.getTimeMarks().forEach(mark -> {
            int x = getXLocation(itemModel.getTimeBounds().getFraction(mark));

            String label = Instant.ofEpochSecond(mark).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM HH:mm"));
            graphics.drawString(label, x - metrics.stringWidth(label) + 10, super.getPreferredSize().height - 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(x, contentEndBottom(), x, contentEndBottom() + config.markHeight());

            graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{5f}, 0f));
            graphics.drawLine(x, contentEndBottom(), x, config.marginTop());
        });

        // y axis marks
        drawYAxisMarks(graphics);

        // graph line
        dataSelectors.forEach(selector -> drawGraphLine(graphics, selector));
        analysisLines.forEach(transformer -> drawGraphLine(graphics, transformer.getData(itemModel), yBounds, config.analysisColor(), 2f, config.analysisDrawStyle()));

        return new Dimension(super.getPreferredSize().width, super.getPreferredSize().height);
    }

    private void drawYAxisMarks(Graphics2D graphics) {
        graphics.setColor(config.graphColor());
        yMarks.forEach(mark -> {
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

            int y = getYLocation(yBounds.getFraction(mark));
            graphics.drawString(label, config.marginLeft() - config.markWidth() - metrics.stringWidth(label) - 2, y + metrics.getHeight() / 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(config.marginLeft() - config.markWidth(), y, config.marginLeft(), y);

            graphics.setStroke(new BasicStroke(1f));
            graphics.drawLine(config.marginLeft(), y, contentEndRight(), y);
        });
    }

    private void drawGraphLine(Graphics2D graphics, DataSelector selector) {
        drawGraphLine(graphics, selector.getData(itemModel), yBounds, selector.getColor(config), 1f, config.drawStyle());
    }

    private void drawGraphLine(Graphics2D graphics, java.util.List<Pair<Integer, Integer>> data, Range bounds, Color color, float strokeWidth, DrawStyle drawStyle) {
        graphics.setStroke(new BasicStroke(strokeWidth));

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
        return super.getPreferredSize().width - config.marginLeft() - config.marginRight();
    }

    private int contentHeight() {
        return super.getPreferredSize().height - config.marginTop() - config.marginBottom();
    }

    private int contentEndRight() {
        return super.getPreferredSize().width - config.marginRight();
    }

    private int contentEndBottom() {
        return super.getPreferredSize().height - config.marginBottom();
    }

    private int getXLocation(double graphFraction) {
        return (int) (config.marginLeft() + graphFraction * contentWidth());
    }

    private int getYLocation(double graphFraction) {
        return (int) (contentEndBottom() - graphFraction * contentHeight());
    }
}
