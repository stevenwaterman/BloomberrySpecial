package com.bloomberryspecial;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

@Slf4j
class BloomberrySpecialPanel extends PluginPanel {
//    private static final Color ODD_ROW = new Color(44, 44, 44);
//
//    private final JPanel listContainer = new JPanel();
//
//    private BankValueTableHeader countHeader;
//    private BankValueTableHeader valueHeader;
//    private BankValueTableHeader nameHeader;
//
//    private SortOrder orderIndex = SortOrder.VALUE;
//    private boolean ascendingOrder = false;
//
//    private ArrayList<BankValueTableRow> rows = new ArrayList<>();
    private BloomberrySpecialPlugin plugin;

    @Inject
    BloomberrySpecialPanel(BloomberrySpecialPlugin plugin) {
        this.plugin = plugin;
    }

    public void updated() {

    }

//        setBorder(null);
//        setLayout(new DynamicGridLayout(0, 1));
//
//        JPanel headerContainer = buildHeader();
//
//        listContainer.setLayout(new GridLayout(0, 1));
//
//        add(headerContainer);
//        add(listContainer);
//    }

//    void updateList() {
//        rows.sort((r1, r2) ->
//        {
//            switch (orderIndex) {
//                case NAME:
//                    return r1.getItemName().compareTo(r2.getItemName()) * (ascendingOrder ? 1 : -1);
//                case COUNT:
//                    return Integer.compare(r1.getItemCount(), r2.getItemCount()) * (ascendingOrder ? 1 : -1);
//                case VALUE:
//                    return Integer.compare(r1.getPrice(), r2.getPrice()) * (ascendingOrder ? 1 : -1);
//                default:
//                    return 0;
//            }
//        });
//
//        listContainer.removeAll();
//
//        for (int i = 0; i < rows.size(); i++) {
//            BankValueTableRow row = rows.get(i);
//            row.setBackground(i % 2 == 0 ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
//            listContainer.add(row);
//        }
//
//        listContainer.revalidate();
//        listContainer.repaint();
//    }
//
//    void populate(List<CachedItem> items) {
//        rows.clear();
//
//        for (int i = 0; i < items.size(); i++) {
//            CachedItem item = items.get(i);
//
//            rows.add(buildRow(item, i % 2 == 0));
//        }
//
//        updateList();
//    }
//
//    private void orderBy(SortOrder order) {
//        nameHeader.highlight(false, ascendingOrder);
//        countHeader.highlight(false, ascendingOrder);
//        valueHeader.highlight(false, ascendingOrder);
//
//        switch (order) {
//            case NAME:
//                nameHeader.highlight(true, ascendingOrder);
//                break;
//            case COUNT:
//                countHeader.highlight(true, ascendingOrder);
//                break;
//            case VALUE:
//                valueHeader.highlight(true, ascendingOrder);
//                break;
//        }
//
//        orderIndex = order;
//        updateList();
//    }
//
//    /**
//     * Builds the entire table header.
//     */
//    private JPanel buildHeader() {
//        JPanel header = new JPanel(new BorderLayout());
//        JPanel leftSide = new JPanel(new BorderLayout());
//        JPanel rightSide = new JPanel(new BorderLayout());
//
//        nameHeader = new BankValueTableHeader("Name", orderIndex == SortOrder.NAME, ascendingOrder);
//        nameHeader.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent mouseEvent) {
//                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
//                    return;
//                }
//                ascendingOrder = orderIndex != SortOrder.NAME || !ascendingOrder;
//                orderBy(SortOrder.NAME);
//            }
//        });
//
//        countHeader = new BankValueTableHeader("#", orderIndex == SortOrder.COUNT, ascendingOrder);
//        countHeader.setPreferredSize(new Dimension(BankValueTableRow.ITEM_COUNT_COLUMN_WIDTH, 0));
//        countHeader.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent mouseEvent) {
//                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
//                    return;
//                }
//                ascendingOrder = orderIndex != SortOrder.COUNT || !ascendingOrder;
//                orderBy(SortOrder.COUNT);
//            }
//        });
//
//        valueHeader = new BankValueTableHeader("$", orderIndex == SortOrder.VALUE, ascendingOrder);
//        valueHeader.setPreferredSize(new Dimension(BankValueTableRow.ITEM_VALUE_COLUMN_WIDTH, 0));
//        valueHeader.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent mouseEvent) {
//                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
//                    return;
//                }
//                ascendingOrder = orderIndex != SortOrder.VALUE || !ascendingOrder;
//                orderBy(SortOrder.VALUE);
//            }
//        });
//
//
//        leftSide.add(nameHeader, BorderLayout.CENTER);
//        leftSide.add(countHeader, BorderLayout.EAST);
//        rightSide.add(valueHeader, BorderLayout.CENTER);
//
//        header.add(leftSide, BorderLayout.CENTER);
//        header.add(rightSide, BorderLayout.EAST);
//
//        return header;
//    }
//
//    /**
//     * Builds a table row, that displays the bank's information.
//     */
//    private BankValueTableRow buildRow(CachedItem item, boolean stripe) {
//        BankValueTableRow row = new BankValueTableRow(item);
//        row.setBackground(stripe ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
//        return row;
//    }
}

