import java.util.Random;

public class Input {

	static int targets = 7;
	static double Z = 9999.0;
	static double B = 15.0; // Budget: number of hours defender has.

	static double[] defCov = { 11.0, 7.0, 14.0, 15.0, 19.0, 16.0, 17.0 };
	static double[] defUCov = { -22.0, -14.0, -35.0, -19.0, -38.0, -14.0, -21.0 };
	static double[] attCov = { -21.0, -31.0, -13.0, -16.0, -25.0, -33.0, -41.0 };
	static double[] attUCov = { 14.0, 23.0, 30.0, 17.0, 76.0, 16.0, 64.0 };
	static double[] hours = { 9.0, 3.0, 4.0, 8.0, 7.0, 6.0, 5.0 };

	// When input called.
	public static void input() {
		// for (int t = 0; t < targets; t++) {
		// defCov[t] = randomNumberInRange(0, 100);
		// attUCov[t] = randomNumberInRange(0, 100);
		// defUCov[t] = randomNumberInRange(-100, 0);
		// attCov[t] = randomNumberInRange(-100, 0);
		// hours[t] = randomNumberInRange(5, 15);
		// }

	}

	public static int randomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}

}