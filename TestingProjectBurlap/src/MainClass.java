import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
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

	public static void main(String[] args) throws IOException {

		// We have to write a separate function for parsing input to the model.
		// This is dummy input for testing purpose.
		ACTIONS.add(ACTION_PATCH);
		ACTIONS.add(ACTION_SCAN);

		for (int i = 0; i < 4; i++) {
			Node n = new Node("N" + i, "N" + i, NodeStatus.UNKNOWN, 0.0);
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

		reward.put(nlist.get(0).getName(), 80);
		reward.put(nlist.get(1).getName(), 120);
		reward.put(nlist.get(2).getName(), 40);
		reward.put(nlist.get(3).getName(), 160);

		WorldGenerator gen = new WorldGenerator();
		SADomain domain = (SADomain) gen.generateDomain();
		WState initialstate = new WState(g);
		SimulatedEnvironment env = new SimulatedEnvironment(domain,
				initialstate);

		// Q-Learning Algorithm: Create Q-Learning Class and implement methods.
		// create Q-learning

		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {

			public String getAgentName() {
				return "Q-learning";
			}

			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.5,
						new SimpleHashableStateFactory(),
						new ConstantValueFunction(), 0.5, 0.1);
			}
		};
		QLearning agent = new QLearning(domain, 0.5,
				new SimpleHashableStateFactory(), new ConstantValueFunction(),
				0.1, 0.1);

		// run Q-learning and store results in a list
		List<Episode> episodes = new ArrayList<Episode>(1000);
		for (int i = 0; i < 500; i++) {
			episodes.add(agent.runLearningEpisode(env));
			env.resetEnvironment();
		}

		// LearningAlgorithmExperimenter exp = new
		// LearningAlgorithmExperimenter(
		// env, 500, 1000, qLearningFactory);
		//
		// exp.setUpPlottingConfiguration(500, 250, 2, 1000,
		// TrialMode.MOST_RECENT_AND_AVERAGE,
		// PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
		// PerformanceMetric.AVERAGE_EPISODE_REWARD);
		//
		// // start experiment
		// exp.startExperiment();

		PrintWriter writer = new PrintWriter("out.txt");
		writer.print("");
		writer.close();

		for (int i = 0; i < 500; i++) {
			Episode e = episodes.get(i);
			List<Action> ls = e.actionSequence;
			List<Double> rs = e.rewardSequence;
			writeFile1(ls);
			System.out.println();
			System.out.println(rs);
			System.out.println();
		}

	}

	public static void writeFile1(List<Action> ls) throws IOException {
		File fout = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		bw.write("New Episode \n");
		for (int j = 0; j < ls.size(); j++) {
			System.out.print(ls.get(j).actionName() + ", ");
			bw.write(ls.get(j).actionName() + ", ");
		}
		bw.newLine();
		bw.newLine();
		bw.newLine();

		bw.close();
	}

}
