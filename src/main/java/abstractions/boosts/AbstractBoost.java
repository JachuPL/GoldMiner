package abstractions.boosts;

import abstractions.Entity;
import abstractions.workers.AbstractWorker;

import java.util.Date;

public abstract class AbstractBoost extends Entity {
    private BoostCategory _category = BoostCategory.Click;
    private BoostType _type = BoostType.Multiplier;
    private double _value;
    private long _duration;
    private long _started;

    public double Value() { return _value; }
    public BoostCategory Category() { return _category; }
    public BoostType Type() { return _type; }
    public long Duration() { return _duration; }
    public long Started() { return _started; }


    public AbstractBoost(int id, BoostCategory category, BoostType type, double value, long duration) {
        super(id);
        ValidateCategory(category);
        _category = category;
        ValidateType(type);
        _type = type;
        ValidateValue(value);
        _value = value;
        _started = System.currentTimeMillis();
        ValidateDuration(duration);
        _duration = duration;
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

    public boolean shouldExpire(){
        if (_category == BoostCategory.Timed)
            return System.currentTimeMillis() > (_started + _duration);

        return false;
    }
}
