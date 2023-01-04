package agh.oop;

import agh.oop.GUI.App;
import agh.oop.animal.*;
import agh.oop.map.*;
import agh.oop.plant.IPlantType;
import agh.oop.plant.Plant;
import javafx.application.Application;

import java.util.List;

public class SimulationEngine implements Runnable {
    private final int energyLostPerCycle;
    private final int grasPerCycle;
    private final int energyFromGrass;
    private final int energyNeededForReproduction;
    private final int energyInheritedFromParent = 20;

    public WorldMap map;
    int i = 0;
    public boolean go = false;
    MapVisualizer mapVisualizer;
    private IMapRefreshObserver observer;

    public SimulationEngine(MapSize mapSize, IGeneMutator gene, INextGene next, IMapType mapType, IPlantType plantType, int startingAnimals, int startingPlants, int animalStartEnergy, int energyFromGrass, int energyToReproduce, int grasPerCycle, int energyLostPerCycle) {
        this.energyNeededForReproduction = energyToReproduce;
        this.energyLostPerCycle = energyLostPerCycle;
        this.map = new WorldMap(mapSize, mapType, plantType);
        this.mapVisualizer = new MapVisualizer(map);
        this.energyFromGrass = energyFromGrass;
        this.grasPerCycle = grasPerCycle;
        map.createNAnimals(startingAnimals, animalStartEnergy, 10, 2, 2, next, gene);
        map.createNPlants(startingPlants, energyFromGrass);
        run();

    }

    public void addObserverMap(IMapRefreshObserver o) {
        observer = o;
    }

    public List<Plant> getplants() {
        return map.getPlants();
    }

    public List<Animal> getanimals() {
        return map.getAnimals();
    }

    public void gate() {
        go = !go;
    }

    public void run() {
        System.out.println("lmoa");
        System.out.println(go);
        try {
            Thread.sleep(500);
            System.out.println("tun");

        } catch (InterruptedException e) {
            System.out.println(e);
        }
        while (go && map.getAnimals().size() > 0) {
            System.out.println(go);

            try {
                Thread.sleep(500);
                //FIXME: add grass growth per cycle to cycle and energyLostPerCycle
                System.out.println(i);
                i++;
                map.cycle(energyNeededForReproduction, energyInheritedFromParent, grasPerCycle, energyFromGrass, energyLostPerCycle, observer);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }


}
