package com.bloomberryspecial;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import java.awt.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class BloomberrySpecialOverlay extends OverlayPanel {
    private final Client client;
    private BloomberrySpecialPlugin plugin;
//
//    private int borderThickness = 2;
//    private int divisionLineThickness = 1;
//
//    private Color textColor = Color.WHITE;
//    private Color graphLineColor;
//    private Color backgroundColor;
//    private int backgroundTransparency;
//
//    private int marginGraphLeft = 43;
//    private int marginGraphTop = 11;
//    private int marginGraphRight = 24;
//    private int marginGraphBottom = 30;
//    private int marginTimeLabelTop = 7;
//
//    private int marginOverlayRight = 5;
//
//    //private int marginStartMessageTop = 12;
//    private int marginStartMessageBottom = 8;
//
//    private int marginLegendRight = 12;
//    private int marginLegendTop = 8;
//    private int marginLegendLeft = 10;
//    private int marginLegendBoxBottom = 2;
//    private int marginLegendBoxRight = 4;
//    private int marginLegendBoxLeft = 4;
//    private int marginLegendBoxTop = 2;
//    private int marginLegendBottom = 3;
//    private int legendBoxSize = 10;
//
//    private int legendWidth = 93;
//    private int legendHeight = 20;
//
//    private int marginVertAxisValueRight = 4;
//
//    private int bottomAxisTickMarkLength = 6;

    @Inject
    private BloomberrySpecialOverlay(Client client, BloomberrySpecialPlugin plugin) {
        this.client = client;
        this.plugin  = plugin;
//        graphLineColor = grapherPlugin.config.graphColor();
//        backgroundColor = grapherPlugin.config.graphBackgroundColor();
//        backgroundTransparency = grapherPlugin.config.graphBackgroundTransparency();
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPosition(OverlayPosition.TOP_CENTER);
    }

    private GraphEntity graphEntity = null;
    public void updated() {
        final ItemModel itemModel = plugin.getItemModel();
        if (itemModel == null) {
            log.info("Deleting Graph entity");
            graphEntity = null;
            return;
        }

        log.info("Creating Graph entity");
        graphEntity = new GraphEntity(itemModel);
    }

//    private int convertTransparency(int configTransparency) {
//        return (int)(255*((double)configTransparency/(double)100));
//    }
//
//    private String formatTime(long timePassed) {
//        int secondsPassed = (int)timePassed/1000;
//        int hours = secondsPassed/(60*60);
//        int secondsLeft = secondsPassed%(60*60);
//        int minutes = secondsLeft/60;
//        int seconds = secondsLeft%60;
//
//        String result = "";
//        if (hours > 0)
//            result += hours + ":";
//        if (minutes > 0 || hours > 0) {
//            if (minutes < 10 && hours > 0)
//                result += "0";
//            result += minutes + ":";
//        }
//        if (seconds < 10 && (minutes > 0|| hours > 0))
//            result += "0";
//        result += seconds;
//        return result;
//    }
//
//    private String formatXpString(int xpToFormat) {
//
//        String result;
//
//        if (xpToFormat < 1000)
//            result = Integer.toString(xpToFormat);
//        else if (xpToFormat < 1000000) {
//
//            int xpInK = xpToFormat/1000;
//            int decimalPart = xpToFormat%1000;
//            //System.out.println(xpToFormat + ", " + xpInK + ", " + decimalPart);
//            result = Integer.toString(xpInK);
//            if (decimalPart > 0) {
//                result = result + "." + decimalPart;
//                while (result.charAt(result.length()-1) == '0')
//                    result = result.substring(0, result.length()-1);
//            }
//
//
//            result = result + "K";
//
//        }
//        else {
//            int xpInM = xpToFormat/1000000;
//            int decimalPart = xpToFormat%1000000;
//            //System.out.println(xpToFormat + ", " + xpInK + ", " + decimalPart);
//            result = Integer.toString(xpInM);
//            if (decimalPart > 0) {
//                result = result + "." + decimalPart;
//                while (result.charAt(result.length()-1) == '0')
//                    result = result.substring(0, result.length()-1);
//            }
//
//
//            result = result + "M";
//        }
//
//        return result;
//    }
//
//    public void printFormattedXp() {
//        for (int i = 0; i < grapherPlugin.xpGraphPointManager.xpGraphMaxValues.length; i++) {
//            System.out.println(formatXpString(grapherPlugin.xpGraphPointManager.xpGraphMaxValues[i]));
//        }
//    }
//
//

//    private LayoutableRenderableEntity graphEntity = new GraphEntity();

    @Override
    public Dimension render(Graphics2D graphics) {
        if(graphEntity != null) {
            panelComponent.getChildren().add(graphEntity);
            panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));
            log.info("Adding children");
        }

        log.info("Rendering overlay");
        return super.render(graphics);
    }
}

