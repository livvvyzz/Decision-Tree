import java.util.*;
import java.io.*;

public class DecTree {

	private int numCategories;
	private int numAtts;
	private List<String> categoryNames;
	private List<String> attNames;
	private List<Instance> allInstances;
	private Node root;

	public DecTree(String fname, String test) {
		// create instance
		readDataFile(fname);
		root = BuildTree(allInstances, attNames);
		// root.report("");
		readDataFile(test);

	}

	public Node BuildTree(List<Instance> inst, List<String> attr) {
		int attNum = 0;
		int attrNum = 0;
		List<Instance> bestTrue = new ArrayList<Instance>();
		List<Instance> bestFalse = new ArrayList<Instance>();

		if (inst.isEmpty()) {
			// return name and probability of most probable class
			return baseLinePredictor();
		} else if (instancesPure(inst)) {
			// return leaf node of class and prob 1
			String name = categoryNames.get(inst.get(0).getCategory());
			return new LeafNode(name, 1);

		} else if (attr.isEmpty()) {
			// get majority name and prob
			int[] numPerCat = new int[numCategories];
			for (Instance i : inst) {
				numPerCat[i.getCategory()] += 1;
			}
			// get majority
			int max = -1;
			int maxIndex = -1;
			for (int i = 0; i < numPerCat.length; i++) {
				if (numPerCat[i] > max) {
					maxIndex = i;
					max = numPerCat[i];
				}
			}
			String name = categoryNames.get(maxIndex);
			double prob = (double) max / inst.size();
			return new LeafNode(name, prob);
		}
		// find best attribute
		else {
			// find how many instances there are of each category
			double[] numEachCat = new double[numCategories];
			for (Instance i : inst) {
				numEachCat[i.getCategory()] += 1;
			}
			// calculate entropy of target E(T)
			double target = entropy(numEachCat, inst.size());
			// calculate entropy off cat+each attribute E(T,X)
			double gain = 0;
			double max = -10;

			for (int i = 0; i < attr.size(); i++) {
				double[][] table = new double[numCategories][2];
				int index = getAttributeIndex(attr, i);

				for (Instance j : inst) {
					boolean bool = j.getAtt(index); //////////////////////////////////////////////// would
					//////////////////////////////////////////////// need
					//////////////////////////////////////////////// to
					//////////////////////////////////////////////// fix
					if (bool) {
						table[j.getCategory()][0] += 1;
					} else
						table[j.getCategory()][1] += 1;
				}
				gain = target - entropy(table);
				if (gain > max) {
					max = gain;
					attNum = index;
					attrNum = i;
				}
			}
			// get lists for the best attr of instances that are true/false
			for (Instance i : inst) {
				if (i.getAtt(attNum))
					bestTrue.add(i);
				else
					bestFalse.add(i);
			}
			// build subtree using remaining atributes
			String name = attr.remove(attrNum);
			Node left = BuildTree(bestTrue, attr);
			Node right = BuildTree(bestFalse, attr);
			return new InnerNode(name, left, right);

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
	public double entropy(double[] att, double n) {
		double ent = 0;
		for (int i = 0; i < att.length; i++) {
			double prob = att[i] / n;
			ent = ent - prob * (Math.log(prob) / Math.log(2));
		}
		return ent;
	}

	/**
	 * Calculates the entropy of two attributes
	 * 
	 * @param table
	 *            2d array of both attributes
	 * @return entropy
	 */
	public double entropy(double[][] table) {
		// get the total of each sub categoriy of the attribute
		double t = 0;
		double f = 0;
		for (int i = 0; i < numCategories; i++) {
			t += table[i][0];
			f += table[i][1];
		}
		// calculate entropy
		double entropyTrue = 0;
		double entropyFalse = 0;
		for (int j = 0; j < 2; j++) {
			double[] cat = new double[numCategories];
			for (int i = 0; i < numCategories; i++) {
				cat[i] = table[i][j];
			}
			if (j == 0)
				entropyTrue = entropy(cat, t);
			else
				entropyFalse = entropy(cat, f);
		}
		double entropy = ((t / allInstances.size()) * entropyTrue) + ((f / allInstances.size()) * entropyFalse);
		return entropy;
	}

	/**
	 * takes a set of isntacnes and returns true if they are pure (all of the
	 * same class)
	 * 
	 * @param instances
	 */
	public boolean instancesPure(List<Instance> instances) {
		int cat = 0;
		int i = 0;
		for (Instance n : instances) {
			if (i == 0)
				cat = n.getCategory();
			else {
				if (cat != n.getCategory())
					return false;
			}
			i++;
		}
		return true;
	}

	public int getAttributeIndex(List<String> attr, int c) {
		for (int i = 0; i < attNames.size(); i++) {
			if (attNames.get(i).equals(attr.get(c))) {
				return i;
			}
		}

		return -1;
	}

	public Node baseLinePredictor() {
		int[] array = new int[numCategories];
		for (Instance i : allInstances) {
			array[i.getCategory()]++;
		}
		int max = -1;
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				index = i;
				max = array[i];
			}
		}
		double prob = (double) max / allInstances.size();

		return new LeafNode(categoryNames.get(index), prob);
	}

	public double test() {
		double num = 0; // number of isntances that are classified successfully
		double classACorrect = 0;
		double classBCorrect = 0;
		double classATotal = 0;
		double classBTotal = 0;
		for (Instance i : allInstances) {
			String className = classify(i, root);
			if (categoryNames.get(i.getCategory()).equals(className))
				num++;

			// check for each class for many correct
			if (i.getCategory() == 0) {
				classATotal++;
				if (categoryNames.get(i.getCategory()).equals(className))
					classACorrect++;
			} else if (i.getCategory() == 1) {
				classBTotal++;
				if (categoryNames.get(i.getCategory()).equals(className))
					classBCorrect++;
			}
		}
		System.out.println(categoryNames.get(0) + ": " + classACorrect + " out of " + classATotal);
		System.out.println(categoryNames.get(1) + ": " + classBCorrect + " out of " + classBTotal);
		double prob = num / allInstances.size();
		return prob;
	}

	public String classify(Instance i, Node n) {

		if (n instanceof LeafNode) {
			return ((LeafNode) n).getName();
		} else {
			if (i.getAtt(attNames.indexOf(((InnerNode) n).getAttr()))) {
				return classify(i, ((InnerNode) n).getLeft());
			} else
				return classify(i, ((InnerNode) n).getRight());
		}
	}

}
