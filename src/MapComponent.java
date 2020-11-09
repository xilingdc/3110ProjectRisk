import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapComponent extends JComponent {
    private final Image background;

    public MapComponent(String fileName) throws IOException {
        background = ImageIO.read(new File(fileName));
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 300, 0, null);
    }
}
