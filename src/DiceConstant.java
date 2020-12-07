/**
 * Enum for setting up dice labels and constants
 * @author Harjap
 */
public enum DiceConstant {
    ATTACK_DICE_LABEL("Attacking Dice"), DEFEND_DICE_LABEL("Defending Dice");
    String value;
    DiceConstant(String value) {
        this.value = value;
    }
    public String toString() {
        return value;
    }
}
