import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Instance {

		private int category;
		private List<Boolean> vals;
		private List<String> categoryNames;

		public Instance(int cat, Scanner s, List<String> categoryNames ) {
			category = cat;
			this.categoryNames = categoryNames;
			vals = new ArrayList<Boolean>();
			while (s.hasNextBoolean())
				vals.add(s.nextBoolean());
		}

		public boolean getAtt(int index) {
			return vals.get(index);
		}

		public int getCategory() {
			return category;
		}

		public String toString() {
			StringBuilder ans = new StringBuilder(categoryNames.get(category));
			ans.append(" ");
			for (Boolean val : vals)
				ans.append(val ? "true  " : "false ");
			return ans.toString();
		}

	}