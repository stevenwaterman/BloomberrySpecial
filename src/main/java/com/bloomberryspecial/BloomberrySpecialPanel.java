package com.bloomberryspecial;

import com.bloomberryspecial.transformers.TransformerType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

@Slf4j
@Singleton
class BloomberrySpecialPanel extends PluginPanel {
    private final JCheckBox priceCheckbox;
    private final JCheckBox volumeCheckbox;
    private final JCheckBox analysisCheckbox;
    private final JComboBox<TransformerType> transformerType;
    private final JComboBox<DataSelector> analysisBaseData;
    private final JSpinner movingAvgWindowSpinner;

    @Inject
    BloomberrySpecialPanel(BloomberrySpecialPlugin plugin, BloomberrySpecialConfig config) {
        setBorder(null);
        setBackground(Color.WHITE);
        setLayout(new DynamicGridLayout(10, 1));

        priceCheckbox = new JCheckBox("Price Graph", config.showPriceChart());
        priceCheckbox.addChangeListener(e -> plugin.setConfig("showPriceChart", priceCheckbox.isSelected()));

        volumeCheckbox = new JCheckBox("Volume Graph", config.showVolumeChart());
        volumeCheckbox.addChangeListener(e -> plugin.setConfig("showVolumeChart", volumeCheckbox.isSelected()));

        analysisCheckbox = new JCheckBox("Analysis Graph", config.showAnalysis());
        analysisCheckbox.addChangeListener(e -> plugin.setConfig("showAnalysisChart", analysisCheckbox.isSelected()));

        add(priceCheckbox);
        add(volumeCheckbox);
        add(analysisCheckbox);

        transformerType = new JComboBox<>(TransformerType.values());
        transformerType.setSelectedItem(config.transformerType());
        transformerType.addActionListener(e -> plugin.setConfig("transformerType", getTransformerType()));
        add(transformerType);

        add(new JLabel("Moving Average Base Data"));

        analysisBaseData = new JComboBox<>(DataSelector.values());
        analysisBaseData.setSelectedItem(config.movingAvgBaseData());
        analysisBaseData.addActionListener(e -> plugin.setConfig("movingAvgBaseData", getAnalysisSelector()));
        add(analysisBaseData);

        movingAvgWindowSpinner = new JSpinner(new SpinnerNumberModel(config.movingAvgWindow(), 1, null, 1));
        movingAvgWindowSpinner.addChangeListener(e -> plugin.setConfig("movingAvgWindow", movingAvgWindowSpinner.getValue()));
        add(movingAvgWindowSpinner);
    }

    public void updated() {

    }

    private DataSelector getAnalysisSelector() {
        return analysisBaseData.getModel().getElementAt(analysisBaseData.getSelectedIndex());
    }

    private TransformerType getTransformerType() {
        return transformerType.getModel().getElementAt(transformerType.getSelectedIndex());
    }
}

