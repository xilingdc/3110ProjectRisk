import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * @author Aleksandar Veselinovic
 */
public class MapComponent extends JComponent {
    private final Image background;//field that stores the map image

    /**
     * Constructor of MapComponent
     *
     * @param fileName the name of the image file
     * @throws IOException if file can't be read
     */
    public MapComponent(String fileName) throws IOException {
        background = ImageIO.read(new File(fileName));//store the image referenced by the file name
        setLayout(null);//set layout to absolute, allowing buttons to be placed by a point on the window
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 300, 0, null);//draw the image
    }
}
