package abstractions.boosts;

public enum BoostType {
    Multiplier(0),
    BaseValue(1);

    private int value;
    private BoostType(int id){
        value = id;
    }
    public int getValue(){
        return value;
    }
}
