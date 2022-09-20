package com.masongulu.gui;

import com.masongulu.colors.Palette;
import com.masongulu.colors.PaletteImage;
import com.masongulu.quantizers.QuantizeBlueNoise;
import com.masongulu.quantizers.QuantizeFloydSteinberg;
import com.masongulu.quantizers.QuantizeNone;
import com.masongulu.quantizers.QuantizeOrdered;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class QuantizeManager extends JPanel {
    private final JComboBox<QuantizeNone> spinner = new JComboBox<>(new QuantizeNone[]{
            new QuantizeNone(),
            new QuantizeOrdered(),
            new QuantizeBlueNoise(),
            new QuantizeFloydSteinberg()
    });
    private final StateMachine m;
    private JPanel lastSelected;

    public QuantizeManager(StateMachine m) {
        super(new GridBagLayout());
        this.m = m;
        GridBagConstraints c = new GridBagConstraints();
        spinner.setSelectedIndex(0);
        lastSelected = ((QuantizeNone) Objects.requireNonNull(spinner.getSelectedItem())).getPanel();
        this.add(new JLabel("Dither"));
        c.gridy = 1;
        this.add(spinner, c);
        c.gridy = 2;
        spinner.addActionListener(actionEvent -> {
            if (lastSelected != null)
                this.remove(lastSelected);
            lastSelected = ((QuantizeNone) Objects.requireNonNull(spinner.getSelectedItem())).getPanel();
            if (lastSelected != null) {
                this.add(lastSelected, c);
            }
            this.revalidate();
        });
    }

    public PaletteImage[] getImage(BufferedImage[] images, Palette[] palettes) {
        PaletteImage[] out = new PaletteImage[images.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((QuantizeNone) Objects.requireNonNull(spinner.getSelectedItem())).quantize(images[i], palettes[i]);
        }
        return out;
    }
}
