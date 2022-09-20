package com.masongulu.gui;

import com.masongulu.utils.GifReader;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Class that handles the Open button, and loading the image file when the user selects one
 */
public class FileInput extends JPanel {
    private final JFileChooser chooser = new JFileChooser();
    private final StateMachine m;
    private BufferedImage[] image = null;

    public FileInput(StateMachine m) {
        this.m = m;
        chooser.setFileFilter(new FileNameExtensionFilter("JPG, PNG, GIF", "jpg", "jpeg", "png", "gif"));
        JButton openButton = new JButton("Open");
        openButton.addActionListener((e) -> {
            // Show file input dialog and attempt to open image
            int state = chooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "gif")) {
                        // terrible check for a gif file
                        image = GifReader.openGif(file, false); // TODO wipe frames option
                    } else {
                        image = new BufferedImage[]{ImageIO.read(file)};
                    }
                    System.out.println("Successfully opened file " + file.getName());
                } catch (IOException ex) {
                    System.out.println("Can't open file " + file.getName() + ": " + ex);
                }
            }
        });
        this.add(openButton);
    }

    public Optional<BufferedImage[]> getImage() {
        return Optional.ofNullable(image);
    }
}
