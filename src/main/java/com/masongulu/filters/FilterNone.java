package com.masongulu.filters;


import com.masongulu.gui.StateMachine;
import com.masongulu.utils.Utils;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class FilterNone {
    protected JPanel panel = null;

    public FilterNone() {
    }

    public JPanel getPanel() {
        return panel;
    }

    public BufferedImage process(BufferedImage image, StateMachine m) {
        return Utils.copyImage(image);
    }

    @Override
    public String toString() {
        return "None";
    }
}
