import java.util.EventObject;

public class BonusTroopEvent extends EventObject {
    private Country bonusCountry;
    private int remainingTroops;
    private RiskModel model;
    /**
     * Constructs a prototypical Event.
     *
     * @param model the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public BonusTroopEvent(RiskModel model, Country bonusCountry, int remainingTroops) {
        super(model);
        this.model = model;
        this.bonusCountry = bonusCountry;
        this.remainingTroops = remainingTroops;
    }

    public Country getBonusCountry() {
        return bonusCountry;
    }

    public int getRemainingTroops() {
        return remainingTroops;
    }

    public RiskModel getModel() {
        return model;
    }
}
