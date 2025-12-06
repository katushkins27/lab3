package models.interfaces;

import models.locations.Location;

public interface Sleeper {
    void fallAsleep();
    void wakeUp();
    boolean maybeSleep(Location location);
}
