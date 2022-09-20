package com.masongulu;

import com.masongulu.blit.BlitMap;
import com.masongulu.colors.Palette;
import com.masongulu.colors.PaletteImage;
import com.masongulu.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageMakerGUI {
    public static final String VERSION = "10.0";
    private final JFrame frame = new JFrame("BIMG Generator " + VERSION);
    private final JPanel topBarPanel = new JPanel(new GridBagLayout());
    private final JCheckBox useCharacters = new JCheckBox("HD", true);
    private final StateMachine m = new StateMachine();

    public void startGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(topBarPanel, BorderLayout.NORTH);

        GridBagConstraints c = new GridBagConstraints();

        FileInput fileInput = new FileInput(m);

        FilterManager filterManager = new FilterManager(m);

        PaletteManager paletteManager = new PaletteManager(m);

        QuantizeManager quantizeManager = new QuantizeManager(m);
        BlitOutput blitOutput = new BlitOutput(m);

        Previewer previewer = new Previewer(m);

        JPanel controller = new JPanel();
        controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));
        controller.add(useCharacters);
        useCharacters.addActionListener(actionEvent -> m.usingCharacters.set(useCharacters.isSelected()));
        m.supportsCharacters.addListener((oldValue, newValue) -> {
            if (newValue) {
                // enable
                useCharacters.setEnabled(true);
            } else {
                // disable
                useCharacters.setEnabled(false);
                useCharacters.setSelected(false);
            }
        });
        JButton generate = new JButton("Generate");
        generate.addActionListener(actionEvent -> {
            if (fileInput.getImage().isPresent()) {
                // make sure there's an input image
                BufferedImage[] input = fileInput.getImage().get();
                BufferedImage[] processed = filterManager.filter(input);
                Palette[] palettes = paletteManager.getPalettes(processed);
                PaletteImage[] paletteImages = quantizeManager.getImage(processed, palettes);
                BlitMap[] blitMaps = new BlitMap[paletteImages.length];
                for (int i = 0; i < blitMaps.length; i++) {
                    blitMaps[i] = new BlitMap(paletteImages[i], m.usingCharacters.get());
                }
                previewer.setBlitMap(blitMaps);
                blitOutput.setBlitMaps(blitMaps);
            }
        });
        controller.add(generate);

        c.gridx = 0;
        topBarPanel.add(fileInput, c);
        c.gridx = 1;
        topBarPanel.add(filterManager, c);
        c.gridx = 2;
        topBarPanel.add(paletteManager, c);
        c.gridx = 3;
        topBarPanel.add(quantizeManager, c);
        c.gridx = 4;
        topBarPanel.add(controller, c);
        c.gridx = 5;
        topBarPanel.add(blitOutput, c);
        frame.getContentPane().add(previewer, BorderLayout.CENTER);
        frame.getContentPane().add(previewer.getBottomBar(), BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ImageMakerGUI gui = new ImageMakerGUI();
        gui.startGUI();

    }
}

