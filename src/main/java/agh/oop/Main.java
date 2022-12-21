package agh.oop;

import agh.oop.animal.Animal;
import agh.oop.map.Earth;
import agh.oop.map.MapSize;
import agh.oop.map.WorldMap;
import agh.oop.map.MapVisualizer;
import agh.oop.plant.Toxic;
import agh.oop.plant.Trees;

public class Main {
    public static void main(String[] args) {
        SimulationEngine engine = new SimulationEngine(new MapSize(10,10), new Earth(), new Toxic(), 10, 40);
        engine.run();
    }
}