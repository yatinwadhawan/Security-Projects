import ilog.concert.IloException;

public class Main {

	public static void main(String[] str) throws IloException {

		ERASER.solve();
		ORIGAMI.solve();
		UniformRandomPolicy.uniformPolicy();

	}
}