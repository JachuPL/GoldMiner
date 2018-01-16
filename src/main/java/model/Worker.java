package model;

import abstractions.workers.AbstractWorker;
import javafx.scene.image.Image;

public class Worker extends AbstractWorker
{
    public Worker(int id, String name, int level, double baseCost, double baseUnitsPerSec, Image image) {
        super(id, name, level, baseCost, baseUnitsPerSec, image);
    }
}
