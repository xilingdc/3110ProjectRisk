import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Xiling
 * @author Aleksandar Veselinovic
 */

public class Controller implements ActionListener {
    private Model model;
    private Country attacker;
    private Country defender;//countries for attacking
    private Country country1;
    private Country country2;//countries for fortifying
    private int placementTroops;//troops for bonus troop placement
    private View view;


    /**
     * Constructor of Controller
     *
     * @param model the model that is controlled
     */
    public Controller(Model model,View view){
        this.model = model;
        this.view = view;
        attacker = null;
        defender = null;
        placementTroops = model.bonusTroopCalculator();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("pass")){//if the pass button was pressed
            if(!model.isPlacementPhase()) {//if the game isn't in placement phase
                model.pass();//pass
                attacker = null;
                defender = null;
                country1 = null;
                country2 = null;//reset all selected countries
            }else{
                //tell the user to place all their troops
                view.showMessage("Place your troops first!"+" You still have "+placementTroops+" more troops to add");
            }
            if(placementTroops == 0){//if all bonus troops are placed
                placementTroops = model.bonusTroopCalculator();//get the bonus troops for the next player
            }
        }else if (e.getActionCommand().equals("cancel")) {//if the cancel button was pressed
            attacker = null;
            defender = null;
            country1 = null;
            country2 = null;//reset all selected countries
        }else if (e.getActionCommand().equals("fortify")) {//if the fortify button was pressed
            if(!model.isPlacementPhase()) {//if the game isn't in placement phase
                model.activateFortify();
            }else{
                view.showMessage("Place your troops first!"+" You still have "+placementTroops+" more troops to add");
            }
        }else if(e.getActionCommand().equals("Country")){//if a country button was pressed

            CountryButton b = (CountryButton) e.getSource();//get the country button that was pressed

            if(model.isPlacementPhase()){//if the game is in placement phase
                if(placementTroops == 0){
                    placementTroops = model.bonusTroopCalculator();//get the bonus troops
                }
                //move troops to country represented by b
                placementTroops = model.troopPlacement(placementTroops, b.getCountry());
            }
            else if(model.isFortifyPhase()){//if the game is in fortify phase
                if(country1 == null){//if the first country hasn't been selected
                    if (model.isFortifying(b.getCountry())) {//if the country can send troops
                        country1 = b.getCountry();//store the country
                    }
                }
                else{//if the first country has been selected
                    //if the two countries can be used for fortifying
                    if(model.canFortify(country1, b.getCountry())){
                        country2 = b.getCountry();
                        model.fortify(country1, country2);//fortify with the 2 countries
                        country1 = null;
                        country2 = null;
                        attacker = null;
                        defender = null;//reset all selected countries
                    }
                }
            }else{
                if (attacker == null) {//if the attacker hasn't been selected yet
                    if (model.isAttacker(b.getCountry())) {//if the country represented by the button can attack
                        attacker = b.getCountry();//store the country represented by the button
                    }
                } else {//if the attacker has been selected
                    if (model.canDefend(attacker, b.getCountry())) {//if the country represented by the button can defend the attacking country
                        defender = b.getCountry();//store the country represented by the button
                        model.attack(attacker, defender);
                        attacker = null;
                        defender = null;
                    }
                }
            }
        }
    }

}
