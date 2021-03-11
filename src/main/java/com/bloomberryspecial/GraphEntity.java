package com.bloomberryspecial;

import com.bloomberryspecial.transformers.Transformer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class GraphEntity extends PanelComponent {
    private final ItemModel itemModel;
    private final BloomberrySpecialConfig config;
    private final List<DataSelector> dataSelectors;
    private final List<Transformer> transformers;
    private final String title;

    public GraphEntity(ItemModel itemModel, BloomberrySpecialConfig config, List<DataSelector> dataSelectors, List<Transformer> transformers, String title, Dimension preferredSize) {
        this.itemModel = itemModel;
        this.config = config;
        this.dataSelectors = dataSelectors;
        this.transformers = transformers;
        this.title = title;

        setPreferredSize(preferredSize);
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

        // Calculate bounds & marks
        Range bounds = Stream.concat(
                dataSelectors.stream().flatMap(selector -> selector.getData(itemModel).stream()),
                transformers.stream().flatMap(transformer -> transformer.getData(itemModel).stream())
        ).reduce(initialPair, GraphEntity::applyRange, GraphEntity::combineRange);
        List<Integer> xMarks = createMarks(bounds.getMinX(), bounds.getMaxX(), contentWidth() / 100);
        List<Integer> yMarks = createMarks(bounds.getMinY(), bounds.getMaxY(), contentHeight() / 30);

        // x axis marks
        xMarks.forEach(mark -> {
            int x = getXLocation(bounds.getFractionX(mark));

            String label = Instant.ofEpochSecond(mark).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMM HH:mm"));
            graphics.drawString(label, x - metrics.stringWidth(label) + 10, super.getPreferredSize().height - 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(x, contentEndBottom(), x, contentEndBottom() + config.markHeight());

            graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{5f}, 0f));
            graphics.drawLine(x, contentEndBottom(), x, config.marginTop());
        });

        // y axis marks
        drawYAxisMarks(graphics, yMarks, bounds);

        // graph line
        dataSelectors.forEach(selector -> drawGraphLine(graphics, selector, bounds));
        transformers.forEach(transformer -> drawGraphLine(graphics, transformer.getData(itemModel), bounds, config.analysisColor(), 2f, config.analysisDrawStyle()));

        return new Dimension(super.getPreferredSize().width, super.getPreferredSize().height);
    }

    private void drawYAxisMarks(Graphics2D graphics, List<Integer> marks, Range bounds) {
        graphics.setColor(config.graphColor());
        marks.forEach(mark -> {
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

            int y = getYLocation(bounds.getFractionY(mark));
            graphics.drawString(label, config.marginLeft() - config.markWidth() - metrics.stringWidth(label) - 2, y + metrics.getHeight() / 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(config.marginLeft() - config.markWidth(), y, config.marginLeft(), y);

            graphics.setStroke(new BasicStroke(1f));
            graphics.drawLine(config.marginLeft(), y, contentEndRight(), y);
        });
    }

    private void drawGraphLine(Graphics2D graphics, DataSelector selector, Range bounds) {
        drawGraphLine(graphics, selector.getData(itemModel), bounds, selector.getColor(config), 1f, config.drawStyle());
    }

    private void drawGraphLine(Graphics2D graphics, java.util.List<Pair<Integer, Double>> data, Range bounds, Color color, float strokeWidth, DrawStyle drawStyle) {
        graphics.setStroke(new BasicStroke(strokeWidth));

        Integer lastX = null;
        Integer lastY = null;

        for (Pair<Integer, Double> point : data) {
            int x = getXLocation(bounds.getFractionX(point.getLeft()));
            int y = getYLocation(bounds.getFractionY(point.getRight()));

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

    private static final Range initialPair = new Range(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);

    private static Range combineRange(Range pair1, Range pair2) {
        return new Range(
                Math.min(pair1.getMinX(), pair2.getMinX()),
                Math.max(pair1.getMaxX(), pair2.getMaxX()),
                Math.min(pair1.getMinY(), pair2.getMinY()),
                Math.max(pair1.getMaxY(), pair2.getMaxY()));
    }

    private static Range applyRange(Range acc, Pair<Integer, Double> elem) {
        return new Range(
                Math.min(acc.getMinX(), elem.getLeft()),
                Math.max(acc.getMaxX(), elem.getLeft()),
                (int) Math.floor(Math.min(acc.getMinY(), elem.getRight())),
                (int) Math.ceil(Math.max(acc.getMaxY(), elem.getRight())));
    }

    private static List<Integer> createMarks(int min, int max, int desiredMarkCount) {
        if (min == max) return Collections.singletonList(min);

        int range = max - min; // 3500
        double desiredRangePerMark = (double) range / desiredMarkCount; // 350.0

        double rangeMagnitude = Math.round(Math.pow(10, Math.floor(Math.log10(desiredRangePerMark)))); // 100.0
        double rangeMagnitudeMultiple = desiredRangePerMark / rangeMagnitude ; // 3.5
        long adjustedMultiple = Math.round(rangeMagnitudeMultiple); // 4
        long rangePerMark = Math.max(1, Math.round(rangeMagnitude * adjustedMultiple)); // 400

        double markStart = Math.ceil((double) min / rangePerMark) * rangePerMark; // 6800.0
        List<Integer> marks = new ArrayList<>();
        for (int i = (int) markStart; i <= max; i += rangePerMark) {
            marks.add(i); // 6800, 7200, ..., 9600, 10000
        }
        return marks;
    }
}
