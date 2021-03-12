package com.bloomberryspecial;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GraphEntity extends PanelComponent {
    private final BloomberrySpecialConfig config;
    private final List<DataSeries> lines;
    private final String title;

    public GraphEntity(BloomberrySpecialConfig config, List<DataSeries> lines, String title, Dimension preferredSize) {
        this.config = config;
        this.lines = lines;
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
        graphics.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);

        // Border
        graphics.setColor(config.graphColor());
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawRect(0, 0, getPreferredSize().width, getPreferredSize().height);
        graphics.drawRect(config.marginLeft(), config.marginTop(), contentWidth(), contentHeight());

        // title
        FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
        graphics.drawString(title, getPreferredSize().width / 2 - metrics.stringWidth(title) / 2, 2 + metrics.getHeight());

        // Calculate bounds & marks
        Range bounds = lines.stream()
                .flatMap(line -> line.getData().stream())
                .reduce(Range.SMALLEST, Range::withDataPoint, Range::withRange);
        List<Integer> xMarks = createMarks(bounds.getMinX(), bounds.getMaxX(), contentWidth() / 100);
        List<Integer> yMarks = createMarks(bounds.getMinY(), bounds.getMaxY(), contentHeight() / 30);

        // x axis marks
        xMarks.forEach(mark -> {
            int x = getXLocation(bounds.getFractionX(mark));

            String pattern = "HH:mm";
            if (bounds.getRangeX() >= 365 * 24 * 3600) pattern = "yy " + pattern;
            if (bounds.getRangeX() >= 28 * 24 * 3600) pattern = "MMM " + pattern;
            if (bounds.getRangeX() > 24 * 3600) pattern = "d " + pattern;
            String label = Instant.ofEpochSecond(mark).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
            graphics.drawString(label, x - metrics.stringWidth(label) + 10, getPreferredSize().height - 2);

            graphics.setStroke(new BasicStroke(2f));
            graphics.drawLine(x, contentEndBottom(), x, contentEndBottom() + config.markHeight());

            graphics.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{5f}, 0f));
            graphics.drawLine(x, contentEndBottom(), x, config.marginTop());
        });

        // y axis marks
        drawYAxisMarks(graphics, yMarks, bounds);

        // graph line
        lines.forEach(line -> drawGraphLine(graphics, line, bounds));

        return new Dimension(getPreferredSize().width, getPreferredSize().height);
    }

    private void drawYAxisMarks(Graphics2D graphics, List<Integer> marks, Range bounds) {
        graphics.setColor(config.graphColor());
        marks.forEach(mark -> {
            FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
            String label = mark.toString();
            if (Math.abs(mark) > 1E9) {
                int millions = mark / 1000000;
                double billions = (double) millions / 1000;
                label = billions + "b";
            } else if (Math.abs(mark) > 1E6) {
                int thousands = mark / 1000;
                double millions = (double) thousands / 1000;
                label = millions + "m";
            } else if (Math.abs(mark) > 1E4) {
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

    private void drawGraphLine(Graphics2D graphics, DataSeries line, Range bounds) {
        graphics.setStroke(line.getStroke());
        graphics.setColor(line.getColor());

        Integer lastX = null;
        Integer lastY = null;

        for (DataPoint point : line.getData()) {
            int x = getXLocation(bounds.getFractionX(point.getX()));
            int y = getYLocation(bounds.getFractionY(point.getY()));

            if (line.getDrawStyle().isPointsEnabled()) graphics.fillOval(x - config.pointSize() / 2, y - config.pointSize() / 2, config.pointSize(), config.pointSize());
            if (line.getDrawStyle().isLinesEnabled() && lastX != null) graphics.drawLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;
        }
    }

    private int contentWidth() {
        return getPreferredSize().width - config.marginLeft() - config.marginRight();
    }

    private int contentHeight() {
        return getPreferredSize().height - config.marginTop() - config.marginBottom();
    }

    private int contentEndRight() {
        return getPreferredSize().width - config.marginRight();
    }

    private int contentEndBottom() {
        return getPreferredSize().height - config.marginBottom();
    }

    private int getXLocation(double graphFraction) {
        return (int) (config.marginLeft() + graphFraction * contentWidth());
    }

    private int getYLocation(double graphFraction) {
        return (int) (contentEndBottom() - graphFraction * contentHeight());
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
