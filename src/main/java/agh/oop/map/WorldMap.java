package agh.oop.map;

import agh.oop.IWorldMap;
import agh.oop.Vector2d;
import agh.oop.animal.Animal;
import agh.oop.animal.IAnimalObserver;
import agh.oop.animal.IGeneMutator;
import agh.oop.animal.INextGene;
import agh.oop.plant.IPlantType;
import agh.oop.plant.Plant;

import java.io.Console;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class WorldMap implements IWorldMap, IAnimalObserver, IDayCycle, IMapRefreshObserver {
    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();
    private final List<Animal> corpses = new ArrayList<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private int days = 0;
    IPlantType plantType;
    IMapType mapType;
    MapSize size;
    HashMap<Vector2d, Integer> deadAnimalsPerPosition = new HashMap<Vector2d, Integer>();

    public WorldMap(MapSize size, IMapType mapType, IPlantType plantType) {
        this.plantType = plantType;
        this.mapType = mapType;
        this.size = size;
        plantType.calculateFertileArea(this);
        for (int i = 0; i < size.getWidth(); i++) {
            for (int j = 0; j < size.getHeight(); j++) {
                deadAnimalsPerPosition.put(new Vector2d(i, j), ThreadLocalRandom.current().nextInt(0, 20));
            }
        }
    }

    private Vector2d generateRandomPosition() {
        return new Vector2d(ThreadLocalRandom.current().nextInt(0, size.getWidth()), ThreadLocalRandom.current().nextInt(0, size.getHeight()));
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public List<Animal> getAnimals() {
        List<Animal> animalList = new ArrayList<>();
//        animals.values().forEach(animalList::addAll);
        for( var I : animals.values() ) {
            animalList.addAll(I);
        }
        System.out.println(animalList);
        return animalList;
    }

    public List<Plant> getPlants() {
//        System.out.println(plants.size());
        return new ArrayList<>(plants.values());
    }

    public int getDays() {
        return days;
    }

    public HashMap<Vector2d, Integer> getdeadAnimalsPerPosition() {
        return deadAnimalsPerPosition;
    }

    @Override
    public String toString() {
        return visualizer.draw(new Vector2d(0, 0), new Vector2d(size.getWidth(), size.getHeight()));
    }

    @Override
    public void positionChanged(Animal animal) {
            Vector2d oldLocation = animal.prevPosition;
            Vector2d newLocation = animal.getPosition();
            removeAnimal(oldLocation, animal);
            addAnimal(newLocation, animal);
    }

    @Override
    public void death(Animal animal) {
        corpses.add(animal);
    }
    private void cleanCorpse(Animal animal) {
        deadAnimals.add(animal);
        Integer num = deadAnimalsPerPosition.getOrDefault(animal.getPosition(), 0);
        deadAnimalsPerPosition.put(animal.getPosition(), num + 1);
        removeAnimal(animal.getPosition(), animal);
    }

    private void removeAnimal(Vector2d position, Animal animal) {
        if (animals.containsKey(position)) {
            if (animals.get(position).size() <= 1) {
                animals.remove(position);
            } else {
                animals.get(position).remove(animal);
            }
        }
    }

    private void addAnimal(Animal animal) {
            animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
    }

     private void addAnimal(Vector2d position, Animal animal) {

             animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);

    }


    public void createNAnimals(int amount, int energy, int genomeLength, int mutationsMin, int mutationsMax, INextGene nextGeneGenerator, IGeneMutator geneMutator) {
        for (int i = 0; i < amount; i++) {
            Vector2d position = generateRandomPosition();
            while (!(objectAt(position) instanceof Animal)) {
                position = generateRandomPosition();
                Animal animal = new Animal(this, position, energy, genomeLength, mutationsMin, mutationsMax, nextGeneGenerator, geneMutator);
                addAnimal(position, animal);
            }
        }
    }

    private void createAnimalAt(Vector2d position) {
        Animal animal = new Animal(this, position);
        addAnimal(animal);
    }

    public void createNPlants(int amount, int energy) {
        var randomList = new ArrayList<Vector2d>();
        for (int x = 0; x < this.getSize().getWidth(); ++x) {
            for (int y = 0; y < this.getSize().getHeight(); ++y) {
                randomList.add(new Vector2d(x,y));
            }
        }
        Collections.shuffle(randomList);
        for( var pos : randomList ) {
            if ( plantAt(pos) == null &&
                    ThreadLocalRandom.current().nextInt(0, 101) <= plantType.getFertileField(pos) ) {
                createPlantAt(pos,energy);
                if( --amount == 0 ) {
                    break;
                }
            }
        }
        for( var pos : randomList ) {
            if ( plantAt(pos) == null ) {
                if( --amount < 0 ) {
                    break;
                }
                createPlantAt(pos,energy);
            }
        }

    }

    private void createPlantAt(Vector2d position, int energy) {
        Plant plant = new Plant(this, position, energy, this.plantType);
        plants.put(position,plant);
    }

    private void removePlantAt(Vector2d position) {
        plants.remove(position);
    }

    private Plant plantAt(Vector2d position) {
        return plants.get(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (animals.containsKey(position)) {
            return true;
        }

        if (plants.containsKey(position)) {
            return true;
        }

        return false;
    }

    @Override
    public Object objectAt(Vector2d position) { // if somwhere else is necessary to have animals checked first then new method should be mabe
        if (plants.containsKey(position)) {
            return plants.get(position);
        }
        if (animals.containsKey(position)) {
            return animals.get(position).get(0);
        }

        return new Object();
    }

    private List<Animal> getAnimalsAt(Vector2d position) {

            List<Animal> animalsList = animals.get(position);
            Collections.sort(animalsList);
            Collections.reverse(animalsList);
            return animalsList;
    }

    @Override
    public MapSize getSize() {
        return size;
    }


    public int getTopGeneFromAllGenomes() {
        Integer[] geneCount = {0, 0, 0, 0, 0, 0, 0, 0};
        getAnimals().forEach(a -> a.getGenome().forEach(x -> geneCount[x]++));
        int maxG = 0;
        for (int i = 0; i < geneCount.length; ++i) {
            if (geneCount[i] > geneCount[maxG]) {
                maxG = i;
            }
        }
        Integer max = Arrays.stream(geneCount).max(Integer::compare).get();
        return Arrays.asList(geneCount).indexOf(max);
    }

    public long getAverageEnergy() {
        double energy = 0.0;
        var animals = getAnimals();
        for (Animal animal : animals) {
            energy += animal.getEnergy();
        }
        return Math.round(energy / animals.size());
    }

    public int getFreeSpace() {
        HashMap<Vector2d, Integer> occupied = new HashMap<Vector2d, Integer>();

        for (Vector2d position : plants.keySet()) {
            occupied.put(position, 0);
        }
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            occupied.put(entry.getKey(), 0);
        }
        return size.getWidth() * size.getHeight() - occupied.size();

    }

    public double getAverageLifespan() {
        double lifespan = 0.0;
        for (Animal animal : getDeadAnimals()) {
            lifespan += animal.getTimeAlive();
            System.out.println(animal.getTimeAlive());
        }
        System.out.println(lifespan + " " + getDeadAnimals().size());
        return Math.round(lifespan / getDeadAnimals().size() * 10) / 10.0;

    }


    public ChangePosition newLocation(Vector2d location) {
        return mapType.newLocation(this.size, location);
    }

    @Override
    public void cleanCorpses() {
        for ( Animal corpse : corpses ) {
            cleanCorpse(corpse);
        }
        corpses.clear();
    }

    @Override
    public void moveAllAnimals(int moveCost) {
        getAnimals().forEach( a -> a.move(moveCost));
        days += 1;
    }

    @Override
    public void consumePlants() {
        for (var position : animals.keySet()) {
            Plant plant = plantAt(position);
            if (plant == null) {
                continue;
            }
            getAnimalsAt(position).get(0).eat(plant);
            removePlantAt(position);
        }
    }

    @Override
    public void reproduce(int energyThreshold, int energyInheritedFromParent) {
        for (var currAnimals : animals.values()) {
            if (currAnimals.size() >= 2) {
                Collections.sort(currAnimals);
                Collections.reverse(currAnimals);
                Animal parent1 = currAnimals.get(0);
                Animal parent2 = currAnimals.get(1);
                if (parent2.getEnergy() >= energyThreshold) {
                    addAnimal(new Animal(parent1, parent2, energyInheritedFromParent));
                }
            }
        }
    }

    @Override
    public void regrowPlants(int amount, int energy) {
        createNPlants(amount, energy);
    }


}
