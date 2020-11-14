public enum DiceConstant {
    A1("attack1"), A2("attack2"), A3("attack3"), D1("defend1"), D2("defend2"), ALABEL("Attacking Dice"), DLABEL("Defending Dice");
    String value;
    DiceConstant(String value) {
        this.value = value;
    }
    public String toString() {
        return value;
    }
}
