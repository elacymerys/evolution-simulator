package agh.cs.project1;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ParametersLoader {
    private int width;
    private int height;
    private int jungleWidth;
    private int jungleHeight;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int numberOfAnimals;
    private int delay;
    private int statisticsForDay;

    static public ParametersLoader loadParametersFromFile() throws FileNotFoundException, IllegalArgumentException {
        Gson gson = new Gson();
        ParametersLoader parameters = gson.fromJson(new FileReader("src/agh/cs/project1/parameters.json"), ParametersLoader.class);
        parameters.validate();
        return parameters;
    }

    private void validate() throws IllegalArgumentException {
        if (this.width <= 0) throw new IllegalArgumentException("Invalid map width");
        if (this.height <= 0) throw new IllegalArgumentException("Invalid map height");
        if (this.jungleWidth <= 0 || this.jungleWidth > this.width) throw new IllegalArgumentException("Invalid jungle width");
        if (this.jungleHeight <= 0 || this.jungleHeight > this.height) throw new IllegalArgumentException("Invalid jungle height");
        if (this.startEnergy < 0) throw new IllegalArgumentException("Invalid start energy");
        if (this.moveEnergy < 0) throw new IllegalArgumentException("Invalid move energy");
        if (this.plantEnergy < 0) throw new IllegalArgumentException("Invalid plant energy");
        if (this.numberOfAnimals <= 0) throw new IllegalArgumentException("Invalid number of animals");
        if (this.delay < 0) throw new IllegalArgumentException("Invalid value of delay");
        if (this.statisticsForDay <= 0) throw new IllegalArgumentException("Invalid value of days");
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setJungleWidth(int jungleWidth) {
        this.jungleWidth = jungleWidth;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public void setJungleHeight(int jungleHeight) {
        this.jungleHeight = jungleHeight;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public void setMoveEnergy(int moveEnergy) {
        this.moveEnergy = moveEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public void setPlantEnergy(int plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public void setNumberOfAnimals(int numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setStatisticsForDay(int statisticsForDay) {
        this.statisticsForDay = statisticsForDay;
    }

    public int getStatisticsForDay() {
        return statisticsForDay;
    }
}
