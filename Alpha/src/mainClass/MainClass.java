package mainClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import QLearning.DecisionMaking;
import QLearning.MAction;
import burlap.mdp.core.state.State;
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

	public static void main(String[] str) throws IOException {

		// Installing database into data structures from files
		InstallDatabase.install();
		InstallDatabase.print();

		// Adding actions and rewards
		NodeStatus.addActions();
		NodeStatus.createActionList();
		NodeStatus.createRewardList();

		// Calling QLearning Algorithm to make decision
		DecisionMaking.makeDecision();

		// Write algorithm to implement Bayesian Graph

	}

}