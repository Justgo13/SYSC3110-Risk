import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RiskMockModel extends RiskModel {
    public RiskMockModel(){
        super();
    }

    @Override
    protected ArrayList<Integer> defendingDiceInitialization(int defendingArmySize) {
        if (defendingArmySize >= 2) {
            return new ArrayList<>(Arrays.asList(5,4));
        } else if (defendingArmySize == 1) {
            return new ArrayList<>(Arrays.asList(5));
        }
        else return new ArrayList<>();

    }

    @Override
    protected ArrayList<Integer> attackingDiceInitialization(int numOfAttackers) {
        if (numOfAttackers >= 3) {
            return new ArrayList<>(Arrays.asList(5,4,6));
        } else if (numOfAttackers == 2) {
            return new ArrayList<>(Arrays.asList(5,4));
        }
        return new ArrayList<>(Arrays.asList(5));
    }
}
