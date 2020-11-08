import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Xiling
 */

public class Controller implements ActionListener {


    private Model model;
    private Command command;
    private ArrayList<String> commandStr;
    public Controller(Model model){
        this.model = model;
        commandStr = new ArrayList<>();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("pass")){
            command = new Command("pass",null,null);
            model.processCommand(command);

        }else if(e.getActionCommand().equals("Attack")){
//            System.out.println(e.getActionCommand());
            commandStr.add(0,"attack");

        }else{

            if((!commandStr.isEmpty())&&commandStr.get(0).equals("attack")&&commandStr.size()<2){
                commandStr.add(e.getActionCommand());
            }else if((!commandStr.isEmpty())&&commandStr.get(0).equals("attack")&&commandStr.size()==2){
                commandStr.add(e.getActionCommand());
                command = new Command("attack",commandStr.get(2),commandStr.get(1));//after clicking attack, click your country first and then click the country you desired to attack
                model.processCommand(command);
                commandStr.clear();
            }else{
                System.out.println("where"+e.getActionCommand());
                model.showInfo(e.getActionCommand());
            }
        }
    }
}
