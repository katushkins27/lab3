package models.interfaces;

import models.exceptions.MovementException;
import models.locations.Location;

public interface Mover {
    void moveTo(Location location) throws MovementException;
}
