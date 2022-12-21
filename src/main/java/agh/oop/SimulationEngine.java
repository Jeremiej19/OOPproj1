package agh.oop;

import agh.oop.animal.*;
import agh.oop.map.*;
import agh.oop.plant.IPlantType;

import java.util.List;

public class SimulationEngine {
    private MapSize mapSize;
    private  IMapType mapType;
    private IPlantType plantType;
    int startingAnimals;
    int startingPlants;
    private final int energyNeededForReproduction = 30;
    private final int energyInheritedFromParent = 20;

    IGeneMutator geneMutator;
    INextGene nextGene;
    WorldMap map;
    MapVisualizer mapVisualizer;

    public SimulationEngine(MapSize mapSize, IMapType mapType, IPlantType plantType, int startingAnimals, int startingPlants) {
        this.mapSize = mapSize;
        this.mapType = mapType;
        this.plantType = plantType;
        this.startingPlants = startingPlants;
        this.startingAnimals = startingAnimals;
        this.map = new WorldMap(mapSize, mapType, plantType);
        this.mapVisualizer = new MapVisualizer(map);

    }

    public void run() {
        int plantsEnergy = 5;
        int animalsStartingEnergy = 100;
        map.createNAnimals(startingAnimals, animalsStartingEnergy, 5, 2, 2, new NextGeneNormal(), new MutatorRandom());
        map.createNPlants(startingPlants, plantsEnergy);
        for(int i=0; i<1000; i++){
            map.cleanCorpses();
            map.moveAllAnimals();
            map.consumePlants();
            map.reproduce(energyNeededForReproduction,energyInheritedFromParent);
            System.out.println(map.getAnimals().size());
            System.out.println(mapVisualizer.draw(
                    new Vector2d(0, 0), new Vector2d(map.getSize().getHeight(), map.getSize().getWidth())));
            map.regrowPlants(startingPlants,plantsEnergy);

        }
        System.out.println(map.getTopGeneFromAllGenomes());


    }
}
