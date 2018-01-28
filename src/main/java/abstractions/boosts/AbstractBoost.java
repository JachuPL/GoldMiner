package abstractions.boosts;
import abstractions.Entity;
import java.time.Duration;

public abstract class AbstractBoost extends Entity {
    protected BoostCategory _category;
    protected BoostType _type;
    protected double _value;
    protected long _duration;
    private long _started;

    public double Value() { return _value; }
    public BoostCategory Category() { return _category; }
    public BoostType Type() { return _type; }

    public AbstractBoost(int id, BoostCategory category, BoostType type, double value, long duration) {
        super(id);
        ValidateCategory(category);
        _category = category;
        ValidateType(type);
        _type = type;
        ValidateValue(value);
        _value = value;
        ValidateDuration(duration);
        _duration = duration;

        _started = System.currentTimeMillis();
    }

    private void ValidateDuration(long duration) {
        // TODO: no restrictions so far
    }

    private void ValidateValue(double value) {
        // TODO: no restrictions so far
    }

    private void ValidateType(BoostType type) {
        switch(type)
        {
            case Multiplier:
            case BaseValue:
                break;
            default:
                throw new IllegalArgumentException("type out of range");
        }
    }

    private void ValidateCategory(BoostCategory category) {

        switch (category) {
            case Click:
            case Timed:
            case Constant:
                break;
            default:
                throw new IllegalArgumentException("boost category out of range");
        }
    }

    public boolean shouldExpire() {
        return _category == BoostCategory.Timed && System.currentTimeMillis() > (_started + _duration);
    }

    @Override
    public String toString() {
        String categoryString = "";
        switch(_category){
            case Timed:
                categoryString = "czasowo";
                break;
            case Click:
                categoryString = "przy kliknięciu";
                break;
            case Constant:
                categoryString = "na stałe";
        }

        String typeString = "";
        switch(_type){
            case Multiplier:
                typeString = "mnożnik";
                break;
            case BaseValue:
                typeString = "wartość podstawowa";
                break;
        }

        String completeString = typeString + " " + ((_value > 0) ? "+" : "-") + _value + " " + categoryString;

        Duration duration = Duration.ofMillis(_duration);
        long s = duration.getSeconds();

        if (_category == BoostCategory.Timed)
            completeString += String.format(" przez następne %02d:%02d", (s%3600)/60, s%60);

        return completeString;
    }

    public abstract AbstractBoost clone();
}
