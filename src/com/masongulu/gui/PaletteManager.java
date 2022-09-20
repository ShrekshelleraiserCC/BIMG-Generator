package com.masongulu.gui;

import com.masongulu.colors.Palette;
import com.masongulu.utils.KMeans;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PaletteManager extends JPanel {
    private final JComboBox<String> spinner = new JComboBox<>(new String[]{
            "Default",
            "K-Means",
            "KM (1st frame)"
    });
    private final StateMachine m;

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
        GridBagConstraints c = new GridBagConstraints();
        this.add(new JLabel("Palette"), c);
        spinner.setSelectedIndex(0);
        c.gridy = 1;
        this.add(spinner, c);
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
