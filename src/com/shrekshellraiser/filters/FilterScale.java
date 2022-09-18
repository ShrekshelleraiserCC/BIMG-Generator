package com.shrekshellraiser.filters;

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
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        frame.add(new JLabel("Fit to scale"), c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        frame.add(horizontal, c);
        c.gridx = 1;
        frame.add(vertical, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        frame.add(units, c);

    }

    public static void main(String[] args) {
        FilterScale test = new FilterScale();
        JFrame frame = new JFrame("TEST");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(test.getFrame());

        frame.setVisible(true);
    }

    @Override
    public BufferedImage process(BufferedImage image, boolean isHD) {
        String selection = unitChoices[units.getSelectedIndex()];

        return super.process(image, isHD);
    }
}

