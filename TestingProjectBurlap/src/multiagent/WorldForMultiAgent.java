package multiagent;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Graph.Node;
import Graph.NodeStatus;
import QLearning.MAction;
import QLearning.MainClass;
import QLearning.WState;
import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.stochasticgames.GameEpisode;
import burlap.behavior.stochasticgames.PolicyFromJointPolicy;
import burlap.behavior.stochasticgames.agents.maql.MultiAgentQLearning;
import burlap.behavior.stochasticgames.agents.twoplayer.singlestage.equilibriumplayer.equilibriumsolvers.CorrelatedEquilibrium;
import burlap.behavior.stochasticgames.agents.twoplayer.singlestage.equilibriumplayer.equilibriumsolvers.MinMax;
import burlap.behavior.stochasticgames.auxiliary.GameSequenceVisualizer;
import burlap.behavior.stochasticgames.madynamicprogramming.AgentQSourceMap;
import burlap.behavior.stochasticgames.madynamicprogramming.QSourceForSingleAgent;
import burlap.behavior.stochasticgames.madynamicprogramming.backupOperators.CorrelatedQ;
import burlap.behavior.stochasticgames.madynamicprogramming.backupOperators.MinMaxQ;
import burlap.behavior.stochasticgames.madynamicprogramming.policies.ECorrelatedQJointPolicy;
import burlap.behavior.stochasticgames.madynamicprogramming.policies.EGreedyJointPolicy;
import burlap.behavior.stochasticgames.madynamicprogramming.policies.EGreedyMaxWellfare;
import burlap.behavior.stochasticgames.madynamicprogramming.policies.EMinMaxPolicy;
import burlap.behavior.stochasticgames.solvers.CorrelatedEquilibriumSolver;
import burlap.behavior.stochasticgames.solvers.CorrelatedEquilibriumSolver.CorrelatedEquilibriumObjective;
import burlap.domain.stochasticgames.gridgame.GGVisualizer;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.stochasticgames.JointAction;
import burlap.mdp.stochasticgames.SGDomain;
import burlap.mdp.stochasticgames.agent.SGAgentType;
import burlap.mdp.stochasticgames.world.World;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class WorldForMultiAgent {

	final double discount = 0.6;
	final double learningRate = 0.6;
	final double defaultQ = 10;
	double epsilon = 0.8;
	int ngames = 500;
	public static int windef = 0;
	public static boolean isDefWin = false;

	public static boolean isDefenderAttackerAccessingSameNode = false;
	public static SAgentType defender, attacker;
	public static Node attackerNode, defenderNode;

	public void createAndRunGameModel() {

		PrintStream originalStream = System.out;

		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		JointWorldGenerator joint = new JointWorldGenerator();
		SGDomain domain = (SGDomain) joint.generateDomain();

		Reward rf = new Reward();
		Terminal tf = new Terminal();
		WState initialState = new WState(MainClass.nlist);

		// Creating Defender object with all the actions available to him.
		defender = new SAgentType("Defender", 0,
				JointWorldGenerator.getDefenderActionList());

		// Adding initial actions of the attacker.
		attacker = new SAgentType("Attacker", 1, new ArrayList<ActionType>());

		World w = new World(domain, rf, tf, initialState);

		MultiAgentQLearning dagent = new MultiAgentQLearning(domain, discount,
				learningRate, hashingFactory, defaultQ, new CorrelatedQ(
						CorrelatedEquilibriumObjective.UTILITARIAN), true,
				defender.getTypeName(), defender);
		dagent.agentNum = 0;

		MultiAgentQLearning aagent = new MultiAgentQLearning(domain, discount,
				learningRate, hashingFactory, defaultQ, new CorrelatedQ(
						CorrelatedEquilibriumObjective.UTILITARIAN), true,
				attacker.getTypeName(), attacker);
		aagent.agentNum = 1;

		w.join(dagent);
		w.join(aagent);

		ECorrelatedQJointPolicy policy = new ECorrelatedQJointPolicy(
				CorrelatedEquilibriumObjective.UTILITARIAN, epsilon);
		PolicyFromJointPolicy dp = new PolicyFromJointPolicy(policy, true);
		PolicyFromJointPolicy ap = new PolicyFromJointPolicy(policy, true);

		dp.setSynchronizeJointActionSelectionAmongAgents(true);
		ap.setSynchronizeJointActionSelectionAmongAgents(true);
		dp.setActingAgent(0);
		ap.setActingAgent(1);

		aagent.setLearningPolicy(ap);
		dagent.setLearningPolicy(dp);

		System.out.println("Starting training");
		// System.setOut(new PrintStream(new OutputStream() {
		// public void write(int b) {
		// // NO-OP
		// }
		// }));

		ArrayList<Integer> randomNum = new ArrayList<Integer>();

		List<GameEpisode> games = new ArrayList<GameEpisode>();
		for (int i = 0; i < ngames; i++) {

			isDefWin = false;
			int random = ThreadLocalRandom.current().nextInt(0, 4 + 1);
			randomNum.add(random);
			defenderNode = null;
			attackerNode = null;
			attacker.clearActions();
			MAction ms = new MAction(MainClass.nlist.get(random).getName(),
					MainClass.ACTION_SCAN);
			MAction mp = new MAction(MainClass.nlist.get(random).getName(),
					MainClass.ACTION_HACK);
			attacker.addAction(ms);
			attacker.addAction(mp);
			attacker.updateActionList(random);

			for (int j = 0; j < MainClass.nlist.size(); j++) {
				initialState.getNodeList().get(j)
						.setDstatus(NodeStatus.UNKNOWN);
				initialState.getNodeList().get(j)
						.setAstatus(NodeStatus.UNKNOWN);
				initialState.getNodeList().get(j).setStatus(NodeStatus.UNKNOWN);
			}
			// Initially attacker has compromised the first node which is the
			// starting point for the attacker.
			initialState.getNodeList().get(random)
					.setDstatus(NodeStatus.HACKED);
			initialState.getNodeList().get(random)
					.setAstatus(NodeStatus.HACKED);
			initialState.getNodeList().get(random).setStatus(NodeStatus.HACKED);

			w.setCurrentState(initialState);

			System.out.println("Start Node: "
					+ initialState.getNodeList().get(random).getName());
			System.out.println("");

			GameEpisode ga = w.runGame();
			if (isDefWin)
				windef++;
			games.add(ga);
		}

		System.setOut(originalStream);
		System.out.println("Finished training");
		System.out.println("");

		System.out.println("Analysis Starts");
		ArrayList<WState> ls = new ArrayList<WState>();
		ArrayList<Double> display = new ArrayList<Double>();
		for (int i = 0; i < ngames; i++) {
			GameEpisode g = games.get(i);
			List<State> temp = g.getStates();

			for (int j = 0; j < temp.size(); j++) {
				if (!ls.contains(temp.get(j))) {
					ls.add((WState) temp.get(j));
				}
			}

			double defreward = 0.0, attrewards = 0.0;
			List<double[]> ld = g.getJointRewards();
			for (int j = 0; j < ld.size(); j++) {
				defreward += ld.get(j)[0];
				attrewards += ld.get(j)[1];
			}
			display.add(defreward / ld.size());
		}

		System.out.println("Random Numbers Choosen: " + randomNum);
		System.out.println("Number of states covered: " + ls.size());

		System.out.println("Number of Games: " + ngames);
		System.out.println("Number of Games won by defender: " + windef);
		System.out.println("Number of Games won by Attacker: "
				+ (ngames - windef));
		System.out
				.println(policy.qSourceProvider.getQSources().agentQSource(0));
	}
}
