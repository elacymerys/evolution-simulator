package agh.cs.project1;

import java.io.FileNotFoundException;

public class World {

    public static void main(String[] args) {
        try {
            ParametersLoader parameters = ParametersLoader.loadParametersFromFile();

            WorldMap map = new WorldMap(
                    parameters.getWidth(),
                    parameters.getHeight(),
                    parameters.getJungleWidth(),
                    parameters.getJungleHeight(),
                    parameters.getStartEnergy(),
                    parameters.getMoveEnergy(),
                    parameters.getPlantEnergy()
            );

            EvolutionSimulation simulation = new EvolutionSimulation(map, parameters.getNumberOfAnimals(), parameters.getDelay(), parameters.getStatisticsForDay());
            simulation.start();

        } catch (FileNotFoundException | IllegalArgumentException exception) {
            System.out.println("Exception: " + exception);
            System.exit(1);
        }
    }
}
