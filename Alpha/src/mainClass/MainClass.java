package mainClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jfree.ui.RefineryUtilities;
import QLearning.MAction;
import QLearning.QLearning;
import QLearning.WState;
import QLearning.WorldGenerator;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QValue;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import classes.NetworkComponent;
import classes.Node;
import classes.NodeStatus;
import classes.Vulnerability;
import database.InstallDatabase;

public class MainClass {

	// Path of the database
	public static String FILENAME = "/Users/yatinwadhawan/Documents/Projects/Alpha/src/database/";
	public final static String ADDRESS = "/Users/yatinwadhawan/Documents/Results/";

	// Data Structure
	public static ArrayList<Vulnerability> vulnerabilityList = new ArrayList<Vulnerability>();
	public static HashMap<String, Vulnerability> vulnerabilityMap = new HashMap<String, Vulnerability>();
	public static ArrayList<NetworkComponent> networkList = new ArrayList<NetworkComponent>();
	public static HashMap<String, NetworkComponent> networkMap = new HashMap<String, NetworkComponent>();
	public static ArrayList<Node> nodeList = new ArrayList<Node>();
	public static HashMap<String, Node> nodeMap = new HashMap<String, Node>();

	// Data Structure for Storing QLearning variables
	public static ArrayList<String> ACTIONS = new ArrayList<String>();
	public static HashMap<String, Integer> reward = new HashMap<String, Integer>();
	public static List<MAction> actionList = new ArrayList<MAction>();
	public static ArrayList<State> wstateList = new ArrayList<State>();
	public static List<State> statelist = new ArrayList<State>();

	// Configuration Variables
	public static int trials = 5000;
	public static double learningrate = 0.8;
	public static double gamma = 0.2;
	public static double epsilon = 0.4;
	public static int count_state = 0;

	public static void main(String[] str) throws IOException {

		// Writing Configuration to the file
		clear(ADDRESS + "config.text");
		clear(ADDRESS + "action.text");
		clear(ADDRESS + "reward.text");
		clear(ADDRESS + "states.text");
		writeConfig(ADDRESS + "config.text");

		// Installing database into data structures from files
		InstallDatabase.install();
		InstallDatabase.print();

		NodeStatus.addActions();
		NodeStatus.createActionList();
		NodeStatus.createRewardList();

		// Initializing the world generator.
		WorldGenerator gen = new WorldGenerator();
		SADomain domain = (SADomain) gen.generateDomain();
		WState initialstate = new WState(nodeList);
		SimulatedEnvironment env = new SimulatedEnvironment(domain,
				initialstate);
		QLearning agent = new QLearning(domain, gamma,
				new SimpleHashableStateFactory(), new ConstantValueFunction(),
				learningrate, epsilon);

		ArrayList<State> wl = new ArrayList<State>();
		ArrayList<State> allStates = new ArrayList<State>();
		ArrayList<Double> plot = new ArrayList<Double>();
		HashMap<State, List<List<QValue>>> map = new HashMap<State, List<List<QValue>>>();

		// run Q-learning and store results in a list
		List<Episode> episodes = new ArrayList<Episode>(trials);
		for (int x = 0; x < trials; x++) {
			episodes.add(agent.runLearningEpisode(env));
			env.resetEnvironment();
			Episode e = episodes.get(x);
			List<State> sl = e.stateSequence;

			for (int j = 0; j < sl.size(); j++) {
				WState w = (WState) sl.get(j);
				if (!wl.contains(w)) {
					List<List<QValue>> qv = new ArrayList<List<QValue>>();
					wl.add(sl.get(j));
					qv.add(agent.qValues(sl.get(j)));
					map.put(sl.get(j), qv);
				} else {
					List<List<QValue>> qv = map.get(sl.get(j));
					qv.add(agent.qValues(sl.get(j)));
					map.put(sl.get(j), qv);
				}
			}
		}
		System.out.println("Count States - " + count_state);
		System.out.println("Total states - " + allStates.size());
		wl.clear();
		allStates.clear();
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

			writeFile(al, ADDRESS + "action.text", i);
			writeFile(rl, ADDRESS + "reward.text", i);
		}

		System.out.println("Count States - " + count_state);
		System.out.println("Total states - " + allStates.size());

		PlotGraph demo = new PlotGraph("Average Reward per Episode", plot);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

		purgeDirectory(ADDRESS + "/states/");
		writeAllStates(allStates, ADDRESS + "states.text");
		createFilesForEachStateQValues(map);

	}

	public static void writeAllStates(ArrayList<State> allStates, String name)
			throws IOException {

		File fout = new File(name);
		FileOutputStream fos = new FileOutputStream(fout, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i < allStates.size(); i++) {
			State s = allStates.get(i);
			WState w = (WState) s;

			bw.write("State " + i + "\n");

			ArrayList<Node> nl = w.getNodeList();
			for (int j = 0; j < nl.size(); j++) {
				Node n = nl.get(j);
				bw.write(n.getName());
				bw.newLine();
				bw.write(n.getStatus());
				bw.newLine();
				bw.write(n.getAdj().toString());
				bw.newLine();
			}
			bw.newLine();
		}
		bw.close();
	}

	public static void createFilesForEachStateQValues(
			HashMap<State, List<List<QValue>>> map) throws IOException {

		int i = 1;
		Set set = map.keySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			State s = (State) itr.next();
			WState w = (WState) s;

			String name = ADDRESS + "/states/" + i++ + ".text";
			File fout = new File(name);
			FileOutputStream fos = new FileOutputStream(fout, true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			ArrayList<Node> nl = w.getNodeList();
			for (int j = 0; j < nl.size(); j++) {
				Node n = nl.get(j);
				bw.write(n.getName());
				bw.newLine();
				bw.write(n.getStatus());
				bw.newLine();
				bw.write(n.getAdj().toString());
				bw.newLine();
			}
			bw.newLine();
			List<List<QValue>> q = map.get(s);
			for (int x = 0; x < q.size(); x++) {
				List<QValue> l = q.get(x);
				for (int y = 0; y < l.size(); y++) {
					String text = l.get(y).a.actionName() + ":" + l.get(y).q
							+ ",";
					bw.write(text);
				}
				bw.newLine();
				bw.newLine();
			}
			bw.close();
		}

	}

	public static void writeFile(List ls, String name, int number)
			throws IOException {
		File fout = new File(name);
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		bw.write("Episode");
		bw.write(Integer.toString(number));
		bw.newLine();
		if (name.equals(ADDRESS + "state.text")) {
			List<State> l = (List<State>) ls;
			for (int j = 0; j < l.size(); j++) {
				WState s = (WState) l.get(j);
				bw.write(s.getNodeList().toString());
				bw.newLine();
			}
		} else if (name.equals(ADDRESS + "reward.text")) {
			List<Double> l = (List<Double>) ls;
			for (int j = 0; j < l.size(); j++) {
				bw.write(l.get(j) + ",");
			}
			bw.newLine();
		} else {
			List<Action> l = (List<Action>) ls;
			for (int j = 0; j < l.size(); j++) {
				bw.write(l.get(j).actionName() + ",");
			}
			bw.newLine();
		}
		bw.newLine();

		bw.close();
	}

	public static void writeConfig(String name) throws IOException {
		File fout = new File(name);
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		bw.write("Trials-" + trials);
		bw.newLine();
		bw.write("Learning Rate-" + learningrate);
		bw.newLine();
		bw.write("Epsilon-" + epsilon);
		bw.newLine();
		bw.write("Discount Factor-" + gamma);
		bw.newLine();
		bw.close();
	}

	public static void clear(String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(name);
		writer.print("");
		writer.close();
	}

	static void purgeDirectory(String str) {
		File dir = new File(str);
		for (File file : dir.listFiles()) {
			file.delete();
		}
	}

}
