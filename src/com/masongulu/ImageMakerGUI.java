package com.masongulu;

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

public class ImageMakerGUI {
    public static final String VERSION = "10.0";
    private JFrame frame;
    public void startGUI() {
        frame = new JFrame("BIMG Generator " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        FileInput fileInput = new FileInput();
        frame.add(fileInput.getPanel());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ImageMakerGUI gui = new ImageMakerGUI();
        gui.startGUI();

    }
}

/**
 * Class that handles the Open button, and loading the image file when the user selects one
 */
class FileInput {
    private BufferedImage[] image = null;
    private final JFileChooser chooser = new JFileChooser();
    private final JPanel panel = new JPanel();

    FileInput() {
        chooser.setFileFilter(new FileNameExtensionFilter("JPG, PNG, GIF", "jpg", "jpeg", "png", "gif"));
        JButton openButton = new JButton("Open");
        openButton.addActionListener((e) -> {
            // Show file input dialog and attempt to open image
            int state = chooser.showOpenDialog(panel);
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
        panel.add(openButton);
    }

    public Optional<BufferedImage[]> getImage() {
        return Optional.ofNullable(image);
    }
    public JPanel getPanel() {
        return panel;
    }
}

