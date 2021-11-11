package Screenshot;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;

public class ImageSave {

    private JComponent ui = null;
    public final static String[] types = {"jpg","png"};
    private final JComboBox<String> typesBox = new JComboBox<>(types);
    BufferedImage image;
    BufferedImage imageCoded;
    JLabel imageLabel = new JLabel();
    JLabel imageCodedLabel = new JLabel();
    JLabel output = new JLabel("Output appears here..");

    ImageSave() {
        try {
            initUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void initUI() throws Exception {
        if (ui != null) {
            return;
        }

        ui = new JPanel(new BorderLayout(4,4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        URL url = new URL("https://i.stack.imgur.com/7bI1Y.jpg");
        image = ImageIO.read(url);
        imageLabel.setIcon(new ImageIcon(image));

        JPanel picsPanel = new JPanel(new GridLayout(1, 0, 4, 4));
        ui.add(picsPanel);

        picsPanel.add(imageLabel);
        picsPanel.add(imageCodedLabel);

        JToolBar toolBar = new JToolBar();
        ui.add(toolBar, BorderLayout.PAGE_START);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEADING));
        toolBar.add(typesBox);
        ActionListener refreshListener = (ActionEvent e) -> {
            try {
                updateGUI();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        typesBox.addActionListener(refreshListener);

        toolBar.add(output);

        updateGUI();
    }

    private void updateGUI() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String type = typesBox.getSelectedItem().toString();
        ImageIO.write(image, type, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        imageCoded = ImageIO.read(bais);
        imageCodedLabel.setIcon(new ImageIcon(imageCoded));

        int diff = compareImages();
        int max = image.getWidth()*image.getHeight()*256*3;
        double percent = 100d*(double)diff/(double)max;
        String s = "type: " + type + "  Color difference: " + diff + 
                " or " + percent + "%.";
        System.out.println(s);
        output.setText(s);
    } 

    private int compareImages() {
        int diff = 0;
        for (int xx=0; xx<image.getWidth(); xx++) {
            for (int yy=0; yy<image.getHeight(); yy++) {
                Color rgb1 = new Color(image.getRGB(xx, yy));
                Color rgb2 = new Color(imageCoded.getRGB(xx, yy));
                int r1 = rgb1.getRed();
                int g1 = rgb1.getGreen();
                int b1 = rgb1.getBlue();
                int r2 = rgb2.getRed();
                int g2 = rgb2.getGreen();
                int b2 = rgb2.getBlue();
                diff += Math.abs(r1-r2);
                diff += Math.abs(g1-g2);
                diff += Math.abs(b1-b2);
            }
        }
        return diff;
    }

    public JComponent getUI() {
        return ui;
    }

    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception useDefault) {
            }
            ImageSave o = new ImageSave();

            JFrame f = new JFrame(o.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            f.setContentPane(o.getUI());
            f.pack();
            f.setMinimumSize(f.getSize());

            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }
}