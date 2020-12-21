package agh.cs.project1;

import java.util.Arrays;

public class Genes {
    private final int genomeLength = 32;
    private final int numberOfGenes = 8;
    private final int[] genome = new int[genomeLength];

    public Genes() {
        fillGenes();
    }

    public Genes(Genes mother, Genes father) {
        divideGenes(mother, father);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int gene: genome) result.append(gene).append(" ");
        return result.toString();
    }

    public int[] getGenome() {
        return genome;
    }

    public int getRandomGen() {
        return genome[(int) (Math.random() * genomeLength)];
    }

    private void fillGenes() {
        for (int i = 0; i < genomeLength; i++) genome[i] = (int) (Math.random() * numberOfGenes);
        fixGenes();
    }

    private void divideGenes(Genes mother, Genes father) {
        int firstCut, secondCut;

        firstCut = (int) (Math.random() * (genomeLength -1) + 1);
        do {
            secondCut = (int) (Math.random() * (genomeLength -1) + 1);
        } while(firstCut == secondCut);

        if (secondCut <  firstCut) {
            int tmp = firstCut;
            firstCut = secondCut;
            secondCut = tmp;
        }

        System.arraycopy(mother.genome, 0, genome, 0, firstCut);
        System.arraycopy(father.genome, firstCut, genome, firstCut, secondCut - firstCut);
        System.arraycopy(mother.genome, secondCut, genome, secondCut, genomeLength - secondCut);

        fixGenes();
    }

    private void fixGenes() {
        int[] numberOfGenesInGenome = new int[numberOfGenes];
        for (int gene: genome) numberOfGenesInGenome[gene]++;

        for (int gene = 0; gene < numberOfGenes; gene++) {
            if (numberOfGenesInGenome[gene] == 0) {
                int mostCommonGene = getMostCommonGenePosition();
                if (findGenePositionInGenome(mostCommonGene) != -1) genome[findGenePositionInGenome(mostCommonGene)] = gene;
                numberOfGenesInGenome[gene] += 1;
                numberOfGenesInGenome[mostCommonGene] -= 1;
            }
        }

        Arrays.sort(genome);
    }

    private int getMostCommonGenePosition() {
        int mostCommonGeneIndex = 0;
        for (int i = 1; i < numberOfGenes; i++) {
            if (genome[i] > genome[mostCommonGeneIndex]) {
                mostCommonGeneIndex = i;
            }
        }
        return mostCommonGeneIndex;
    }

    private int findGenePositionInGenome(int gene) {
        for (int i = 0; i < genomeLength; i++) {
            if (genome[i] == gene)
                return i;
        }
        return -1;
    }
}
