package model;

import abstractions.boosts.AbstractBoost;
import abstractions.boosts.BoostCategory;
import abstractions.boosts.BoostType;

public class Boost extends AbstractBoost {
    public Boost(int id, BoostCategory category, BoostType type, double value, long duration) {
        super(id, category, type, value, duration);
    }
}
