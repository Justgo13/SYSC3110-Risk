import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;

public class DiceEvent extends EventObject {

    private ArrayList<Integer> attackerDice;
    private ArrayList<Integer> defenderDice;
    /**
     *
     * @param riskModel
     * @param attackerDice
     * @param defenderDice
     */

    public DiceEvent(RiskModel riskModel, ArrayList<Integer> attackerDice, ArrayList<Integer> defenderDice) {
        super(riskModel);
        this.attackerDice=attackerDice;
        this.defenderDice=defenderDice;
    }



    public int getAttackerMaxRoll(){
        return Collections.max(attackerDice);
    }

    public int getDefenderMaxRoll(){
        return Collections.max(defenderDice);
    }

    public ArrayList<Integer> getAttackerDice() {
        return attackerDice;
    }

    public ArrayList<Integer> getDefenderDice() {
        return defenderDice;
    }
}
