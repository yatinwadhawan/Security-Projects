import java.util.ArrayList;
import java.util.List;

import Graph.Graph;
import Graph.Node;
import Graph.NodeStatus;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.Domain;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class WorldGenerator implements DomainGenerator {

	@Override
	public Domain generateDomain() {

		SADomain domain = new SADomain();
		for (int i = 0; i < MainClass.nlist.size(); i++) {

			MAction ms = new MAction(MainClass.nlist.get(i),
					MainClass.ACTION_SCAN);
			MAction mp = new MAction(MainClass.nlist.get(i),
					MainClass.ACTION_PATCH);
			domain.addActionType(ms);
			domain.addActionType(mp);
		}
		// System.out.println(domain.getActionTypes());

		StateWorld smodel = new StateWorld();
		Reward rf = new Reward();
		Terminal tf = new Terminal();

		domain.setModel(new FactoredModel(smodel, rf, tf));

		return domain;
	}

	protected class StateWorld implements FullStateModel {

		@Override
		public State sample(State s, Action a) {

			MAction ac = (MAction) a;
			Node n = ac.getNode();
			String action = ac.getAction();
			State w = s.copy();
			WState state = (WState) w;
			Graph g = state.getGraph();
			int size = g.getNodelist().size();
			int index = 0;

			// Find the index of the node in the graph on which action is
			// performed.
			for (int i = 0; i < size; i++) {
				if (n.getName().equals(g.getNodelist().get(i).getName())) {
					index = i;
					break;
				}
			}

			// Perform Specific action on the node.
			if (action.equals(MainClass.ACTION_SCAN)) {

				// If status is UNKNOWN
				if (n.getStatus().equals(NodeStatus.UNKNOWN)) {
					g.getNodelist().get(index).setPstatus(NodeStatus.UNKNOWN);
					double rand = Math.random();
					if (rand > 0.5) {
						g.getNodelist().get(index)
								.setStatus(NodeStatus.PATCHED);
					} else {
						g.getNodelist().get(index)
								.setStatus(NodeStatus.VULNERABLE);
					}
					// If status is PATCHED
				} else if (n.getStatus().equals(NodeStatus.PATCHED)

				) {
					g.getNodelist().get(index).setPstatus(NodeStatus.PATCHED);
					double rand = Math.random();
					if (rand > 0.6) {
						g.getNodelist().get(index)
								.setStatus(NodeStatus.PATCHED);
					} else {
						g.getNodelist().get(index)
								.setStatus(NodeStatus.VULNERABLE);
					}

					// If status is VULNERABLE
				} else if (n.getStatus().equals(NodeStatus.VULNERABLE)) {
					g.getNodelist().get(index)
							.setPstatus(NodeStatus.VULNERABLE);
					g.getNodelist().get(index).setStatus(NodeStatus.VULNERABLE);

				}

			} else if (action.equals(MainClass.ACTION_PATCH)) {

				// If status is UNKNOWN
				if (n.getStatus().equals(NodeStatus.UNKNOWN)) {
					// Only scan is possible. No patching allowed since defender
					// does not know what to patch.
					g.getNodelist().get(index).setStatus(NodeStatus.UNKNOWN);

					// If status is PATCHED
				} else if (n.getStatus().equals(NodeStatus.PATCHED)) {
					// The node is already patched. Defender can only scan to
					// know whether it is vulnerable or not. Later we
					// will introduce Unknown state over a period of time.
					g.getNodelist().get(index).setStatus(NodeStatus.PATCHED);

					// If status is VULNERABLE
				} else if (n.getStatus().equals(NodeStatus.VULNERABLE)) {
					// Defender will patch the node and change the state of the
					// node.
					g.getNodelist().get(index)
							.setPstatus(NodeStatus.VULNERABLE);
					g.getNodelist().get(index).setStatus(NodeStatus.PATCHED);
				}

			}
			state.setGraph(g);
			return state;
		}

		@Override
		public List<StateTransitionProb> stateTransitions(State s, Action a) {

			return null;
		}
	}

	protected class Reward implements RewardFunction {

		@Override
		public double reward(State s, Action a, State sp) {

			// Fill this function
			MAction ac = (MAction) a;
			Node na = ac.getNode();
			String action = ac.getAction();

			State w = s.copy();
			WState state = (WState) w;
			Graph g = state.getGraph();
			int size = g.getNodelist().size();
			Node n = null;

			State wp = sp.copy();
			WState statep = (WState) wp;
			Graph gp = statep.getGraph();
			int sizep = gp.getNodelist().size();
			Node np = null;

			// Find the index of the node in the graph on which action is
			// performed.
			for (int i = 0; i < size; i++) {
				if (na.getName().equals(g.getNodelist().get(i).getName())) {
					n = g.getNodelist().get(i);
					break;
				}
			}
			for (int i = 0; i < sizep; i++) {
				if (na.getName().equals(gp.getNodelist().get(i).getName())) {
					np = gp.getNodelist().get(i);
					break;
				}
			}

			if (action.equals(MainClass.ACTION_SCAN)) {

				if (np.getPstatus().equals(NodeStatus.VULNERABLE)) {
					return MainClass.reward.get(np.getName()) / 8;
				} else if (np.getPstatus().equals(NodeStatus.UNKNOWN)) {
					// We distinguish between the state in which
					// node lands.. whether Vulnerable or Patched
					if (np.getStatus().equals(NodeStatus.PATCHED)) {
						return MainClass.reward.get(np.getName()) / 4;
					} else {
						return MainClass.reward.get(np.getName()) / 2;
					}
				} else if (np.getPstatus().equals(NodeStatus.PATCHED)) {
					// We distinguish between the state in which
					// node lands.. whether Vulnerable or Patched
					if (np.getStatus().equals(NodeStatus.PATCHED)) {
						return MainClass.reward.get(np.getName()) / 4;
					} else {
						return MainClass.reward.get(np.getName()) / 2;
					}
				}

			} else if (action.equals(MainClass.ACTION_PATCH)) {

				if (np.getPstatus().equals(NodeStatus.VULNERABLE)) {
					return MainClass.reward.get(np.getName());
				} else if (np.getPstatus().equals(NodeStatus.PATCHED)) {
					return MainClass.reward.get(np.getName()) / 8;
				} else if (np.getPstatus().equals(NodeStatus.UNKNOWN)) {
					return MainClass.reward.get(np.getName()) / 8;
				}
			}

			return 0;
		}
	}

	protected class Terminal implements TerminalFunction {

		@Override
		public boolean isTerminal(State s) {

			WState state = (WState) s;
			Graph g = state.getGraph();
			ArrayList<Node> l = g.getNodelist();
			int size = l.size();
			for (int i = 0; i < size; i++) {
				Node n = l.get(i);
				if (n.getStatus().equals(NodeStatus.VULNERABLE)
						|| n.getStatus().equals(NodeStatus.UNKNOWN)) {
					return false;
				}
			}

			return true;
		}
	}

}
