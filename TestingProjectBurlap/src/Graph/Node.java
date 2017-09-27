package Graph;

import java.util.ArrayList;
import java.util.Objects;

public class Node {

	private String name;
	private String status;
	private ArrayList<Node> adj;

	// private String pstatus;

	public Node() {
	}

	public Node(String name, String s) {
		this.name = name;
		this.status = s;
		// this.pstatus = "";
	}

	public Node copy() {
		Node copy = new Node();
		copy.name = this.name;
		copy.status = this.status;
		// copy.pstatus = this.pstatus;
		copy.adj = (ArrayList<Node>) this.adj.clone();
		return copy;
	}

	public static void print(Node n) {
		System.out.println("Name - " + n.getName());
		// System.out.println("Previous Status - " + n.getPstatus());
		System.out.println("Status - " + n.getStatus());
		System.out.println("Adjacency List - " + n.getAdj());
	}

	public void print() {
		System.out.println("Name - " + this.name);
		// System.out.println("Previous Status - " + this.pstatus);
		System.out.println("Status - " + this.status);
		System.out.println("Adjacency List - " + this.adj);
	}

	@Override
	public boolean equals(Object n) {
		// TODO Auto-generated method stub

		if (n instanceof Node) {
			Node node = (Node) n;
			if (node.name == this.name && node.status == this.status) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, status, adj);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Node> getAdj() {
		return adj;
	}

	public void setAdj(ArrayList<Node> adj) {
		this.adj = adj;
	}

}
