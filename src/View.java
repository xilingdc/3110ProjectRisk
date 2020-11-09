import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Xiling
 */
public class View extends JFrame {
    private JTextField gameInfo,playerTurn;
    private  JButton pass, attack;
    private HashMap<Country, CountryButton> countryButtons;

    public View() throws IOException {
        super("Risk Domination");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        String userInput = JOptionPane.showInputDialog(this,"Enter Player Number(2-6): ");
        int numPlayer = 0;
        while(true) {
            if (userInput.isEmpty()) {
                userInput = JOptionPane.showInputDialog(this,"Enter Player Number(2-6): ");
            }else{
                try{
                    numPlayer = Integer.parseInt(userInput);
                    if(numPlayer<2||numPlayer>6){
                        userInput = JOptionPane.showInputDialog(this,"Enter Player Number(2-6): ");
                    }else{
                        break;
                    }

                }catch (NumberFormatException e){
                    userInput = JOptionPane.showInputDialog(this,"Enter Player Number(2-6): ");
                }
            }
        }

        Model model = new Model(numPlayer);
        model.setView(this);
        Controller controller = new Controller(model);

        JPanel topPanel = new JPanel();
        playerTurn = new JTextField("It is player "+model.getCurrentPlayer().getName()+"'s turn.");
        gameInfo = new JTextField("Game Info Text Box");
        pass = new JButton("Pass");
        pass.addActionListener(controller);
        pass.setActionCommand("pass");

        attack = new JButton("Attack");
        attack.addActionListener(controller);
        attack.setActionCommand("Attack");

        topPanel.add(playerTurn);
        topPanel.add(gameInfo);
        topPanel.add(attack);
        topPanel.add(pass);
        this.add(topPanel, BorderLayout.NORTH);

        MapComponent map = new MapComponent("risk-board-white.png");
        countryButtons = new HashMap<>();
        for (Country country : model.getMap().getCountries()) {
            CountryButton button = new CountryButton(country.getArmySize(), country);
            button.addActionListener(controller);
            button.setActionCommand("Country");
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setBounds(country.getX(), country.getY(), 30, 30);
            button.setBackground(country.getColor());
            countryButtons.put(country, button);
            map.add(button);
        }
        this.add(map, BorderLayout.CENTER);

        this.setSize(1600,1000);
        this.setVisible(true);
    }

    public void updateTextInfoHandler(String countryName, String owner, int troop){
        //System.out.println(countryName+owner+" troops: "+troop);
        gameInfo.setText(countryName+": player "+owner+", troop: "+troop);
    }

    public void updatePlayerTurnTextHandler(String playerName){
        playerTurn.setText("It is player "+playerName+"'s turn.");
    }

    public void updateCountryButton(Country country, Color color, int troops){
        CountryButton b = countryButtons.get(country);
        b.setBackground(color);
        b.setText(""+troops);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public int getDice(String message, int maxDice) {
        String dice = JOptionPane.showInputDialog(this, message);
        while (true) {
            if (dice.isEmpty()) {
                dice = JOptionPane.showInputDialog(this, message);
            } else {
                int numberOfDice = Integer.parseInt(dice);
                if (numberOfDice < 1 || numberOfDice > maxDice) {
                    dice = JOptionPane.showInputDialog(this, message);
                } else return numberOfDice;
            }
        }
    }

    public void showEndMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
        this.dispose();
    }

    public static void main(String[] args) throws IOException {
        new View();
    }
}
