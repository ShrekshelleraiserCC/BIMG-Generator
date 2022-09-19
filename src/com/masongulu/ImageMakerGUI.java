package com.masongulu;

import com.masongulu.colors.Palette;
import com.masongulu.colors.PaletteImage;
import com.masongulu.quantizers.QuantizeBlueNoise;
import com.masongulu.quantizers.QuantizeFloydSteinberg;
import com.masongulu.quantizers.QuantizeNone;
import com.masongulu.quantizers.QuantizeOrdered;
import com.masongulu.utils.GifReader;
import com.masongulu.utils.KMeans;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ImageMakerGUI {
    public static final String VERSION = "10.0";
    private JFrame frame;
    private JPanel panel;
    public void startGUI() {
        frame = new JFrame("BIMG Generator " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        GridBagConstraints c = new GridBagConstraints();

        FileInput fileInput = new FileInput();
        panel.add(fileInput.getPanel());

        PaletteManager paletteManager = new PaletteManager();
        panel.add(paletteManager.getPanel());

        QuantizeManager quantizeManager = new QuantizeManager();
        panel.add(quantizeManager.getPanel());

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

class PaletteManager {
    private final JPanel panel = new JPanel(new GridBagLayout());
    private final JComboBox<String> spinner = new JComboBox<>(new String[]{
            "Default",
            "K-Means",
            "KM (1st frame)"
    });

    public PaletteManager() {
        GridBagConstraints c = new GridBagConstraints();
        panel.add(new JLabel("Palette"), c);
        spinner.setSelectedIndex(0);
        c.gridy = 1;
        panel.add(spinner, c);
    }

    public Palette[] getPalettes(BufferedImage[] images) {
        String selection = (String) spinner.getSelectedItem();
        Palette[] palettes = new Palette[images.length];
        boolean copyFirst = false;
        if (("Default").equals(selection)) {
            copyFirst = true;
            palettes[0] = Palette.defaultPalette;
        } else if (("K-Means").equals(selection)) {
            for (int i = 0; i < images.length; i++) {
                palettes[i] = new Palette(KMeans.applyKMeans(images[i], 16));
            }
        } else {
            copyFirst = true;
            palettes[0] = new Palette(KMeans.applyKMeans(images[0], 16));
        }
        if (copyFirst) {
            for (int i = 1; i < images.length; i++) {
                palettes[i] = palettes[0];
            }
        }
        return palettes;
    }

    public JPanel getPanel() {
        return panel;
    }
}

class QuantizeManager {
    private final JPanel panel = new JPanel(new GridBagLayout());
    private final JComboBox<QuantizeNone> spinner = new JComboBox<>(new QuantizeNone[]{
            new QuantizeNone(),
            new QuantizeOrdered(),
            new QuantizeBlueNoise(),
            new QuantizeFloydSteinberg()
    });
    private JPanel lastSelected;

    public QuantizeManager() {
        GridBagConstraints c = new GridBagConstraints();
        spinner.setSelectedIndex(0);
        lastSelected = ((QuantizeNone) Objects.requireNonNull(spinner.getSelectedItem())).getPanel();
        panel.add(new JLabel("Dither"));
        c.gridy = 1;
        panel.add(spinner, c);
        c.gridy = 2;
        spinner.addActionListener(actionEvent -> {
            if (lastSelected != null)
                panel.remove(lastSelected);
            lastSelected = ((QuantizeNone) Objects.requireNonNull(spinner.getSelectedItem())).getPanel();
            if (lastSelected != null) {
                panel.add(lastSelected, c);
            }
            panel.revalidate();
        });
    }

    public PaletteImage[] getImage(BufferedImage[] images, Palette[] palettes) {
        return null; // TODO
    }

    public JPanel getPanel() {
        return panel;
    }
}

class FilterManager {
    private final JPanel panel = new JPanel(new GridBagLayout());

    public FilterManager() {

    }

    public BufferedImage[] filter(BufferedImage[] images) {
        return null; // TODO
    }
}