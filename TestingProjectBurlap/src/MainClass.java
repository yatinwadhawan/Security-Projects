import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;
import Graph.Graph;
import Graph.Node;
import Graph.NodeStatus;

public class MainClass {

	public final static String ACTION_PATCH = "PATCH";
	public final static String ACTION_HACK = "HACK";
	public final static String ACTION_SCAN = "SCAN";
	public static ArrayList<String> ACTIONS = new ArrayList<String>();
	public static ArrayList<Node> nlist = new ArrayList<Node>();
	public static HashMap<String, Integer> reward = new HashMap<String, Integer>();

	public static void main(String[] args) {

		// We have to write a separate function for parsing input to the model.
		// This is dummy input for testing purpose.
		ACTIONS.add(ACTION_PATCH);
		ACTIONS.add(ACTION_SCAN);

		for (int i = 0; i < 4; i++) {
			Node n = new Node("N" + i, "N" + i, NodeStatus.UNKNOWN, 0.6);
			nlist.add(n);
		}
		ArrayList<Node> l = new ArrayList<Node>();
		l.add(nlist.get(1));
		l.add(nlist.get(2));
		nlist.get(0).setAdj(l);
		ArrayList<Node> l1 = new ArrayList<Node>();
		l1.add(nlist.get(3));
		nlist.get(1).setAdj(l1);
		nlist.get(2).setAdj(l1);
		nlist.get(3).setAdj(new ArrayList<Node>());

		Graph g = new Graph(nlist.size(), 4, nlist, nlist.get(0),
				nlist.get(nlist.size() - 1));

		for (int i = 0; i < 4; i++) {
			Node.print(nlist.get(i));
		}
		reward.put(nlist.get(0).getName(), 20);
		reward.put(nlist.get(1).getName(), 30);
		reward.put(nlist.get(2).getName(), 10);
		reward.put(nlist.get(3).getName(), 40);

		WorldGenerator gen = new WorldGenerator();
		SADomain domain = (SADomain) gen.generateDomain();
		WState initialstate = new WState(g);
		SimulatedEnvironment env = new SimulatedEnvironment(domain,
				initialstate);

		// Q-Learning Algorithm: Create Q-Learning Class and implement methods.
		// create Q-learning
		QLearning agent = new QLearning(domain, 0.99,
				new SimpleHashableStateFactory(), new ConstantValueFunction(),
				0.1, 0.1);

		// run Q-learning and store results in a list
		List<Episode> episodes = new ArrayList<Episode>(1000);
		for (int i = 0; i < 1; i++) {
			episodes.add(agent.runLearningEpisode(env));
			env.resetEnvironment();
		}
	}

}