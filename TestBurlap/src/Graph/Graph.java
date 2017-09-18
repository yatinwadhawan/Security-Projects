package Graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

	private int nodes;
	private int edges;
	private ArrayList<Node> nodelist;
	private Node startNode;
	private Node endNode;

	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public int getEdges() {
		return edges;
	}

	public void setEdges(int edges) {
		this.edges = edges;
	}

	public ArrayList<Node> getNodelist() {
		return nodelist;
	}

	public void setNodelist(ArrayList<Node> nodelist) {
		this.nodelist = nodelist;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

}
