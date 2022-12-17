package agh.oop.animal;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    @Test
    void reproduce() {
        Animal parent1 = new Animal(10);
        Animal parent2 = new Animal(100);
        Animal kid = new Animal(parent1,parent2,5);

        System.out.println(parent1.info());
        System.out.println(parent2.info());
        System.out.println(kid.info());

        assertTrue( checkGenome(kid.getGenome(),parent1.getGenome(),parent2.getGenome(),1) );


    }

    @Test
    void reproduceEqualEnergy() {
        Animal parent1 = new Animal(50);
        Animal parent2 = new Animal(50);
        Animal kid = new Animal(parent1,parent2,20);
        System.out.println(parent1.info());
        System.out.println(parent2.info());
        System.out.println(kid.info());


        assertTrue( checkGenome(kid.getGenome(),parent1.getGenome(),parent2.getGenome(),parent1.getGenome().size()/2) );


    }

    boolean checkGenome(List<Integer> genomeResult, List<Integer> genome1, List<Integer> genome2, int division) {
        var r1 = genome1.subList(0,division);
        r1.addAll(genome2.subList(division,genome2.size()));
        var r2 = genome2.subList(0,genome2.size()-division);
        r2.addAll(genome1.subList(genome1.size()-division,genome1.size()));

        System.out.println(r1);
        System.out.println(r2);

        return genomeResult.equals( r1 ) || genomeResult.equals(r2);
    }
}