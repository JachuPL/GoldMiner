package model;

import abstractions.workers.AbstractWorker;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class Worker extends AbstractWorker
{
    public Worker(int id, String name, int level, double baseCost, double baseUnitsPerSec, Image image, Media sound) {
        super(id, name, level, baseCost, baseUnitsPerSec, image, sound);
    }
}
