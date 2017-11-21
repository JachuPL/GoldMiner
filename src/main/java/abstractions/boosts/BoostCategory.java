package abstractions.boosts;

public enum BoostCategory {
    Timed(0),
    Constant(1),
    Click(2);

    private int value;
    private BoostCategory(int value) {
        this.value = value;
    }

    public int getValue() { return value; }

}
