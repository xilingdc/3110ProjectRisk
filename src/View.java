import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Xiling
 * @author Aleksandar Veselinovic
 */
public class View extends JFrame {
    private JTextArea playerTurn;
    private  JButton pass, cancel;
    private HashMap<Country, CountryButton> countryButtons;
    private JPanel bottomPanel;
    private Model model;
    
    
    
    /**
    *@Constructor
    */
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

        model = new Model();
        model.setView(this);

        bottomPanel = new JPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);

        model.processBegin(numPlayer);
        model.setUp();
        Controller controller = new Controller(model);

        JPanel topPanel = new JPanel();
        playerTurn = new JTextArea("Current Player: Player "+model.getCurrentPlayer().getName());
        playerTurn.setForeground(model.getCurrentPlayer().getColor());
        playerTurn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        pass = new JButton("Pass");
        pass.addActionListener(controller);
        pass.setActionCommand("pass");
        cancel = new JButton("Cancel Attack");
        cancel.addActionListener(controller);
        cancel.setActionCommand("cancel");

        topPanel.add(playerTurn);
        topPanel.add(pass);
        topPanel.add(cancel);
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
    
    
    /**
    *update view's textfield to show whose turn in game
    *@param player - player
    */
    public void updatePlayerTurnTextHandler(Player player){
        playerTurn.setText("Current Player: Player "+player.getName());
        playerTurn.setForeground(player.getColor());
    }
    
    
    /**
    *update the button's new state (its new owner which means new color, new troop number)
    *@param country
    *@param color - country's color
    *@param troops - country's troops
    */
    public void updateCountryButton(Country country, Color color, int troops){
        CountryButton b = countryButtons.get(country);
        b.setBackground(color);
        b.setText(""+troops);
    }
    
    
    /**
    *@param message
    *@param color
    */
    public void addPlayer(String message, Color color) {
        JLabel player = new JLabel("  " + message + "  ");
        player.setForeground(color);
        player.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        bottomPanel.add(player);
    }
    
    
    
    /**
    *pop up windows
    *@param message
    */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    
    /**
    *@param message
    *@param maxDice
    */
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
    
    
    /**
    *@param message
    */
    public void showEndMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
        this.dispose();
    }
    
    
    /**
    *@return model
    */
    public Model getModel() {
        return model;
    }
    
    
    /**
    *main method
    *@param args
    */
    public static void main(String[] args) throws IOException {
        new View();
    }
}
