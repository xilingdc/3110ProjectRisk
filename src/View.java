import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Xiling
 * @author Aleksandar Veselinovic
 * @author Ali Fahd
 */
public class View extends JFrame implements Views{
    private JTextArea playerTurn,placeNum;
    private  JButton pass, cancel, fortify;
    private HashMap<Country, CountryButton> countryButtons;
    private JPanel bottomPanel;
    private Model model;

    public View() throws IOException {
        super("Risk Domination");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        int numPlayer = getNumber("Enter Player Number(2-6):", 2, 6);
        int numAI = getNumber("Enter the number of AI players:", 0, numPlayer);

        model = new Model();
//        model.setView(this);
        model.addView(this);
        bottomPanel = new JPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);

        model.processBegin(numPlayer, numAI);
//        model.setUp();
        Controller controller = new Controller(model,this);

        JPanel topPanel = new JPanel();
        playerTurn = new JTextArea("Current Player: Player "+model.getCurrentPlayer().getName());
        playerTurn.setForeground(model.getCurrentPlayer().getColor());
        playerTurn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        pass = new JButton("Pass");
        fortify = new JButton("Fortify");
        pass.addActionListener(controller);
        pass.setActionCommand("pass");
        fortify.addActionListener(controller);
        fortify.setActionCommand("fortify");
        cancel = new JButton("Cancel Selection");
        cancel.addActionListener(controller);
        cancel.setActionCommand("cancel");
        placeNum = new JTextArea("Bonus Troop Number: "+  model.bonusTroopCalculator());
        placeNum.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        topPanel.add(playerTurn);
        topPanel.add(pass);
        topPanel.add(fortify);
        topPanel.add(cancel);
        topPanel.add(placeNum);
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


        model.activatePlacement();
    }

    /**
     * Opens a dialog box with the given message
     *
     * @param message the message to be shown
     */

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }


    /*
     * Opens a dialog box that asks the user for a number based on the given message.
     * Returns the number the player chose.
     *
     * @param message the message to be shown
     * @param max the maximum number the player can choose
     * @param min the minimum number the player can choose
     * @return the number of dice the user chose
     */
    public int getNumber(String message, int min, int max) {
        String input = JOptionPane.showInputDialog(this, message);//open the dialog box
        while (true) {
            if (input.isEmpty()) {//if the player entered nothing
                input = JOptionPane.showInputDialog(this, message);//open the dialog box again
            } else {
                int number = Integer.parseInt(input);
                if (number < min || number > max) {//if the user entered an invalid number
                    input = JOptionPane.showInputDialog(this, message);//open the dialog box again
                } else return number;
            }
        }
    }

    /**
     *
     * @return
     */
    public Model getModel() {
        return model;
    }



    public static void main(String[] args) throws IOException {
        new View();
    }




    /**
     * Opens a dialog box with the given message
     * @param event
     */
    @Override
    public void showMessage(Event event) {
        String message = event.getMessage();
        JOptionPane.showMessageDialog(null, message);
    }


    /**
     *  Opens a dialog box that asks the user for a number based on the given message.
     *  Returns the number the player chose.
     * @param event
     * @return
     */
    @Override
    public int getNumberFromDiolog(Event event) {
        String message = event.getMessage();
        int max = event.getMax();
        int min = event.getMin();
        String input = JOptionPane.showInputDialog(this, message);//open the dialog box
        while (true) {
            if (input.isEmpty()) {//if the player entered nothing
                input = JOptionPane.showInputDialog(this, message);//open the dialog box again
            } else {
                int number = Integer.parseInt(input);
                if (number < min || number > max) {//if the user entered an invalid number
                    input = JOptionPane.showInputDialog(this, message);//open the dialog box again
                } else return number;
            }
        }
    }

    @Override
    public void updateCountryButton(Event event) {
        CountryButton b = countryButtons.get(event.getCountry());//find the button corresponding to the country
        b.setBackground(event.getColor());
        b.setText(""+event.getTroop());
    }


    /**
     * Adds the player name to the bottom panel
     * @param event
     */
    @Override
    public void addPlayer(Event event) {
        JLabel player = new JLabel("  " + event.getPlayerName() + "  ");
        player.setForeground(event.getColor());//set the color of the player name to their color
        player.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));//increase the font size
        bottomPanel.add(player);//add the player name to the bottom panel
    }



    /**
     * Opens a dialog box that says who won the game, then closes the game
     * @param event
     */
    @Override
    public void showEndMessage(Event event) {
        JOptionPane.showMessageDialog(this, event.getMessage());//open the dialog box
        this.dispose();//close the frame
    }

    /**
     * Updates the playerTurn JTextArea when the player changes
     * @param event
     */
    @Override
    public void updatePlayerTurnTextHandler(Event event) {
        playerTurn.setText("Current Player: Player "+event.getPlayer().getName());
        playerTurn.setForeground(event.getPlayer().getColor());//set the color of the text to the player's color
//        updatePlaceNum(model.bonusTroopCalculator());
    }

    @Override
    public void updatePlaceNum(Event event) {
        placeNum.setText("Bonus Troop Number: "+ event.getTroop());
    }

   /* *//**
     * update placeNum's text
     * @param troop
     *//*
    public void updatePlaceNum(int troop){
        placeNum.setText("Bonus Troop Number: "+ troop);
    }
*/
}
