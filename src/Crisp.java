import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

// ~ ----- -- - ----- - -- ----- ~ //

public class Crisp extends JFrame{

    // --- ~ main method ~ --- //
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Crisp();
            }
        });
    }

    private JPanel p;
    private BufferedImage sprite =  new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public Crisp() {

        // --- ~ settings jframe ~ --- //
        setResizable(false);
        setTitle("~ --- Xiangsu --- ~");
        setBounds(0, 0, 320, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- ~ create jpanel ~ --- //
        p = new JPanel();
        p.setLayout(null);
        setContentPane(p);

        // --- ~ pick a size ~ --- //
        JLabel chooseSize = new JLabel("~ Size:");
        chooseSize.setBounds(205, 10, 120, 20);
        p.add(chooseSize);

        JComboBox<Integer> sizeBox = new JComboBox<>();
        sizeBox.setBounds(200, 30, 110, 20);
        sizeBox.setEditable(true);
        // ~ adds possible font sizes
        for (int s: new int[] {8, 9, 10, 11, 12, 14, 16, 24, 36}) {
            sizeBox.addItem(s);
        }
        sizeBox.setEditable(false);
        sizeBox.setSelectedItem(8);
        p.add(sizeBox);

        // --- ~ add a preview ~ --- //
        JLabel preview = new JLabel(new ImageIcon(sprite));
        preview.setBounds(10, 80, 300, 40);
        p.add(preview);

        // --- ~ pick a font ~ --- //
        JLabel chooseFont = new JLabel("~ Pick a Font:");
        chooseFont.setBounds(15, 10, 160, 20);
        p.add(chooseFont);

        JComboBox<String> fontBox = new JComboBox<>();
        fontBox.setBounds(10, 30, 160, 20);
        fontBox.setEditable(true);
        fontBox.addItem("~");
        // ~ gets all the local fonts on your computer
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        for (Font font: fonts) fontBox.addItem(font.getFontName());
        fontBox.setEditable(false);
        fontBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sprite = createSprite((String) fontBox.getSelectedItem(), (int) sizeBox.getSelectedItem());
            }
        });
        p.add(fontBox);

        // --- ~ generate sprite ~ --- //
        JButton fried = new JButton("Generate Sprite");
        fried.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ~ save sprite
                saveFile(sprite);
            }
        });
        fried.setBounds(10, 80, 300, 40);
        p.add(fried);

        // --- ~ little self-promo ~ --- //
        JLabel credits = new JLabel();
        String url = "http://www.breachalk.com";
        credits.setText("Made by Breachalk");
        credits.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent me) {
                credits.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent me) {
                credits.setCursor(Cursor.getDefaultCursor());
            }
            public void mouseClicked(MouseEvent e) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URI(url));
                    } catch (Exception exception) {
                        System.err.println("Whoops");
                    }
                }
            }
        });
        credits.setBounds(10, 130, 295, 20);
        credits.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(credits);

        setVisible(true);
    }

    // ~ ----- -- - ----- - -- ----- ~ //

    private BufferedImage createSprite(String font, int size) {
        // ~ new chosen font
        Font f = new Font(font, Font.PLAIN, size);
        Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

        // ~ calculate width for each character,
        //   add +2 each time for the start- and stop-tags
        int w = 0;
        FontMetrics m = g.getFontMetrics(f);
        for (int c = 0; c < 256; c++) w += m.charWidth((char) c) + 2;

        // ~ calculate height, add +1 for the row w/ tags
        FontRenderContext rc = g.getFontRenderContext();
        // ~ string w/ all possible characters
        String s = "";
        for (int c = 0; c < 256; c++) s += (char) c;
        int h = f.createGlyphVector(rc, s).getPixelBounds(null, 0, 0).height + 1;

        // ~ modify the height to suppress high characters
        for (int c = 0; c < 256; c++) {
            size = (size + f.createGlyphVector(rc, Character.toString((char) c)).getPixelBounds(null, 0, 0).height) / 2;
        }

        // ~ create final sprite
        BufferedImage sprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = sprite.createGraphics();

        int x = 0;
        graphics.setFont(f);
        for (int c = 0; c < 256; c++) {
            // ~ red tag: start
            sprite.setRGB(x++, 0, 0xffff0000);
            // ~ draw character
            graphics.setColor(Color.white);
            graphics.drawString(String.valueOf((char) c), x, size + 1);
            x += m.charWidth((char) c);
            // ~ yellow tag: end
            sprite.setRGB(x++, 0, 0xffffff00);
        }

        return sprite;
    }

    // ~ ----- -- - ----- - -- ----- ~ //

    private void saveFile(BufferedImage sprite) {
        JFileChooser fileChooser = new JFileChooser() {
            // ~ make sure it is an .png
            public File getSelectedFile() {
                File file = super.getSelectedFile();
                if (file == null) return null;
                String name = file.getName();
                if (!name.endsWith(".png")) name += ".png";
                return new File(file.getParentFile(), name);
            }

            // ~ custom messages
            public void approveSelection() {
                File file = getSelectedFile();
                if (file.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this,
                            "The file " + file.getName() + " already exists. Want to replace it?", "~ overwrite file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION: super.approveSelection(); return;
                        case JOptionPane.NO_OPTION: return;
                        case JOptionPane.CLOSED_OPTION: return;
                        case JOptionPane.CANCEL_OPTION: cancelSelection(); return;
                    }
                } super.approveSelection();
            }
        };

        fileChooser.setDialogTitle("~ save file");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG (.png)", "png"));

        // ~ write out sprite
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(sprite, "png", file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getCanonicalName(), JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, "Font saved at " + file.getAbsolutePath(), "~ file saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
