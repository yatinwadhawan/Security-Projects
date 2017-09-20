package Graph;

import java.util.ArrayList;

public class Node {

	private String name;
	private String function;
	private String status;
	private double cProb;
	private ArrayList<Node> adj;
	private String pstatus;

	public String getPstatus() {
		return pstatus;
	}

	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}

	public Node(String name, String function, String s, double p) {
		this.name = name;
		this.function = function;
		this.status = s;
		this.cProb = p;
		this.pstatus = "";
	}

	public static void print(Node n) {
		System.out.println("Name - " + n.getName());
		System.out.println("Function - " + n.getFunction());
		System.out.println("Probability - " + n.getcProb());
		System.out.println("Previous Status - " + n.getPstatus());
		System.out.println("Status - " + n.getStatus());
		System.out.println("Adjacency List - " + n.getAdj());
	}

	public void print() {
		System.out.println("Name - " + this.name);
		System.out.println("Function - " + this.function);
		System.out.println("Probability - " + this.cProb);
		System.out.println("Previous Status - " + this.pstatus);
		System.out.println("Status - " + this.status);
		System.out.println("Adjacency List - " + this.adj);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
