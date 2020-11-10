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
        for (Country country : model.getMap().getCountries()) {//loop through the countries of the game
            CountryButton button = new CountryButton(country.getArmySize(), country);//make a new JButton with the number of troops of the country as the label
            button.addActionListener(controller);
            button.setActionCommand("Country");//used to identify which JButton was pressed
            button.setMargin(new Insets(0, 0, 0, 0));//remove margins of the button
            button.setBounds(country.getX(), country.getY(), 30, 30);//put the button on top of its country on the map
            button.setBackground(country.getColor());//set the color to the country's color
            countryButtons.put(country, button);//make a connection between the country and the button
            map.add(button);
        }
        this.add(map, BorderLayout.CENTER);

        this.setSize(1600,1000);
        this.setVisible(true);
    }

    /**
     * Updates the playerTurn JTextArea when the player changes
     *
     * @param player current player
     */
    public void updatePlayerTurnTextHandler(Player player){
        playerTurn.setText("Current Player: Player "+player.getName());
        playerTurn.setForeground(player.getColor());//set the color of the text to the player's color
    }

    /**
     * Updates the button whose country changed state
     *
     * @param country the country whose state changed
     * @param color the color the button should change to
     * @param troops the number the button should say
     */
    public void updateCountryButton(Country country, Color color, int troops){
        CountryButton b = countryButtons.get(country);//find the button corresponding to the country
        b.setBackground(color);
        b.setText(""+troops);
    }

    /**
     * Adds the player name to the bottom panel
     *
     * @param message the player name
     * @param color the color of the player name
     */
    public void addPlayer(String message, Color color) {
        JLabel player = new JLabel("  " + message + "  ");
        player.setForeground(color);//set the color of the player name to their color
        player.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));//increase the font size
        bottomPanel.add(player);//add the player name to the bottom panel
    }

    /**
     * Opens a dialog box with the given message
     *
     * @param message the message to be shown
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Opens a dialog box that asks the user how many dice they want to play.
     * Returns the number the player chose.
     *
     * @param message the message to be shown
     * @param maxDice the maximum number of dice the player can choose
     * @return the number of dice the user chose
     */
    public int getDice(String message, int maxDice) {
        String dice = JOptionPane.showInputDialog(this, message);//open the dialog box
        while (true) {
            if (dice.isEmpty()) {//if the player entered nothing
                dice = JOptionPane.showInputDialog(this, message);//open the dialog box again
            } else {
                int numberOfDice = Integer.parseInt(dice);
                if (numberOfDice < 1 || numberOfDice > maxDice) {//if the user entered an invalid number
                    dice = JOptionPane.showInputDialog(this, message);//open the dialog box again
                } else return numberOfDice;
            }
        }
    }

    /**
     * Opens a dialog box that says who won the game, then closes the game
     *
     * @param message the message to be shown
     */
    public void showEndMessage(String message) {
        JOptionPane.showMessageDialog(this, message);//open the dialog box
        this.dispose();//close the frame
    }

    public Model getModel() {
        return model;
    }

    public static void main(String[] args) throws IOException {
        new View();
    }
}
