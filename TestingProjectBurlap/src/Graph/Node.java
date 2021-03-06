package Graph;

import java.util.ArrayList;
import java.util.Objects;

public class Node {

	private String name;
	private String status;
	private ArrayList<Node> adj;
	private String dstatus;
	private String astatus;

	// private String pstatus;

	public Node() {
	}

	public Node(String name, String s) {
		this.name = name;
		this.status = s;
		this.dstatus = s;
		this.astatus = s;
		// this.pstatus = "";
	}

	public Node copy() {
		Node copy = new Node();
		copy.name = this.name;
		copy.status = this.status;
		copy.dstatus = this.dstatus;
		copy.astatus = this.astatus;
		// copy.pstatus = this.pstatus;
		copy.adj = (ArrayList<Node>) this.adj.clone();
		return copy;
	}

	public static void print(Node n) {
		System.out.println("Name - " + n.getName());
		// System.out.println("Previous Status - " + n.getPstatus());
		System.out.println("Status - " + n.getStatus());
		System.out.println("DStatus - " + n.getDstatus());
		System.out.println("AStatus - " + n.getAstatus());
		System.out.println("Adjacency List - " + n.getAdj());
	}

	public void print() {
		System.out.println("Name - " + this.name);
		// System.out.println("Previous Status - " + this.pstatus);
		System.out.println("Status - " + this.status);
		System.out.println("DStatus - " + this.dstatus);
		System.out.println("AStatus - " + this.astatus);
		System.out.println("Adjacency List - " + this.adj);
	}

	@Override
	public boolean equals(Object n) {
		// TODO Auto-generated method stub

		if (n instanceof Node) {
			Node node = (Node) n;
			if (node.name == this.name && node.status == this.status
					&& this.dstatus == node.dstatus
					&& this.astatus == node.astatus) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, status, dstatus, astatus, adj);
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

	public String getDstatus() {
		return dstatus;
	}

	public void setDstatus(String dstatus) {
		this.dstatus = dstatus;
	}

	public String getAstatus() {
		return astatus;
	}

	public void setAstatus(String astatus) {
		this.astatus = astatus;
	}

}
