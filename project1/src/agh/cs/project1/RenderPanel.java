package agh.cs.project1;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {
    WorldMap map;
    EvolutionSimulation simulation;

    public RenderPanel(WorldMap map, EvolutionSimulation simulation) {
        this.map = map;
        this.simulation = simulation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (0.6 * simulation.frame.getWidth()), simulation.frame.getHeight()-38);
        this.setLocation((int) (0.4 * simulation.frame.getWidth()), 0);

        int width = this.getWidth();
        int height = this.getHeight();

        Vector2d jungleLowerLeft = map.getJungleLowerLeft();

        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        int jungleWidth = map.getJungleWidth();
        int jungleHeight = map.getJungleHeight();

        int scaledWidth = width / mapWidth;
        int scaledHeight = height / mapHeight;

        // draw Steppe
        g.setColor(new Color(129, 165, 82));
        g.fillRect(0, 0, width, height);

        // draw Jungle
        g.setColor(new Color(24, 59, 30));
        g.fillRect(
                jungleLowerLeft.x * scaledWidth,
                jungleLowerLeft.y * scaledHeight,
                jungleWidth * scaledWidth,
                jungleHeight * scaledHeight
        );

        // draw Grass
        for (Grass grass : map.getGrassField()) {
            g.setColor(grass.toColor());
            int x = grass.getPosition().x * scaledWidth;
            int y = grass.getPosition().y * scaledHeight;
            g.fillRect(x, y, scaledWidth, scaledHeight);
        }

        // draw Animals
        for (Animal animal : map.getAnimals()) {
            g.setColor(animal.toColor());
            int x = animal.getPosition().x * scaledWidth;
            int y = animal.getPosition().y * scaledHeight;
            g.fillOval(x, y, scaledWidth, scaledHeight);
        }
    }
}
