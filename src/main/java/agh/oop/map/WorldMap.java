package agh.oop.map;

import agh.oop.IWorldMap;
import agh.oop.Vector2d;
import agh.oop.animal.Animal;
import agh.oop.animal.IAnimalObserver;
import agh.oop.animal.IGeneMutator;
import agh.oop.animal.INextGene;
import agh.oop.plant.IPlantType;
import agh.oop.plant.Plant;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WorldMap implements IWorldMap, IAnimalObserver, IDayCycle {
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final List<Animal> corpses = new ArrayList<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final List<Plant> plants = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final IPlantType plantType;
    private final IMapType mapType;
    private final MapSize size;
    private final HashMap<Vector2d, Integer> deadAnimalsPerPosition = new HashMap<Vector2d, Integer>();

    public WorldMap(MapSize size, IMapType mapType, IPlantType plantType) {
        this.plantType = plantType;
        this.mapType = mapType;
        this.size = size;
        for (int i = 0; i < size.getWidth(); i++) {
            for (int j = 0; j < size.getHeight(); j++) {
//                deadAnimalsPerPosition.put(new Vector2d(i,j), ThreadLocalRandom.current().nextInt(0, 20));
                deadAnimalsPerPosition.put(new Vector2d(i, j), 0);
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
        animals.values().forEach(animalList::addAll);
        return animalList;
    }

    public List<Plant> getPlants() {
        return plants;
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
        Vector2d oldLocation = animal.getPosition();
        Vector2d newLocation = this.newLocation(animal.nextPosition()).newPosition;
        removeAnimal(animal);
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
        removeAnimal(animal);
    }

    private void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
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
        for (int i = 0; i < amount; i++) {
            //TODO:make this plant 80% on fertile 20% non fertile
            //TODO:when no fertile is available plant on non fertile
            //TODO:when occupied plant on different position
            Vector2d positon = plantType.getFertileField(this);
            if (positon != null) {
                Plant plant = new Plant(positon, energy, this.plantType);
                plants.add(plant);
            }
        }

    }

    private void createPlantAt(Vector2d position) {
        Plant plant = new Plant(position, 5, this.plantType);
        plants.add(plant);
    }

    private void removePlantAt(Vector2d position) {
        for (Plant plant : plants) {
            if (plant.getPosition().equals(position)) {
                plants.remove(plant);
                break;
            }
        }
    }

    private Plant plantAt(Vector2d position) {
        for (Plant plant : plants) {
            if (position.equals(plant.getPosition())) {
                return plant;
            }
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (animals.containsKey(position)) {
            return true;
        }

        for (Plant plant : plants) {
            if (position.equals(plant.getPosition())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) { // if somwhere else is necessary to have animals checked first then new method should be mabe
        for (Plant plant : plants) {
            if (position.equals(plant.getPosition())) {
                return  plant;
            }
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


    public int getAutosomalDominant() {
        Integer[] geneCount = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Animal animal : getAnimals()) {
            geneCount[animal.getActiveGene()]++;
        }
        Integer max =Arrays.stream(geneCount) .max(Integer::compare).get();
        return Arrays.asList(geneCount).indexOf(max);
    }

    public int getTopGeneFromAllGenomes() {
        int[] geneCount = {0, 0, 0, 0, 0, 0, 0, 0};
        getAnimals().forEach(a -> a.getGenome().forEach(x -> geneCount[x]++));
        int maxG = 0;
        for (int i = 0; i < geneCount.length; ++i) {
            if (geneCount[i] > geneCount[maxG]) {
                maxG = i;
            }
        }
        return maxG;
    }

    public long getAverageEnergy() {
        double energy = 0.0;
        for (Animal animal : getAnimals()) {
            energy += animal.getEnergy();
        }

        return Math.round(energy / getAnimals().size());
    }

    public long getAverageLifespan() {
        double lifespan = 0.0;
        for (Animal animal : getAnimals()) {
            lifespan += animal.getEnergy();
        }

        return Math.round(lifespan / getAnimals().size());

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
    public void moveAllAnimals() {
        getAnimals().forEach(Animal::move);
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
    public void regrowPlants(int totalPlants, int energy) {
        createNPlants(totalPlants-plants.size() ,energy);
    }
}
