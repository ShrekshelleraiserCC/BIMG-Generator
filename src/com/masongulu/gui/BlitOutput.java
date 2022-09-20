package com.masongulu.gui;

import com.masongulu.blit.BlitMap;
import com.masongulu.formats.FormatBBF;
import com.masongulu.formats.FormatBIMG;
import com.masongulu.formats.FormatBase;
import com.masongulu.formats.FormatNFP;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class BlitOutput extends JPanel {
    private final JComboBox<FormatBase> spinner = new JComboBox<>(new FormatBase[]{
            new FormatBIMG(),
            new FormatBBF(),
            new FormatNFP()
    });
    private final StateMachine m;
    private BlitMap[] blitMaps = null;

    public BlitOutput(StateMachine m) {
        super(new GridBagLayout());
        this.m = m;
        GridBagConstraints c = new GridBagConstraints();
        this.add(new JLabel("Output"), c);
        c.gridy = 1;
        spinner.setSelectedIndex(0);
        this.add(spinner, c);
        c.gridy = 2;
        JFileChooser chooser = new JFileChooser();
        JButton button = new JButton("Save");
        button.addActionListener(actionEvent -> {
            if (blitMaps == null) return;
            FormatBase format = (FormatBase) spinner.getSelectedItem();
            assert format != null;
            chooser.setFileFilter(new FileNameExtensionFilter(format.toString(), format.toString()));
            int state = chooser.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                format.save(blitMaps, file);
            }
        });
        this.add(button, c);
        spinner.addActionListener(actionEvent -> {
            FormatBase format = (FormatBase) spinner.getSelectedItem();
            assert format != null;
            m.supportsCharacters.set(format.supportCharacters());
            m.supportsCustomPalette.set(format.supportCustomPalette());
        });
    }

    public void setBlitMaps(BlitMap[] blitMaps) {
        this.blitMaps = blitMaps;
    }
}
