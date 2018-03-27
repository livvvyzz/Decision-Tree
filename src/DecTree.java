import java.util.*;
import java.io.*;

public class DecTree {

	private int numCategories;
	private int numAtts;
	private List<String> categoryNames;
	private List<String> attNames;
	private List<Instance> allInstances;

	public DecTree(String fname) {
		// create instance
		readDataFile(fname);
		// find how many instances there are of each category
		double[] numEachCat = new double[numCategories];
		for (Instance i : allInstances) {
			numEachCat[i.getCategory()] += 1;
		}
		// calculate entropy of target E(T)
		entropy(numEachCat);
		// calculate entropy off cat+each attribute E(T,X)
		for (int i = 0; i < numAtts; i++) {
			double[][] table = new double[numCategories][2];
			for (Instance j : allInstances) {
				boolean bool = j.getAtt(i);
				if (bool) {
					table[j.getCategory()][1] += 1;
				} else
					table[j.getCategory()][2] += 1;
			}
			double ent = entropy(table);
		}
	}

	private void readDataFile(String fname) {
		/*
		 * format of names file: names of categories, separated by spaces names
		 * of attributes category followed by true's and false's for each
		 * instance
		 */
		System.out.println("Reading data from file " + fname);
		try {
			Scanner din = new Scanner(new File(fname));

			categoryNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
				categoryNames.add(s.next());
			numCategories = categoryNames.size();
			System.out.println(numCategories + " categories");

			attNames = new ArrayList<String>();
			for (Scanner s = new Scanner(din.nextLine()); s.hasNext();)
				attNames.add(s.next());
			numAtts = attNames.size();
			System.out.println(numAtts + " attributes");

			allInstances = readInstances(din);
			din.close();
		} catch (IOException e) {
			throw new RuntimeException("Data File caused IO exception");
		}
	}

	private List<Instance> readInstances(Scanner din) {
		/* instance = classname and space separated attribute values */
		List<Instance> instances = new ArrayList<Instance>();
		String ln;
		while (din.hasNext()) {
			Scanner line = new Scanner(din.nextLine());
			instances.add(new Instance(categoryNames.indexOf(line.next()), line, categoryNames));
		}
		System.out.println("Read " + instances.size() + " instances");
		return instances;
	}

	/**
	 * Calculates the entropy of an attribute, given an array of the number of
	 * instances that has/does not have the attribute
	 * 
	 * @param cat
	 * @return the entropy
	 */
	public double entropy(double[] att) {
		double ent = 0;
		for (int i = 0; i < att.length; i++) {
			double prob = att[i] / allInstances.size();
			System.out.println(att[i] + "   " + allInstances.size() + "   " + prob);
			ent = ent - prob * (Math.log(prob) / Math.log(2));
			System.out.println(att[i] + "   " + ent);
		}
		return ent;
	}

	public double entropy(double[][] table) {
		// get the total of each sub categoriy of the attribute
		double t = 0;
		double f = 0;
		for (int i = 0; i < numCategories; i++) {
			t += table[i][1];
			f += table[i][2];
		}
		// calculate entropy
		for (int j = 0; j < 2; j++) {
			double[] cat = new double[numCategories];
			for (int i = 0; i < numCategories; i++) {
				cat[i] = table[i][j];
			}
			
		}
		double entropy = (t / allInstances.size()) * entropy();
		return 0;
	}

}
