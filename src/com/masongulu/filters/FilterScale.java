package com.masongulu.filters;

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
    private JTextField horizontal = new JTextField("51", 3);
    private JTextField vertical = new JTextField("19", 3);
    private JComboBox units = new JComboBox(unitChoices);

    public FilterScale() {
        frame = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        frame.add(new JLabel("Fit to scale"), c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        frame.add(new JLabel("x"), c);
        c.gridx = 1;
        frame.add(horizontal, c);
        c.gridx = 2;
        frame.add(new JLabel("y"), c);
        c.gridx = 3;
        frame.add(vertical, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        frame.add(units, c);
    }

    @Override
    public BufferedImage process(BufferedImage image, boolean isHD) {
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
            return super.process(image, isHD); // just return a clone of the image
        }
    }
}

