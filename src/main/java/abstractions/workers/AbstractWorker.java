package abstractions.workers;

import abstractions.Entity;
import abstractions.actionPossibility.*;
import abstractions.boosts.AbstractBoost;
import abstractions.boosts.BoostCategory;
import abstractions.boosts.BoostType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWorker extends Entity {
    private String _name;
    private int _level;
    private double _baseCost;
    private double _baseUnitsPerSec;
    private LinkedHashSet<AbstractBoost> _boosts;
    private double _unitsPerSecMultiplier;
    private double _costMultiplier;
    private Image _image;
    private MediaPlayer _sound;

    public String getName() { return _name; }
    public int getLevel() { return _level; }
    private double getUnitsPerSec() {
        double result = 0;
        if (_level > 0)
            result = _baseUnitsPerSec * Math.pow(1.67, _level - 1);
        return result;
    }

    public double getPrice() { return _baseCost * _costMultiplier * Math.pow(1.15, _level); }
    public Image getImage() { return _image; }

    public AbstractWorker(int id, String name, int level, double baseCost, double baseUnitsPerSec, Image image, Media sound)
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
        ValidateSound(sound);
        _sound = new MediaPlayer(sound);

        _boosts = new LinkedHashSet<>();
        _costMultiplier = 1;
        _unitsPerSecMultiplier = 1;
    }

    private void ValidateSound(Media sound) {
        if (sound == null)
            throw new IllegalArgumentException("Sound cannot be null");
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

    public double getBoostedUnitsPerSecForNonClicking() {
        List<AbstractBoost> constantOrTimedBoosts = _boosts.stream().filter(x -> x.Category() != BoostCategory.Click).collect(Collectors.toList());
        return getUnitsPerSec() +  constantOrTimedBoosts.stream().filter(x -> x.Type() == BoostType.BaseValue).mapToDouble(AbstractBoost::Value).sum();
    }

    public double getBoostedUnitsPerSecMultiplierForNonClicking() {
        List<AbstractBoost> constantOrTimedBoosts = _boosts.stream().filter(x -> x.Category() != BoostCategory.Click).collect(Collectors.toList());
        return _unitsPerSecMultiplier + constantOrTimedBoosts.stream().filter(x -> x.Type() == BoostType.Multiplier).mapToDouble(AbstractBoost::Value).sum();
    }

    public double harvest(){
        return getBoostedUnitsPerSecForNonClicking() * getBoostedUnitsPerSecMultiplierForNonClicking();
    }

    public double getBoostedUnitsPerSecForClicking(){
        List<AbstractBoost> clickingBoosts = _boosts.stream().filter(x -> x.Category() == BoostCategory.Click).collect(Collectors.toList());
        return getUnitsPerSec() +  clickingBoosts.stream().filter(x -> x.Type() == BoostType.BaseValue).mapToDouble(AbstractBoost::Value).sum();
    }

    public double getBoostedUnitsPerSecMultiplierForClicking(){
        List<AbstractBoost> clickingBoosts = _boosts.stream().filter(x -> x.Category() == BoostCategory.Click).collect(Collectors.toList());
        return _unitsPerSecMultiplier + clickingBoosts.stream().filter(x -> x.Type() == BoostType.Multiplier).mapToDouble(AbstractBoost::Value).sum();
    }

    public double harvestOnClick() {
        return getBoostedUnitsPerSecForClicking() * getBoostedUnitsPerSecMultiplierForClicking();
    }

    public void expireBoosts() {
        _boosts.removeIf(AbstractBoost::shouldExpire);
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

    public void playSound() {
        _sound.play();
        _sound.seek(_sound.getStartTime());
    }
}
