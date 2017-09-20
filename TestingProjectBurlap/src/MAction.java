import java.util.ArrayList;
import java.util.List;

import Graph.Graph;
import Graph.Node;
import Graph.NodeStatus;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

public class MAction implements ActionType, Action {

	// We have three fields so that to we can decide what action is taken on
	// which node.
	private Node node;
	private String name;
	private String action;

	public MAction(Node n, String action) {
		this.node = n;
		this.name = action + "-" + n.getName();
		this.action = action;
	}

	@Override
	public List<Action> allApplicableActions(State s) {

		WState state = (WState) s;
		Graph g = state.getGraph();
		List<Node> nls = g.getNodelist();
		List<Action> ls = new ArrayList<Action>();
		for (int i = 0; i < nls.size(); i++) {
			Node n = nls.get(i);
			if (n.getStatus().equals(NodeStatus.UNKNOWN)) {
				String action = MainClass.ACTION_SCAN;
				MAction m = new MAction(n, action);
				ls.add(m);
			} else if (n.getStatus().equals(NodeStatus.VULNERABLE)) {
				String action = MainClass.ACTION_PATCH;
				MAction m = new MAction(n, action);
				ls.add(m);
			} else if (n.getStatus().equals(NodeStatus.PATCHED)) {
				String action = MainClass.ACTION_SCAN;
				MAction m = new MAction(n, action);
				ls.add(m);
			}
		}
		// for (int i = 0; i < ls.size(); i++) {
		// System.out.print(ls.get(i).actionName() + ",");
		// }
		// System.out.println();
		return ls;
	}

	@Override
	public Action associatedAction(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String typeName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String actionName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public Action copy() {
		// TODO Auto-generated method stub
		return new MAction(this.node, this.name);
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
