package com.masongulu.gui;

import com.masongulu.colors.Color;
import com.masongulu.colors.Palette;
import com.masongulu.utils.KMeans;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class PaletteManager extends JPanel {
    private final JComboBox<String> spinner = new JComboBox<>(new String[]{
            "Default",
            "K-Means",
            "KM (1st frame)",
            "Custom"
    });
    private final TextField customInput = new TextField();
    private final StateMachine m;
    private Color[] customPalette;

    public PaletteManager(StateMachine m) {
        super(new GridBagLayout());
        this.m = m;
        m.supportsCustomPalette.addListener((oldValue, newValue) -> {
            if (newValue) {
                // enable
                spinner.setEnabled(true);
            } else {
                // disable
                spinner.setSelectedIndex(0);
                spinner.setEnabled(false);
            }
        });
        spinner.addActionListener(actionEvent -> {
            customInput.setEnabled(spinner.getSelectedIndex() == 3);
        });
        GridBagConstraints c = new GridBagConstraints();
        this.add(new JLabel("Palette"), c);
        spinner.setSelectedIndex(0);
        c.gridy = 1;
        this.add(spinner, c);
        c.gridy = 2;
        customInput.setPreferredSize(new Dimension(120, 25));
        customInput.setSize(new Dimension(120, 25));
        customInput.setEnabled(false);
        this.add(customInput, c);
    }

    public Palette[] getPalettes(BufferedImage[] images) {
        String selection = (String) spinner.getSelectedItem();
        Palette[] palettes = new Palette[images.length];
        boolean copyFirst = false;
        if (("Default").equals(selection)) {
            copyFirst = true;
            palettes[0] = Palette.defaultPalette;
        } else if (("K-Means").equals(selection)) {
            for (int i = 0; i < images.length; i++) {
                palettes[i] = new Palette(KMeans.applyKMeans(images[i], 16));
            }
        } else if (("Custom").equals(selection)) {
            // load from input box
            copyFirst = true;
            try {
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(customInput.getText());
                Color[] colors = new Color[16];
                Iterator itr = jsonArray.iterator();
                for (int i = 0; i < 16; i++) {
                    if (itr.hasNext()) {
                        int color = ((Long) itr.next()).intValue();
                        colors[i] = new Color(color);
                    } else {
                        colors[i] = new Color(0);
                    }
                }
                palettes[0] = new Palette(colors);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            copyFirst = true;
            palettes[0] = new Palette(KMeans.applyKMeans(images[0], 16));
        }
        if (copyFirst) {
            for (int i = 1; i < images.length; i++) {
                palettes[i] = palettes[0];
            }
        }
        return palettes;
    }
}
