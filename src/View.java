import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Xiling
 */
public class View extends JFrame {
    private JButton[] countries;
    private JTextField gameInfo,playerTurn;
    private  JButton pass, attack;
    private JPanel contentPane;
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

        Model model = new Model();
        model.setView(this);
        Controller controller = new Controller(model);



        model.processBegin(numPlayer);
        model.setUp();

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
        /*this.setLayout(new GridLayout(10,15));

        countries = new JButton[model.getMap().getCountries().size()];
        for (int i = 0; i < new Map().getCountries().size(); i++) {

            JButton button = new JButton(model.getMap().getCountry(i).getName());
            countries[i]=button;
            button.setBackground(model.getMap().getCountry(i).getColor());
            button.addActionListener(controller);
            button.setActionCommand(model.getMap().getCountry(i).getName());
            this.add(button);//temporary
            //TODO set button's layout
        }*/






        this.setSize(1600,1000);
        this.setVisible(true);



    }

    public void updateTextInfoHandler(String countryName, String owner, int troop){
//        System.out.println(countryName+owner+" troops: "+troop);
        gameInfo.setText(countryName+": player "+owner+", troop: "+troop);
    }

    public void updatePlayerTurnTextHandler(String playerName){
        playerTurn.setText("It is player "+playerName+"'s turn.");
    }

    public void updateCountryOwner(int index, Color color){
        countries[index].setBackground(color);
    }

    public static void main(String[] args) throws IOException {
        new View();
    }
}
