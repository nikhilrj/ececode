import static java.lang.System.out;
import static java.lang.Math.*;

import java.util.Arrays;
import java.util.TreeMap;

import java.io.PrintWriter;
import java.io.File;

public class GeneticAlgorithm
{
	public static final StringBuffer TARGET = new StringBuffer("genetic algorithms are awesome!");
	public static final int POP_SIZE = 1024;
	public static final int MAX_GENERATION = 256;
	public static final double MUTATION_RATE = 0.01;
	public static final double LOG2 = log(2);
	
	public GeneticAlgorithm() {}
	
	public static void main(String[] args) throws Exception
	{
		PrintWriter output = new PrintWriter(new File("output.dat"));
		
		//generate initial population
		Organism[] population = new Organism[POP_SIZE];
		for(int i = 0; i < POP_SIZE; i++)
		{
			StringBuffer gen = new StringBuffer();
			while(random() > 0.05)
				gen.append((char)(Math.random() * 95 + 32));
			population[i] = new Organism(gen, getFitness(TARGET, gen));
		}
		Arrays.sort(population);
		
		//mate and create successive generations
		int generation = 0;
		StringBuffer parent1 = null, parent2 = null;
		while(population[0].fitness != 0 && generation < MAX_GENERATION)
		{
			System.out.println(population[0].id + " (generation: " + generation + " fitness: " + population[0].fitness + ")");
			output.println(population[0].id + " (generation: " + generation + " fitness: " + population[0].fitness + ")");
			
			Organism[] buffer = new Organism[POP_SIZE];
			int minFitness = Integer.MAX_VALUE;
			for(int i = 0; i < POP_SIZE; i++)
			{
				int index1 = (int)floor(pow(log(1 / (1 - random())) / LOG2, 2));
				int index2;
				while((index2 = (int)floor(pow(log(1 / (1 - random())) / LOG2, 2))) == index1);
				
				StringBuffer offspring = mate(population[index1].id, population[index2].id);
				int fitness = getFitness(TARGET, offspring);
				buffer[i] = new Organism(offspring, fitness);
				if(fitness < minFitness)
				{
					minFitness = fitness;
					parent1 = population[index1].id;
					parent2 = population[index2].id;
				}
			}
			
			output.println("\t" + parent1 + "\n\t" + parent2);
			
			for(int i = 0; i < POP_SIZE; i++)
				population[i] = buffer[i];
			Arrays.sort(population);
			
			generation++;
		}
		
		out.println(population[0].id + " (generation: " + generation + " fitness: " + population[0].fitness + ")");
		output.println(population[0].id + " (generation: " + generation + " fitness: " + population[0].fitness + ")");
		output.print("\t" + parent1 + "\n\t" + parent2);
		
		output.close();
	}
	
	public static int getFitness(StringBuffer target, StringBuffer compare)
	{
		int ret = 0;
		for(int i = 0; i < max(target.length(), compare.length()); i++)
		{
			if(i >= target.length())
			{
				ret += compare.charAt(i);
				continue;
			}
			
			if(i >= compare.length())
			{
				ret += target.charAt(i);
				continue;
			}
			
			ret += abs(target.charAt(i) - compare.charAt(i));
		}
		
		return ret;
	}
	
	public static StringBuffer mate(StringBuffer one, StringBuffer two)
	{
		StringBuffer ret = new StringBuffer(one.substring(0, one.length() / 2) + two.substring(two.length() / 2));
		for(int i = 0; i < ret.length(); i++)
			if(random() <= MUTATION_RATE)
				ret.setCharAt(i, (char)(Math.random() * 95 + 32));
		return ret;
	}
}

class Organism implements Comparable<Organism>
{
	StringBuffer id;
	int fitness;
	
	public Organism(StringBuffer id, int fitness)
	{
		this.id = id;
		this.fitness = fitness;
	}
	
	public int compareTo(Organism other)
	{
		return fitness - other.fitness;
	}
}
