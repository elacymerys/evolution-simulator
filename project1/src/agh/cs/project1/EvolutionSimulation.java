package agh.cs.project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EvolutionSimulation implements ActionListener {
    private final WorldMap map;
    private final int numberOfAnimals;
    private final int delay;
    private final int statisticsForDay;

    public Timer timer;
    public JFrame frame;
    public RenderPanel renderPanel;
    public PlotRenderPanel plotRenderPanel;

    public EvolutionSimulation(WorldMap map, int numberOfAnimals, int delay, int statisticsForDay) {
        this.map = map;
        this.numberOfAnimals = numberOfAnimals;
        this.delay = delay;
        this.statisticsForDay = statisticsForDay;

        timer = new Timer(delay, this);

        frame = new JFrame("Evolution Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        plotRenderPanel = new PlotRenderPanel(map, this, statisticsForDay);
        plotRenderPanel.setSize(1, 1);

        renderPanel = new RenderPanel(map, this);
        renderPanel.setSize(new Dimension(1, 1));

        frame.add(plotRenderPanel);
        frame.add(renderPanel);
    }

    public void start() {
        for (int i = 0; i < numberOfAnimals; i++) map.spawnAnimal();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        plotRenderPanel.repaint();
        renderPanel.repaint();

        map.removeDeadAnimals();
        map.moveAnimals();
        map.eating();
        map.reproduction();
        map.spawnGrass();
    }
}
