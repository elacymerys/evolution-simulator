package agh.cs.project1;

import java.util.*;

public class WorldMap implements IPositionChangeObserver {
    private final int width;
    private final int height;
    private final int jungleWidth;
    private final int jungleHeight;

    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;

    private final int startEnergy;
    private final int copulationEnergy;
    private final int moveEnergy;
    private final int plantEnergy;

    private final Map<Vector2d, LinkedList<Animal>> animals = new LinkedHashMap<>();
    private final Map<Vector2d,Grass> grassField = new LinkedHashMap<>();

    public WorldMap(int width, int height, int jungleWidth, int jungleHeight, int startEnergy, int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;

        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.jungleLowerLeft = new Vector2d((width-jungleWidth)/2, (height-jungleHeight)/2);
        this.jungleUpperRight = new Vector2d(jungleLowerLeft.x + jungleWidth, jungleLowerLeft.y + jungleHeight);

        this.startEnergy = startEnergy;
        this.copulationEnergy = startEnergy/2;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        if (animals.containsKey(oldPosition) && animals.get(oldPosition).contains(animal)) {
            if (!animals.containsKey(newPosition)) animals.put(newPosition, new LinkedList<>());
            animals.get(newPosition).add(animal);

            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty()) animals.remove(oldPosition);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
    }

    public Vector2d getJungleUpperRight() {
        return jungleUpperRight;
    }

    public LinkedList<Grass> getGrassField() {
        return new LinkedList<>(grassField.values());
    }

    public LinkedList<Animal> getAnimals() {
        LinkedList<Animal> animalsList = new LinkedList<>();
        for (LinkedList<Animal> animalsAtPosition : animals.values()) animalsList.addAll(animalsAtPosition);
        return animalsList;
    }

    public int getDominantGen() {
        int dominantGene = 0;

        int[] numberOfGenes = new int[8];
        for (Animal animal : getAnimals())
            for (int gene : animal.getGenes().getGenome())
                numberOfGenes[gene]++;

        for (int gene = 1; gene < 8; gene++)
            if (numberOfGenes[gene] > numberOfGenes[dominantGene])
                dominantGene = gene;

        return dominantGene;
    }

    public int getEnergyLevel() {
        int totalEnergy = 0;
        for (Animal animal : getAnimals()) totalEnergy += animal.getEnergy();
        return totalEnergy;
    }

    public int getNumberOfChildren() {
        int totalNumberOfChildren = 0;
        for (Animal animal : getAnimals()) totalNumberOfChildren += animal.getNumberOfChildren();
        return totalNumberOfChildren;
    }

    public boolean canMoveTo(Vector2d position) {
        return position.x >= 0 && position.x <= width && position.y >= 0 && position.y <= height;
    }

    public void place(Animal animal) {
        Vector2d animalPosition = animal.getPosition();
        if (!animals.containsKey(animalPosition)) animals.put(animalPosition, new LinkedList<>());
        animals.get(animalPosition).add(animal);
        animal.addObserver(this);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public Object objectAt(Vector2d position) {
        if (animals.containsKey(position)) return animals.get(position).getFirst();
        if (grassField.containsKey(position)) return grassField.get(position);
        return null;
    }

    public void removeDeadAnimals() {
        LinkedList<Animal> animalsList = getAnimals();
        for (Animal animal : animalsList) {
            if (animal.isDead()) {
                Vector2d animalPosition = animal.getPosition();
                animal.removeObserver(this);
                animals.get(animalPosition).remove(animal);
                if (animals.get(animalPosition).isEmpty()) animals.remove(animalPosition);
            }
        }
    }

    public void moveAnimals() {
        LinkedList<Animal> animalsList = getAnimals();
        for (Animal animal : animalsList) {
            animal.rotate();
            animal.move(MoveDirection.FORWARD);
            animal.changeEnergy(-1 * moveEnergy);
        }
    }

    public void eating() {
        LinkedList<Grass> grassList = getGrassField();
        for (Grass grass : grassList) {
            Vector2d grassPosition = grass.getPosition();
            if (animals.containsKey(grassPosition)) {
                LinkedList<Animal> animalsList = animals.get(grassPosition);
                animalsList.sort(Comparator.comparing(Animal::getEnergy).reversed());

                int numberOfAnimalsWithMaxEnergy = 1;
                int maxEnergy = animalsList.get(0).getEnergy();
                for (int i = 1; i < animalsList.size(); i++) {
                    if (animalsList.get(i).getEnergy() == maxEnergy) numberOfAnimalsWithMaxEnergy += 1;
                    else break;
                }

                int dividedPlantEnergy = plantEnergy / numberOfAnimalsWithMaxEnergy;
                for (int i = 0; i < numberOfAnimalsWithMaxEnergy; i++) animalsList.get(i).changeEnergy(dividedPlantEnergy);

                grassField.remove(grassPosition);
            }
        }
    }

    public void reproduction() {
        Set<Vector2d> positions = animals.keySet();
        for (Vector2d position : positions) {
            if (animals.get(position).size() > 1) {
                LinkedList<Animal> animalsList = animals.get(position);
                animalsList.sort(Comparator.comparing(Animal::getEnergy).reversed());

                Animal father = animalsList.get(0);
                Animal mother = animalsList.get(1);
                if (father.getEnergy() >= copulationEnergy && mother.getEnergy() >= copulationEnergy) {
                    Animal child = father.copulate(mother);
                    place(child);
                }
            }
        }
    }

    public void spawnGrass() {
        if (getRandomFreePositionInTheJungle() != null) {
            Vector2d grassPosition = getRandomFreePositionInTheJungle();
            Grass grass = new Grass(grassPosition);
            grassField.put(grassPosition, grass);
        }

        if (getRandomFreePositionInTheSteppe() != null) {
            Vector2d grassPosition = getRandomFreePositionInTheSteppe();
            Grass grass = new Grass(grassPosition);
            grassField.put(grassPosition, grass);
        }
    }

    public void spawnAnimal() {
        Vector2d animalPosition;

        if (getRandomFreePosition() != null) animalPosition = getRandomFreePosition();
        else animalPosition = new Vector2d((int) (Math.random() * (width+1)), (int) (Math.random() * (height+1)));

        MapDirection animalOrientation = MapDirection.NORTH;

        Genes animalGenes = new Genes();

        Animal animal = new Animal(this, animalPosition, animalOrientation, startEnergy, animalGenes);
        animal.rotate();

        place(animal);
    }

    public Vector2d getPositionOnTheMap(Vector2d position) {
        int x = position.x;
        int y = position.y;
        if (x > width) x %= (width+1);
        if (y > height) y %= (height+1);
        while (x < 0) x = (width + 1 + x);
        while (y < 0) y = (height + 1 + y);
        return new Vector2d(x, y);
    }

    public Vector2d getRandomFreePosition() {
        Vector2d position;
        for (int i = 0; i < (int) (Math.sqrt(width * height)); i++) {
            position = new Vector2d((int) (Math.random() * (width+1)), (int) (Math.random() * (height+1)));
            if (!isOccupied(position)) return position;
        }
        return getFreePosition();
    }

    public Vector2d getFreePosition() {
        Vector2d position;
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                position = new Vector2d(x, y);
                if (!isOccupied(position)) return position;
            }
        }
        return null;
    }

    public Vector2d getRandomFreePositionInTheSteppe() {
        Vector2d position;
        for (int i = 0; i < (int) (Math.sqrt(width * height - jungleWidth * jungleHeight)); i++) {
            position = new Vector2d((int) (Math.random() * (width+1)), (int) (Math.random() * (height+1)));
            if (position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight)) continue;
            if (!isOccupied(position)) return position;
        }
        return getFreePositionInTheSteppe();
    }

    public Vector2d getFreePositionInTheSteppe() {
        Vector2d position;
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                position = new Vector2d(x, y);
                if (position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight)) continue;
                if (!isOccupied(position)) return position;
            }
        }
        return null;
    }

    public Vector2d getRandomFreePositionInTheJungle() {
        Vector2d position;
        for (int i = 0; i < (int) (Math.sqrt(jungleWidth * jungleHeight)); i++) {
            position = new Vector2d((int) (jungleLowerLeft.x + Math.random() * (jungleWidth+1)), (int) (jungleLowerLeft.y + Math.random() * (jungleHeight+1)));
            if (!isOccupied(position)) return position;
        }
        return getFreePositionInTheJungle();
    }

    public Vector2d getFreePositionInTheJungle() {
        Vector2d position;
        for (int x = jungleLowerLeft.x; x <= jungleUpperRight.x; x++) {
            for (int y = jungleLowerLeft.y; y <= jungleUpperRight.y; y++) {
                position = new Vector2d(x, y);
                if (!isOccupied(position)) return position;
            }
        }
        return null;
    }
}
