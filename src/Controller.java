import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Xiling
 * @author Aleksandar Veselinovic
 */

public class Controller implements ActionListener {
    private Model model;
    private Country attacker;
    private Country defender;
    private Country country1;
    private Country country2;
    private int placementTroops;
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
            if(!model.isPlacementPhase()) {
                model.pass();
                attacker = null;
                defender = null;
                country1 = null;
                country2 = null;
            }else{
                view.showMessage("Place your troops first!"+" You still have "+placementTroops+" more troops to add");
            }
            if(placementTroops == 0){
                placementTroops = model.bonusTroopCalculator();
            }
        }else if (e.getActionCommand().equals("cancel")) {//if the cancel button was pressed
            attacker = null;
            defender = null;
            country1 = null;
            country2 = null;
        }else if (e.getActionCommand().equals("fortify")) {//if the fortify button was pressed
            if(!model.isPlacementPhase()) {
                model.activateFortify();
            }else{
                view.showMessage("Place your troops first!"+" You still have "+placementTroops+" more troops to add");
            }
        }else if(e.getActionCommand().equals("Country")){//if a country button was pressed

            CountryButton b = (CountryButton) e.getSource();//get the country button that was pressed

            if(model.isPlacementPhase()){
                if(placementTroops == 0){
                    placementTroops = model.bonusTroopCalculator();
                }
                placementTroops = model.troopPlacement(placementTroops, b.getCountry());
            }
            else if(model.isFortifyPhase()){
                if(country1 == null){
                    if (model.isFortifying(b.getCountry())) {
                        country1 = b.getCountry();
                    }
                }
                else{
                    if(model.canFortify(country1, b.getCountry())){
                        country2 = b.getCountry();
                        model.fortify(country1, country2);
                        country1 = null;
                        country2 = null;
                        attacker = null;
                        defender = null;
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
