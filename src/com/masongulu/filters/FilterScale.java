package com.masongulu.filters;

import com.masongulu.gui.StateMachine;
import com.masongulu.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FilterScale extends FilterNone {
    private final static String[] unitChoices = {
            "Characters",
            "Monitors",
            "Pixels",
    };
    private final JTextField horizontal = new JTextField("51", 3);
    private final JTextField vertical = new JTextField("19", 3);
    private final JComboBox<String> units = new JComboBox<>(unitChoices);

    public FilterScale() {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        panel.add(new JLabel("Fit to scale"), c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("x"), c);
        c.gridx = 1;
        panel.add(horizontal, c);
        c.gridx = 2;
        panel.add(new JLabel("y"), c);
        c.gridx = 3;
        panel.add(vertical, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        panel.add(units, c);
    }

    @Override
    public BufferedImage process(BufferedImage image, StateMachine m) {
        boolean isHD = m.usingCharacters.get();
        String selection = unitChoices[units.getSelectedIndex()];
        try {
            int xRes = Integer.parseInt(horizontal.getText());
            int yRes = Integer.parseInt(vertical.getText());
            boolean characters = selection.equals("Characters");
            if (selection.equals("Monitors")) {
                // resolution should be scaled
                xRes = 15 + (21 * (xRes - 1));
                yRes = 10 + (14 * (yRes - 1));
                characters = true;
            }
            if (characters && isHD) {
                xRes *= 2;
                yRes *= 3;
            }
            return Utils.resizeImageLinear(image, xRes, yRes);
        } catch (NumberFormatException nfe) {
            System.out.println("Error scaling image, invalid size provided.");
            return super.process(image, m); // just return a clone of the image
        }
    }

    @Override
    public String toString() {
        return "Scale";
    }
}

