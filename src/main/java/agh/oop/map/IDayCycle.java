package agh.oop.map;

public interface IDayCycle {
    void cleanCorpses();

    void moveAllAnimals();

    void consumePlants();

    public void reproduce(int energyThreshold, int energyInheritedFromParent);

    void regrowPlants(int totalPlants, int energy);

}
