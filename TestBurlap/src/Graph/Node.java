package Graph;

import java.util.ArrayList;

public class Node {

	private String name;
	private String function;
	private NodeStatus status;
	private double cProb;
	private ArrayList<Node> adj;

	public Node(String name, String function, NodeStatus s, double p,
			ArrayList<Node> adj) {
		this.name = name;
		this.function = function;
		this.status = s;
		this.cProb = p;
		this.adj = adj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public NodeStatus getStatus() {
		return status;
	}

	public void setStatus(NodeStatus status) {
		this.status = status;
	}

	public double getcProb() {
		return cProb;
	}

	public void setcProb(double cProb) {
		this.cProb = cProb;
	}

	public ArrayList<Node> getAdj() {
		return adj;
	}

	public void setAdj(ArrayList<Node> adj) {
		this.adj = adj;
	}

}
