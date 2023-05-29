package com.masongulu.gui;

import com.masongulu.blit.BlitMap;
import com.masongulu.colors.Color;
import com.masongulu.colors.Palette;
import com.masongulu.utils.Property;
import com.masongulu.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Previewer extends ScrollPane {
    private final Property<BufferedImage> preview = new Property<>();
    private final JPanel bottomPanel = new JPanel(new GridBagLayout());
    private final Property<Integer> scale = new Property<>(1);
    private final JSpinner frameSelect = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
    private final StateMachine m;
    private BufferedImage preScaledPreview;
    private BlitMap[] blitMaps;

    public Previewer(StateMachine m) {
        this.m = m;
        JLabel previewImage = new JLabel();
        preview.addListener((oldValue, newValue) -> {
            previewImage.setIcon(new ImageIcon(newValue));
            previewImage.repaint();
        });
        this.add(previewImage);

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JPG, PNG, GIF", "jpg", "jpeg", "png", "gif"));
        JButton savePreview = new JButton("Save Preview");
        bottomPanel.add(savePreview);
        savePreview.addActionListener(actionEvent -> {
            if (preview.get() == null) return;
            int state = chooser.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    ImageIO.write(preview.get(), FilenameUtils.getExtension(file.getName()), file);
                } catch (IOException e) {
                    System.out.println("Unable to save file " + e);
                }

            }
        });
        JSpinner visualScale = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        visualScale.addChangeListener(changeEvent -> {
            scale.set((Integer) visualScale.getValue());
            updatePreview();
        });
        frameSelect.addChangeListener(changeEvent -> {
            calculatePreview();
            updatePreview();
        });

        bottomPanel.add(new JLabel("Scale: "));
        bottomPanel.add(visualScale);
        bottomPanel.add(new JLabel("Frame: "));
        bottomPanel.add(frameSelect);
    }

    private BufferedImage convert(BlitMap blitMap) {
        BufferedImage newImage = new BufferedImage(blitMap.getWidth() * 2,
                blitMap.getHeight() * 3, BufferedImage.TYPE_INT_RGB);
        Palette imPalette = blitMap.getPalette();
        for (int line = 0; line < blitMap.getHeight(); line++) {
            String lineString = blitMap.getCharacter(line);
            String lineBG = blitMap.getBG(line);
            String lineFG = blitMap.getFG(line);
            for (int i = 0; i < lineString.length(); i++) {
                // iterate over line of characters
                int chCode = lineString.charAt(i) - 0x80;
                Color BG = imPalette.getColor(Character.digit(lineBG.charAt(i), 16));
                Color FG = imPalette.getColor(Character.digit(lineFG.charAt(i), 16));
                if (chCode < 0 || chCode > 64) {
                    // this character is not a graphics character, so just fill this section with pixels
                    for (int dx = 0; dx < 2; dx++) {
                        for (int dy = 0; dy < 3; dy++) {
                            int x = i * 2 + dx;
                            int y = line * 3 + dy;
                            if (y < newImage.getHeight() && x < newImage.getWidth())
                                newImage.setRGB(x, y, BG.getColor());
                        }
                    }
                } else {
                    // this character IS a graphics character, now we suffer as we draw it
                    for (int dy = 0; dy < 3; dy++) {
                        for (int dx = 0; dx < 2; dx++) {
                            int x = i * 2 + dx;
                            int y = line * 3 + dy;
                            int pixelIndex = (dx + dy * 2);
                            if (y < newImage.getHeight() && x < newImage.getWidth()) {
                                int val = (chCode & (1 << pixelIndex));
                                if (val > 0) {
                                    // this pixel is FG
                                    newImage.setRGB(x, y, FG.getColor());
                                } else {
                                    // this pixel is BG
                                    newImage.setRGB(x, y, BG.getColor());
                                }
                            }

                        }
                    }
                }
            }
        }
        return newImage;
    }

    public void setBlitMap(BlitMap[] preview) {
        this.blitMaps = preview;
        frameSelect.setModel(new SpinnerNumberModel(0, 0, preview.length - 1, 1));
        calculatePreview();
        updatePreview();
    }

    // reconvert the preview
    private void calculatePreview() {
        preScaledPreview = convert(blitMaps[(int) frameSelect.getValue()]);
    }

    private void updatePreview() {
        this.preview.set(Utils.scaleImage(preScaledPreview, scale.get(), scale.get()));
    }

    public JPanel getBottomBar() {
        return bottomPanel;
    }
}
