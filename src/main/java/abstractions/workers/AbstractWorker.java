package abstractions.workers;

import abstractions.Entity;
import abstractions.actionPossibility.*;
import abstractions.boosts.AbstractBoost;
import abstractions.boosts.BoostCategory;
import abstractions.boosts.BoostType;
import javafx.scene.image.Image;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWorker extends Entity {
    private String _name;
    private int _level;
    protected double _baseCost;
    protected double _baseUnitsPerSec;
    protected LinkedHashSet<AbstractBoost> _boosts;
    protected double _unitsPerSecMultiplier;
    protected double _costMultiplier;
    private String _desc;
    private Image _image;

    public String getName() { return _name; }
    public int getLevel() { return _level; }
    public LinkedHashSet<AbstractBoost> getBoosts() { return _boosts; }
    public double getUnitsPerSec() {
        double result = 0;
        if (_level > 0)
            result = _baseUnitsPerSec * Math.pow(1.67, _level - 1);
        return result;
    }

    public double getUnitsPerSecMultiplier() { return _unitsPerSecMultiplier; }
    public double getCostMultiplier() { return _costMultiplier; }
    public double getPrice() { return _baseCost * getCostMultiplier() * Math.pow(1.15, _level); }
    public Image getImage() { return _image; }
    public AbstractWorker(int id, String name, int level, double baseCost, double baseUnitsPerSec, Image image)
    {
        super(id);
        ValidateName(name);
        _name = name;
        ValidateLevel(level);
        _level = level;
        ValidateBaseCost(baseCost);
        _baseCost = baseCost;
        ValidateUnitsPerSec(baseUnitsPerSec);
        _baseUnitsPerSec = baseUnitsPerSec;
        ValidateImage(image);
        _image = image;

        _boosts = new LinkedHashSet<AbstractBoost>();
        _costMultiplier = 1;
        _unitsPerSecMultiplier = 1;
    }

    private void ValidateImage(Image image)
    {
        if (image == null)
            throw new IllegalArgumentException("Image cannot be null");
    }

    private void ValidateUnitsPerSec(double baseUnitsPerSec) {
        if (baseUnitsPerSec < 0)
            throw new IllegalArgumentException("base units cannot be lower than zero");
    }

    private void ValidateBaseCost(double baseCost) {
        if (baseCost < 0)
            throw new IllegalArgumentException("base price cannot be lower than zero");
    }

    private void ValidateLevel(int level) {
        if (level < 0)
            throw new IllegalArgumentException("level cannot be lower than zero");
    }

    private void ValidateName(String name) {
        if ("".equals(name))
            throw new IllegalArgumentException("name is null");
    }

    public void upgrade (){
        _level++;
    }

    public double harvest(){
        List<AbstractBoost> constantOrTimedBoosts = _boosts.stream().filter(x -> x.Category() == BoostCategory.Constant || x.Category() == BoostCategory.Timed).collect(Collectors.toList());
        double boostedUnitsPerSec = getUnitsPerSec() +  constantOrTimedBoosts.stream().filter(x -> x.Type() == BoostType.Multiplier).mapToDouble(boost -> boost.Value()).sum();
        double boostedUnitsPerSecMultiplier = getUnitsPerSecMultiplier() + constantOrTimedBoosts.stream().filter(x -> x.Type() == BoostType.Multiplier).mapToDouble(boost -> boost.Value()).sum();

        return boostedUnitsPerSec * boostedUnitsPerSecMultiplier;
    }

    public double harvestOnClick() {
        List<AbstractBoost> clickingBoosts = _boosts.stream().filter(x -> x.Category() == BoostCategory.Click).collect(Collectors.toList());
        double boostedClickValue = getUnitsPerSec() +  clickingBoosts.stream().filter(x -> x.Type() == BoostType.BaseValue).mapToDouble(boost -> boost.Value()).sum();
        double boostedClickValueMultiplier = getUnitsPerSecMultiplier() + clickingBoosts.stream().filter(x -> x.Type() == BoostType.Multiplier).mapToDouble(boost -> boost.Value()).sum();

        return boostedClickValue * boostedClickValueMultiplier;
    }

    public void expireBoosts() {
        _boosts.removeAll(_boosts.stream().filter(x -> x.shouldExpire()).collect(Collectors.toList()));
    }

    private IActionPossible canAddBoost(AbstractBoost boost)
    {
        if (boost == null)
            return new ActionImpossible("Added boost is null");

        if (_boosts.contains(boost))
            return new ActionImpossible("Boost already added");

        return new ActionPossible();
    }

    public void addBoost(AbstractBoost boost)
    {
        IActionPossible canAdd = canAddBoost(boost);
        if (!canAdd.isPossible())
            throw new IllegalArgumentException(canAdd.reason());

        _boosts.add(boost);
    }
}
