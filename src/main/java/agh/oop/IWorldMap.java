package agh.oop;

import agh.oop.animal.Animal;
import agh.oop.map.MapSize;

public interface IWorldMap {

    boolean place(Animal animal);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
    MapSize getSize();

}