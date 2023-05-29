package com.masongulu.gui;

import com.masongulu.filters.FilterNone;
import com.masongulu.filters.FilterScale;
import com.masongulu.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class FilterManager extends JPanel {
    public final ArrayList<FilterOption> filters = new ArrayList<>();
    private final StateMachine m;

    public FilterManager(StateMachine m) {
        super(new GridBagLayout());
        this.m = m;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JLabel("Filters"), gbc);
        gbc.gridy = 1;
        JButton addFilter = new JButton("Add Filter");
        this.add(addFilter, gbc);
        JPanel panel = new JPanel();
        gbc.gridy = 2;
        this.add(panel, gbc);
        addFilter.addActionListener(actionEvent -> {
            FilterOption filter = new FilterOption();
            filters.add(filter);
            panel.add(filter);
            panel.revalidate();
            this.revalidate();
        });
    }

    public BufferedImage[] filter(BufferedImage[] images) {
        BufferedImage[] postFilter = new BufferedImage[images.length];
        for (int i = 0; i < postFilter.length; i++) {
            postFilter[i] = Utils.copyImage(images[i]);
        }
        for (int i = 0; i < postFilter.length; i++) {
            for (FilterOption filter : filters) {
                // apply each filter
                postFilter[i] = filter.getFilter().process(postFilter[i], m);
            }
        }
        return postFilter;
    }
}

class FilterOption extends JPanel {
    private final JComboBox<FilterNone> comboBox;
    private JPanel lastSelected;

    FilterOption() {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        comboBox = new JComboBox<>(getFilters());
        comboBox.setSelectedIndex(0);
        this.add(comboBox, gbc);
        gbc.gridy = 1;
        JButton removeButton = new JButton("Remove");
        this.add(removeButton, gbc);
        removeButton.addActionListener(actionEvent -> {
            Container parent = this.getParent(); // this parent is panel
            // so the parent of that panel should be the FilterManager class
            FilterManager parentParent = (FilterManager) parent.getParent();
            parentParent.filters.remove(this);
            parent.remove(this); // remove itself from the parent object
            parent.revalidate();
            parent.getParent().revalidate();
        });
        gbc.gridy = 2;
        lastSelected = ((FilterNone) Objects.requireNonNull(comboBox.getSelectedItem())).getPanel();
        comboBox.addActionListener(actionEvent -> {
            if (lastSelected != null)
                this.remove(lastSelected);
            lastSelected = ((FilterNone) Objects.requireNonNull(comboBox.getSelectedItem())).getPanel();
            if (lastSelected != null) {
                this.add(lastSelected, gbc);
            }
            this.revalidate();
        });
    }

    private FilterNone[] getFilters() {
        return new FilterNone[]{
                new FilterNone(),
                new FilterScale()
        };
    }

    public FilterNone getFilter() {
        return (FilterNone) Objects.requireNonNull(comboBox.getSelectedItem());
    }
}
