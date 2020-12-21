package agh.cs.project1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement {
    private final WorldMap map;
    private Vector2d position;
    private MapDirection orientation;
    private final int startEnergy;
    private int energy;
    private final Genes genes;
    private int numberOfChildren;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(WorldMap map, Vector2d initialPosition, MapDirection initialOrientation, int startEnergy, Genes genes) {
        this.map = map;
        this.position = initialPosition;
        this.orientation = initialOrientation;
        this.startEnergy = startEnergy;
        this.energy = startEnergy;
        this.genes = genes;
        this.numberOfChildren = 0;
    }

    public Animal(WorldMap map, Vector2d initialPosition, MapDirection initialOrientation, int startEnergy, int energy, Genes genes) {
        this.map = map;
        this.position = initialPosition;
        this.orientation = initialOrientation;
        this.startEnergy = startEnergy;
        this.energy = energy;
        this.genes = genes;
        this.numberOfChildren = 0;
    }

    @Override
    public String toString() {
        return isDead() ? "X" : orientation.toString();
    }

    public Color toColor() {
        if (isDead()) return new Color(31, 29, 29);
        if (energy < 0.2 * startEnergy) return new Color(56, 31, 17);
        if (energy < 0.4 * startEnergy) return new Color(78, 45, 39);
        if (energy < 0.6 * startEnergy) return new Color(99, 56, 50);
        if (energy < 0.8 * startEnergy) return new Color(123, 66, 60);
        if (energy < startEnergy) return new Color(139, 81, 72);
        if (energy < 2 * startEnergy) return new Color(158, 92, 82);
        if (energy < 4 * startEnergy) return new Color(191, 117, 103);
        if (energy < 6 * startEnergy) return new Color(205, 133, 119);
        if (energy < 8 * startEnergy) return new Color(213, 142, 130);
        if (energy < 10 * startEnergy) return new Color(196, 140, 134);
        return new Color(234, 179, 173);
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) observer.positionChanged(this, oldPosition, newPosition);
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genes getGenes() {
        return genes;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void changeEnergy(int value) {
        energy += value;
        if (energy < 0) energy = 0;
    }

    public void move(MoveDirection direction) {
        switch (direction) {
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> {
                Vector2d newPosition = map.getPositionOnTheMap(position.add(orientation.toUnitVector()));
                if (map.canMoveTo(newPosition)) {
                    positionChanged(position, newPosition);
                    position = newPosition;
                }
            }
            case BACKWARD -> {
                Vector2d newPosition = map.getPositionOnTheMap(position.subtract(orientation.toUnitVector()));
                if (map.canMoveTo(newPosition)) {
                    positionChanged(position, newPosition);
                    position = newPosition;
                }
            }
        }
    }

    public void rotate() {
        for (int i = 0; i < genes.getRandomGen(); i++) move(MoveDirection.RIGHT);
    }

    public Animal copulate(Animal mother) {
        this.numberOfChildren++;
        mother.numberOfChildren++;

        int childEnergy = (int) (0.25 * mother.energy) + (int) (0.25 * this.energy);
        mother.changeEnergy(-1 * (int) (0.25 * mother.energy));
        this.changeEnergy(-1 * (int) (0.25 * this.energy));

        Vector2d childPosition = mother.position;
        MapDirection childOrientation = MapDirection.NORTH;

        Genes childGenes = new Genes(mother.genes, this.genes);

        Animal child = new Animal(map, childPosition, childOrientation, mother.startEnergy, childEnergy, childGenes);
        child.rotate();

        return child;
    }
}
