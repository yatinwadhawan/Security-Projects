import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.Domain;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class TestBurlap implements DomainGenerator {

	public static final String VAR_X = "x";
	public static final String VAR_Y = "y";
	public static final String ACTION_NORTH = "north";
	public static final String ACTION_SOUTH = "south";
	public static final String ACTION_EAST = "east";
	public static final String ACTION_WEST = "west";
	protected int goalx = 10;
	protected int goaly = 10;

	protected int[][] map = new int[][] { { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, };

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public static void main(String[] args) {

		TestBurlap gen = new TestBurlap();
		gen.setGoalLocation(10, 10);
		SADomain domain = (SADomain) gen.generateDomain();
		State initialState = new GridState(0, 0);
		// SimulatedEnvironment env = new SimulatedEnvironment(domain,
		// initialState);
		boolean flag = false;

		if (flag) {

			// Planning MDP
			VITutorial vi = new VITutorial(domain, 0.99,
					new SimpleHashableStateFactory(),
					new ConstantValueFunction(0.0), 30);

			// run planning from our initial state
			Policy p = vi.planFromState(initialState);

			// evaluate the policy with one roll out visualize the trajectory
			Episode ea = PolicyUtils
					.rollout(p, initialState, domain.getModel());
			System.out.println(ea.actionString());
			System.out.println(ea.rewardSequence);
			System.out.println(ea.stateSequence);

			Visualizer v = GridWorldVisualizer.getVisualizer(gen.getMap());
			new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));

		} else {

			// Q-Learning
			SimulatedEnvironment env = new SimulatedEnvironment(domain,
					initialState);
			QLTutorial agent = new QLTutorial(domain, 0.99,
					new SimpleHashableStateFactory(),
					new ConstantValueFunction(), 0.3, 0.1);

			// run Q-learning and store results in a list
			List<Episode> episodes = new ArrayList<Episode>(1000);
			for (int i = 0; i < 1000; i++) {
				episodes.add(agent.runLearningEpisode(env));
				env.resetEnvironment();
			}

			Visualizer v = GridWorldVisualizer.getVisualizer(gen.getMap());
			new EpisodeSequenceVisualizer(v, domain, episodes);

		}
	}

	@Override
	public Domain generateDomain() {
		SADomain domain = new SADomain();
				
		domain.addActionTypes(new UniversalActionType(ACTION_NORTH),
				new UniversalActionType(ACTION_SOUTH), new UniversalActionType(
						ACTION_EAST), new UniversalActionType(ACTION_WEST));

		GridStateModel smodel = new GridStateModel();
		RewardFunction rf = new GridRewardModel(this.goalx, this.goaly);
		TerminalFunction tf = new GridTerminalModel(this.goalx, this.goaly);

		domain.setModel(new FactoredModel(smodel, rf, tf));

		return domain;
	}

	public void setGoalLocation(int goalx, int goaly) {
		this.goalx = goalx;
		this.goaly = goaly;
	}

	protected class GridStateModel implements FullStateModel {

		protected double[][] transitionProbs;

		public GridStateModel() {
			this.transitionProbs = new double[4][4];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					double p = i != j ? 0.2 / 3 : 0.8;
					transitionProbs[i][j] = p;
				}
			}
		}

		@Override
		public State sample(State s, Action a) {
			s = s.copy();
			GridState gs = (GridState) s;
			int curX = gs.x;
			int curY = gs.y;

			int adir = actionDir(a);

			// sample direction with random roll
			double r = Math.random();
			double sumProb = 0.;
			int dir = 0;
			for (int i = 0; i < 4; i++) {
				sumProb += this.transitionProbs[adir][i];
				if (r < sumProb) {
					dir = i;
					break; // found direction
				}
			}

			// get resulting position
			int[] newPos = this.moveResult(curX, curY, dir);

			// set the new position
			gs.x = newPos[0];
			gs.y = newPos[1];

			// return the state we just modified
			return gs;
		}

		@Override
		public List<StateTransitionProb> stateTransitions(State s, Action a) {

			// get agent current position
			GridState gs = (GridState) s;

			int curX = gs.x;
			int curY = gs.y;

			int adir = actionDir(a);

			List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>(
					4);
			StateTransitionProb noChange = null;
			for (int i = 0; i < 4; i++) {

				int[] newPos = this.moveResult(curX, curY, i);
				if (newPos[0] != curX || newPos[1] != curY) {
					// new possible outcome
					GridState ns = (GridState) gs.copy();
					ns.x = newPos[0];
					ns.y = newPos[1];

					// create transition probability object and add to our list
					// of outcomes
					tps.add(new StateTransitionProb(ns,
							this.transitionProbs[adir][i]));
				} else {
					// this direction didn't lead anywhere new
					// if there are existing possible directions
					// that wouldn't lead anywhere, aggregate with them
					if (noChange != null) {
						noChange.p += this.transitionProbs[adir][i];
					} else {
						// otherwise create this new state and transition
						noChange = new StateTransitionProb(s.copy(),
								this.transitionProbs[adir][i]);
						tps.add(noChange);
					}
				}

			}

			return tps;
		}

		protected int[] moveResult(int curX, int curY, int direction) {

			// first get change in x and y from direction using 0: north; 1:
			// south; 2:east; 3: west
			int xdelta = 0;
			int ydelta = 0;
			if (direction == 0) {
				ydelta = 1;
			} else if (direction == 1) {
				ydelta = -1;
			} else if (direction == 2) {
				xdelta = 1;
			} else {
				xdelta = -1;
			}

			int nx = curX + xdelta;
			int ny = curY + ydelta;

			int width = TestBurlap.this.map.length;
			int height = TestBurlap.this.map[0].length;

			// make sure new position is valid (not a wall or off bounds)
			if (nx < 0 || nx >= width || ny < 0 || ny >= height
					|| TestBurlap.this.map[nx][ny] == 1) {
				nx = curX;
				ny = curY;
			}

			return new int[] { nx, ny };

		}

		protected int actionDir(Action a) {
			int adir = -1;
			if (a.actionName().equals(ACTION_NORTH)) {
				adir = 0;
			} else if (a.actionName().equals(ACTION_SOUTH)) {
				adir = 1;
			} else if (a.actionName().equals(ACTION_EAST)) {
				adir = 2;
			} else if (a.actionName().equals(ACTION_WEST)) {
				adir = 3;
			}
			return adir;
		}
	}

	protected class GridRewardModel implements RewardFunction {

		int goalX;
		int goalY;

		public GridRewardModel(int goalX, int goalY) {
			this.goalX = goalX;
			this.goalY = goalY;
		}

		@Override
		public double reward(State s, Action a, State sprime) {

			int ax = (Integer) s.get(VAR_X);
			int ay = (Integer) s.get(VAR_Y);

			// are they at goal location?
			if (ax == this.goalX && ay == this.goalY) {
				return 100.;
			}

			return -1;
		}
	}

	protected class GridTerminalModel implements TerminalFunction {

		int goalX;
		int goalY;

		public GridTerminalModel(int goalX, int goalY) {
			this.goalX = goalX;
			this.goalY = goalY;
		}

		@Override
		public boolean isTerminal(State s) {

			// get location of agent in next state
			int ax = (Integer) s.get(VAR_X);
			int ay = (Integer) s.get(VAR_Y);

			// are they at goal location?
			if (ax == this.goalX && ay == this.goalY) {
				return true;
			}

			return false;
		}
	}

}
