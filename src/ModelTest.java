import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
/**
 * @author Ali Fahd
 * @author Xiling Wang
 */
public class ModelTest{

    Model model;

    @Before
    public void testBegin(){
        model = new Model();
    }

    @Test
    public void testProcessBegin(){
        model.pureProcessBegin(5,3);
        assertEquals(model.getPlayers().size(),5);
        assertFalse(model.getPlayers().size()==3);
        assertFalse(model.getPlayers().size()==4);
        assertFalse(model.getPlayers().size()==2);
    }

    @Test
    public void testPlacingTroops(){
        model.pureProcessBegin(3,0);
        int totalPlaceNum=7;
        int previousTargetCountryTroop= model.getCurrentPlayer().countries.get(1).getArmySize();
        int restPlaceTroop = model.purePlacingTroop(totalPlaceNum,4,model.getCurrentPlayer().countries.get(1));
        assertEquals(previousTargetCountryTroop+4,model.getCurrentPlayer().countries.get(1).getArmySize());
        assertEquals(restPlaceTroop, 3);

    }

    @Test
    public void testAttack(){
        model.pureProcessBegin(2,0);
        Player p1 = model.getPlayers().get(0);
        Player p2 = model.getPlayers().get(1);
        Country country1=model.getMap().getCountry("Alaska");
        Country country2=model.getMap().getCountry("Alberta");;
        country1.setOwner(p1);
        country2.setOwner(p2);
        country1.addTroops(4);
        country2.addTroops(4);
        Integer[] attackerDice = {6,3,1};
        Integer[] defenderDice = {5,3,2};
        String[] message1 = model.pureAttack(country1,country2,attackerDice,defenderDice,0);
        assertEquals(message1[1],"Attacker wins");
        assertFalse(message1[1].equals("Defender wins"));
        String[] message2 = model.pureAttack(country1,country2,attackerDice,defenderDice,1);
        assertEquals(message2[1],"Defender wins");
        assertFalse(message2[1].equals("Attacker wins"));
        String[] message3 = model.pureAttack(country1,country2,attackerDice,defenderDice,2);
        assertEquals(message3[1],"Defender wins");
    }



    @Test
    public void testPass(){
        model.pureProcessBegin(2,0);
        Player p1 = model.getPlayers().get(0);
        Player p2 = model.getPlayers().get(1);
        assertEquals(p1,model.getCurrentPlayer());
        model.purePass();
        assertEquals(p2,model.getCurrentPlayer());
    }


    @Test
    public void testFortify(){
        model.pureProcessBegin(2,0);
        Player p1 = model.getPlayers().get(0);
        Country country1=model.getMap().getCountry("Alaska");
        Country country2=model.getMap().getCountry("Alberta");;
        country1.setOwner(p1);
        country2.setOwner(p1);
        country1.addTroops(4);
        country2.addTroops(4);
        int troop1=country1.getArmySize();
        int troop2=country2.getArmySize();
        model.pureFortify(country1,country2,4);
        assertEquals(troop1-4,country1.getArmySize());
        assertEquals(troop2+4,country2.getArmySize());

    }

}