import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jfree.ui.RefineryUtilities;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QValue;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import Graph.Node;
import Graph.NodeStatus;


public class MainClass {

	public final static String ACTION_PATCH = "PATCH";
	public final static String ACTION_HACK = "HACK";
	public final static String ACTION_SCAN = "SCAN";
	public static ArrayList<String> ACTIONS = new ArrayList<String>();
	public static HashMap<String, Integer> reward = new HashMap<String, Integer>();
	public static int trials = 10;
	public static List<MAction> ls = new ArrayList<MAction>();
	public static List<State> statelist = new ArrayList<State>();
	public static int count_state = 0;
	static ArrayList<State> wl = new ArrayList<State>();

	public static void main(String[] args) throws IOException {

		// We have to write a separate function for parsing input to the model.
		// This is dummy input for testing purpose.
		ACTIONS.add(ACTION_PATCH);
		ACTIONS.add(ACTION_SCAN);

		// Create a nodelist that will create a state.
		ArrayList<Node> nlist = new ArrayList<Node>();
		for (int i = 0; i < 6; i++) {
			Node n = new Node("N" + i, NodeStatus.UNKNOWN);
			nlist.add(n);
		}
		ArrayList<Node> l0 = new ArrayList<Node>();
		l0.add(nlist.get(1));
		l0.add(nlist.get(2));
		nlist.get(0).setAdj(l0);
		ArrayList<Node> l1 = new ArrayList<Node>();
		l1.add(nlist.get(3));
		nlist.get(1).setAdj(l1);
		ArrayList<Node> l2 = new ArrayList<Node>();
		l2.add(nlist.get(3));
		l2.add(nlist.get(4));
		nlist.get(2).setAdj(l2);
		ArrayList<Node> l4 = new ArrayList<Node>();
		l4.add(nlist.get(3));
		l4.add(nlist.get(5));
		nlist.get(4).setAdj(l4);
		ArrayList<Node> l5 = new ArrayList<Node>();
		l5.add(nlist.get(3));
		nlist.get(5).setAdj(l5);

		nlist.get(3).setAdj(new ArrayList<Node>());

		// Creating action set for the domain. We will assign this in the
		// WorldGenerator class.
		for (int i = 0; i < nlist.size(); i++) {
			nlist.get(i).print();

			MAction ms = new MAction(nlist.get(i).getName(),
					MainClass.ACTION_SCAN);
			MAction mp = new MAction(nlist.get(i).getName(),
					MainClass.ACTION_PATCH);
			ls.add(ms);
			ls.add(mp);
		}

		// Assigning rewards to each node for patching.
		reward.put(nlist.get(0).getName(), 2);
		reward.put(nlist.get(1).getName(), 8);
		reward.put(nlist.get(2).getName(), 10);
		reward.put(nlist.get(3).getName(), 4);
		reward.put(nlist.get(4).getName(), 6);
		reward.put(nlist.get(5).getName(), 12);

		// Initializing the world generator.
		WorldGenerator gen = new WorldGenerator();
		SADomain domain = (SADomain) gen.generateDomain();
		WState initialstate = new WState(nlist);
		SimulatedEnvironment env = new SimulatedEnvironment(domain,
				initialstate);

		QLearning agent = new QLearning(domain, 0.5,
				new SimpleHashableStateFactory(), new ConstantValueFunction(),
				0.2, 0.1);

		ArrayList<State> wl = new ArrayList<State>();
		ArrayList<State> allStates = new ArrayList<State>();
		ArrayList<Double> plot = new ArrayList<Double>();

		// run Q-learning and store results in a list
		List<Episode> episodes = new ArrayList<Episode>(1000);
		for (int x = 0; x < trials; x++) {
			episodes.add(agent.runLearningEpisode(env));
			env.resetEnvironment();
		}

		clear("state.text");
		clear("action.text");
		clear("reward.text");

		for (int i = 0; i < trials; i++) {
			Episode e = episodes.get(i);
			List<Action> al = e.actionSequence;
			List<Double> rl = e.rewardSequence;
			List<State> sl = e.stateSequence;

			for (int j = 0; j < sl.size(); j++) {
				WState w = (WState) sl.get(j);
				if (!wl.contains(w)) {
					wl.add(sl.get(j));
					allStates.add(sl.get(j));
				}
			}

			double reward = 0.0;
			for (int j = 0; j < rl.size(); j++) {
				reward += rl.get(j);
			}
			plot.add(reward / rl.size());

			writeFile(sl, "state.text");
			writeFile(al, "action.text");
			writeFile(rl, "reward.text");
		}

		for (int i = 0; i < allStates.size(); i++) {
			State s = allStates.get(i);
			WState w = (WState) s;
			List<QValue> lq = agent.qValues(s);

			System.out.println("State " + i);

			ArrayList<Node> nl = w.getNodeList();
			for (int j = 0; j < nl.size(); j++) {
				Node n = nl.get(j);
				n.print();
			}
			for (int j = 0; j < lq.size(); j++) {
				System.out.print(lq.get(j).a.actionName() + " - " + lq.get(j).q
						+ ", ");
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}

		System.out.println("Count States - " + count_state);
		System.out.println("Total states - " + allStates.size());

		PlotGraph demo = new PlotGraph("Average Reward per Episode", plot);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

	public static void clear(String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(name);
		writer.print("");
		writer.close();
	}

	public static void writeFile(List ls, String name) throws IOException {
		File fout = new File(name);
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		bw.write("New Episode \n");
		if (name.equals("state.text")) {
			List<State> l = (List<State>) ls;
			for (int j = 0; j < l.size(); j++) {
				WState s = (WState) l.get(j);
				bw.write(s.getNodeList().toString());
				bw.newLine();
			}
		} else if (name.equals("reward.text")) {
			List<Double> l = (List<Double>) ls;
			for (int j = 0; j < l.size(); j++) {
				bw.write(l.get(j) + ", ");
			}
			bw.newLine();
		} else {
			List<Action> l = (List<Action>) ls;
			for (int j = 0; j < l.size(); j++) {
				bw.write(l.get(j).actionName() + ", ");
			}
			bw.newLine();
		}
		bw.newLine();

		bw.close();
	}

	public static void writeFile1(String str) throws IOException {
		File fout = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(str);
		bw.newLine();

		bw.close();
	}

}

// Q-Learning Algorithm: Create Q-Learning Class and implement methods.
// LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
//
// public String getAgentName() {
// return "Q-learning";
// }
//
// public LearningAgent generateAgent() {
// return new QLearning(domain, 0.5,
// new SimpleHashableStateFactory(),
// new ConstantValueFunction(), 0.5, 0.1);
// }
// };
// LearningAlgorithmExperimenter exp = new
// LearningAlgorithmExperimenter(
// env, 1000, 100, qLearningFactory);
//
// exp.setUpPlottingConfiguration(500, 250, 2, 1000,
// TrialMode.MOST_RECENT_AND_AVERAGE,
// PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
// PerformanceMetric.AVERAGE_EPISODE_REWARD);
//
// // start experiment
// exp.startExperiment();

// LinkedHashMap<State, ArrayList<Double>> map = new LinkedHashMap<State,
// ArrayList<Double>>();
// for (int i = 0; i < trials; i++) {
// Episode e = episodes.get(i);
// List<Action> ls = e.actionSequence;
// List<Double> rs = e.rewardSequence;
// List<State> sl = e.stateSequence;
//
// for (int j = 0; j < rs.size(); j++) {
// if (!map.containsKey(sl.get(j))) {
// ArrayList<Double> re = new ArrayList<Double>();
// re.add(rs.get(j));
// map.put(sl.get(j), re);
// } else {
// ArrayList<Double> re = map.get(sl.get(j));
// re.add(rs.get(j));
// map.put(sl.get(j), re);
// }
// }
// }
// Set set = map.entrySet();
// java.util.Iterator itr = set.iterator();
// while (itr.hasNext()) {
// Map.Entry me = (Map.Entry) ((java.util.Iterator<Entry<State,
// ArrayList<Double>>>) itr)
// .next();
// ArrayList<Double> ls = (ArrayList<Double>) me.getValue();
// PlotGraph demo = new PlotGraph("Average Reward per Episode", ls);
// demo.pack();
// RefineryUtilities.centerFrameOnScreen(demo);
// demo.setVisible(true);
// break;
// }
