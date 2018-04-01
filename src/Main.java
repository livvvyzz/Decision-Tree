
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double prob = 0;
		for(int i = 1; i < 10; i++){
			String training = "hepatitis-training-run0" + i +".dat";
			String test = "hepatitis-test-run0" + i +".dat";
			DecTree dt = new DecTree(training, test);
			double subProb = dt.test();
			System.out.println(i + "  " + subProb);
			prob = prob + subProb;
			
		}
		double avgProb = prob/9;
		System.out.println(avgProb);
	}

}