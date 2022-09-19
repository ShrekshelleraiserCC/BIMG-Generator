package com.masongulu.filters;


import com.masongulu.utils.Utils;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class FilterNone {
    protected JPanel frame = new JPanel();

    public FilterNone() {
    }

    public JPanel getFrame() {
        return frame;
    }

    public BufferedImage process(BufferedImage image, boolean isHD) {
        return Utils.copyImage(image);
    }
}
