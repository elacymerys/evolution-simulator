package agh.cs.project1;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class PlotRenderPanel extends JPanel {
    WorldMap map;
    EvolutionSimulation simulation;
    int statisticsForDay;

    int numberOfDays = 0;

    int numberOfAnimals;
    int numberOfGrass;
    int dominantGene;
    int energyLevel;
    int numberOfChildren;

    int totalNumberOfAnimals = 0;
    int totalNumberOfGrass = 0;
    int[] dominantGenes = new int[8];
    int avgDominantGene = 0;
    int totalEnergy = 0;
    int totalNumberOfChildren = 0;

    public PlotRenderPanel(WorldMap map, EvolutionSimulation simulation, int statisticsForDay) {
        this.map = map;
        this.simulation = simulation;
        this.statisticsForDay = statisticsForDay;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (0.4 * simulation.frame.getWidth()), simulation.frame.getHeight()-38);
        this.setLocation(0, 0);

        numberOfDays++;

        numberOfAnimals = map.getAnimals().size();
        numberOfGrass = map.getGrassField().size();
        dominantGene = map.getDominantGen();
        energyLevel = map.getEnergyLevel();
        numberOfChildren = map.getNumberOfChildren();

        if (numberOfDays <= statisticsForDay) {
            totalNumberOfAnimals += numberOfAnimals;
            totalNumberOfGrass += numberOfGrass;
            if (map.getAnimals().size() > 0) dominantGenes[dominantGene]++;
            totalEnergy += energyLevel;
            totalNumberOfChildren += numberOfChildren;
        }

        // Legend
        g.setColor(new Color(0, 0, 0));
        g.drawString("Legend: ", 20, 20);
        g.drawString("Steppe Field ", 40, 60);
        g.drawString("Jungle Field ", 40, 90);
        g.drawString("Animal ", 40, 120);
        g.drawString("Grass ", 40, 150);

        // Steppe Field
        g.setColor(new Color(129, 165, 82));
        g.fillRect(20, 50, 10, 10);

        // Jungle Field
        g.setColor(new Color(21, 52, 26));
        g.fillRect(20, 80, 10, 10);

        // Animal
        g.setColor(new Color(139, 81, 72));
        g.fillRect(20, 110, 10, 10);

        // Grass
        g.setColor(new Color(61, 113, 68));
        g.fillRect(20, 140, 10, 10);

        // Statistics
        g.setColor(new Color(0, 0, 0));
        g.drawString("Statistics for day " + numberOfDays + ": ", 20, 220);
        g.drawString("Total number of animals: " + numberOfAnimals, 20, 260);
        g.drawString("Total number of tufts of grass: " + numberOfGrass, 20, 290);
        if (map.getAnimals().size() > 0) g.drawString("Dominant gene: " + dominantGene, 20, 320);
        else g.drawString("Dominant gene: -", 20, 320);
        g.drawString("Average energy level: " + (float) (Math.round(((float) energyLevel / numberOfAnimals) * 100)) / 100, 20, 350);
        g.drawString("Average number of children: " + (float) (Math.round(((float) numberOfChildren / numberOfAnimals) * 100)) / 100, 20, 380);

        // Saving statistics to file
        if (numberOfDays == statisticsForDay) {
            try {
                FileWriter file = new FileWriter("statistics_for_day_" + statisticsForDay + ".txt");
                file.write("Average number of animals: " + (float) (Math.round(((float) totalNumberOfAnimals / numberOfDays)) * 100) / 100 + "\n");
                file.write("Average number of tufts of grass: " + (float) (Math.round(((float) totalNumberOfGrass / numberOfDays)) * 100) / 100 + "\n");
                for (int i = 1; i < 8; i++) if (dominantGenes[i] > dominantGenes[avgDominantGene]) avgDominantGene = i;
                file.write("Average dominant gene: " + avgDominantGene + "\n");
                file.write("Average energy level: " + (float) (Math.round(((float) totalEnergy / totalNumberOfAnimals)) * 100) / 100 + "\n");
                file.write("Average number of children: " + (float) (Math.round(((float) totalNumberOfChildren / totalNumberOfAnimals)) * 100) / 100 + "\n");
                file.close();
            } catch (IOException exception) {
                System.out.println("An error occured! Can't write statistics to file");
            }
        }
    }
}
