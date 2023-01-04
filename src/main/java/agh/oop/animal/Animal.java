package agh.oop.animal;

import agh.oop.AbstractMapElement;
import agh.oop.MapDirection;
import agh.oop.Vector2d;
import agh.oop.map.Earth;
import agh.oop.map.MapSize;
import agh.oop.map.WorldMap;
import agh.oop.plant.Plant;
import agh.oop.plant.Trees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import javafx.scene.image.Image;


public class Animal extends AbstractMapElement implements Comparable<Animal> {
    private final List<Integer> genome = new ArrayList<>();
    private final List<IAnimalObserver> observerList = new ArrayList<>();
    private final WorldMap map;
    private final INextGene nextGeneGenerator;
    private final IGeneMutator geneMutator;
    private final int mutationsMin;
    private final int mutationsMax;
    private int activeGene = 0;
    private int timeAlive = 0;
    private int plantsEaten = 0;
    private int kids = 0;
    public Vector2d prevPosition;

    public Animal(WorldMap map, Vector2d position, int energy, int genomeLength, int mutationsMin, int mutationsMax,
                  INextGene nextGeneGenerator, IGeneMutator geneMutator) {
        this.map = map;
        addObserver(map);
        this.position = position;
        this.prevPosition = position;
        this.energy = energy;
        this.mutationsMin = mutationsMin;
        this.mutationsMax = mutationsMax;
        this.nextGeneGenerator = nextGeneGenerator;
        this.geneMutator = geneMutator;
//        texture = new Image("animal_textures/bee.png");
        texture = new Image("waspyboi.gif");

        for (int i = 0; i < genomeLength; i++) {
            genome.add(generateGene());
        }
    }

    public Animal(WorldMap map, Vector2d position) {
        this(map, position, 100, 5, 2, 2, new NextGeneNormal(), new MutatorRandom());
    }

    protected Animal(int energy) {
        this(new WorldMap(new MapSize(10, 10), new Earth(), new Trees()), new Vector2d(0, 0), energy, 10, 0, 0, new NextGeneNormal(), new MutatorRandom());
    }

    public Animal(Animal parent1, Animal parent2, int energyFromParent) {
        this.map = parent1.map;
        this.position = parent1.position;
        this.prevPosition = position;
        this.observerList.addAll(parent1.observerList);
        this.nextGeneGenerator = parent1.nextGeneGenerator;
        this.geneMutator = parent1.geneMutator;
        this.mutationsMin = parent1.mutationsMin;
        this.mutationsMax = parent1.mutationsMax;
        texture = new Image("waspyboi.gif");

        int totalEnergy = parent1.energy + parent2.energy;
        int genomeLength = parent1.genome.size();
        int a = Math.round(((float) parent1.energy / totalEnergy) * (genomeLength));
        if (ThreadLocalRandom.current().nextBoolean()) {
            this.genome.addAll(parent1.genome.subList(0, a));
            this.genome.addAll(parent2.genome.subList(a, genomeLength));
        } else {
            this.genome.addAll(parent2.genome.subList(0, genomeLength - a));
            this.genome.addAll(parent1.genome.subList(genomeLength - a, genomeLength));
        }

        List<Integer> randomOrder =
                IntStream.rangeClosed(0, genomeLength - 1).collect(ArrayList::new, List::add, List::addAll);
        Collections.shuffle(randomOrder);
        int numberOfMutations = ThreadLocalRandom.current().nextInt(mutationsMin, mutationsMax + 1);
        for (int i = 0; i < numberOfMutations; ++i) {
            int currentGene = genome.get(randomOrder.get(i));
            genome.set(randomOrder.get(i), geneMutator.mutateGene(currentGene));
        }

        this.energy = energyFromParent * 2;
        parent1.removeEnergy(energyFromParent);
        parent2.removeEnergy(energyFromParent);
        parent1.kids++;
        parent2.kids++;
    }

    public void move(int cost) {
        this.prevPosition = position;
        timeAlive += 1;
        var newLocation = map.newLocation(this.nextPosition());
        this.setPosition(newLocation.newPosition);
        this.removeEnergy(cost + newLocation.energyCost);
        activeGene = nextGeneGenerator.NextGene(activeGene, genome.size());
        notifyObservers(ActionType.POSITION_CHANGED);
    }

    public void removeEnergy(int amount) {
        energy -= amount;
        if (energy <= 0) {
            notifyObservers(ActionType.DEATH);
        }
    }

    public void eat(Plant plant) {
        ++plantsEaten;
        addEnergy(plant.getEnergy());
    }

    public void addEnergy(int amount) {
        energy += amount;
    }

    public int getActiveGene() {
        return genome.get(activeGene);
    }

    public void getNextGene() { //could be used for more friendly animal rotation?
//        return nextGene()?
    }

    public int getNextDirection() {
        return (genome.get(activeGene));

    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }


    public int getKids() {
        return kids;
    }

    public List<Integer> getGenome() {
        return new ArrayList<>(genome);
    }

    public Vector2d nextPosition() {
        return this.getPosition().add(MapDirection.fromNumber(genome.get(activeGene)).toUnitVector());
    }

    public void addObserver(IAnimalObserver o) {
        observerList.add(o);
    }

    private void removeObserver(IAnimalObserver o) {
        observerList.remove(o);
    }

    private void notifyObservers(ActionType actionType) {
        switch (actionType) {
            case POSITION_CHANGED -> observerList.forEach(x -> x.positionChanged(this));
            case DEATH -> observerList.forEach(x -> x.death(this));
        }
    }

    protected static int generateGene() {
        return ThreadLocalRandom.current().nextInt(0, 8);
    }

//    @Override
//    public String toString() {
//        return "Animal{" +
//                "position=" + position +
//                ", energy=" + energy +
//                ", genome=" + genome +
//                ", activeGene=" + activeGene +
//                ", timeAlive=" + timeAlive +
//                ", plantsEaten=" + plantsEaten +
//                ", kids=" + kids +
//                '}';
//    }

    public void setSelected() {
        this.texture = new Image("animal_textures/bee.png");
    }

    public String stats() {
        return "Animal{" +
                "position=" + position +
                ", energy=" + energy +
                ", genome=" + genome +
                ", activeGene=" + activeGene +
                ", timeAlive=" + timeAlive +
                ", plantsEaten=" + plantsEaten +
                ", kids=" + kids +
                '}';
    }

    @Override
    public String toString() {
        return String.valueOf(genome.get(activeGene));
    }

    @Override
    public int compareTo(Animal o) {
        return Comparator.comparing(Animal::getEnergy)
                .thenComparing(Animal::getTimeAlive)
                .thenComparing(Animal::getKids)
                .compare(this, o);
    }
}
