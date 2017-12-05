package model;

import abstractions.workers.AbstractWorker;

public class Worker extends AbstractWorker
{
    public Worker(int id, String name, int level, double baseCost, double baseUnitsPerSec) {
        super(id, name, level, baseCost, baseUnitsPerSec);
    }
}
