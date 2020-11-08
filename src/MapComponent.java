import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapComponent extends JComponent {
    private final Image background;
    private ArrayList<JButton> countryButtons;

    public MapComponent(String fileName, ArrayList<Country> countries) throws IOException {
        background = ImageIO.read(new File(fileName));
        setLayout(null);
        countryButtons = new ArrayList<>();
        for (Country country : countries) {
            JButton button = new JButton("" + country.getArmySize());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    button.setBackground(Color.RED);
                }
            });
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setBounds(country.getX(), country.getY(), 30, 30);
            button.setBackground(country.getColor());
            add(button);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 300, 0, null);
    }
}
