package classes;

import java.util.ArrayList;

public class Node {
	// Node represents the function node that is displayed on the dashboard. The
	// responsibility of the defender is to perform resource allocation on these
	// node. For example scan node, patch node etc.

	private String symbol;
	private String name;
	private String function;
	private String status;
	private String pstatus;
	private double compromiseProb;
	private ArrayList<Node> adjList;
	private ArrayList<NetworkComponent> networkComponentList;

	public Node() {
		System.out.println("Node created");
	}

	public Node(String symbol, String name, String function, String status,
			String pstatus, double prob, ArrayList<Node> adj,
			ArrayList<NetworkComponent> nclist) {
		this.symbol = symbol;
		this.name = name;
		this.function = function;
		this.status = status;
		this.pstatus = pstatus;
		this.compromiseProb = prob;
		this.adjList = adj;
		this.networkComponentList = nclist;
	}

	public void print() {
		System.out.println("Name - " + this.symbol);
		System.out.println("Name - " + this.name);
		System.out.println("Function - " + this.function);
		System.out.println("Status - " + this.status);
		System.out.println("Previous Status - " + this.pstatus);
		System.out.println("Probability - " + this.compromiseProb);
		System.out.println("Adjacency List - " + this.adjList);
		System.out.println("Previous Status - " + this.networkComponentList);
	}

	public static void print(Node n) {
		System.out.println("Name - " + n.symbol);
		System.out.println("Name - " + n.name);
		System.out.println("Function - " + n.function);
		System.out.println("Status - " + n.status);
		System.out.println("Previous Status - " + n.pstatus);
		System.out.println("Probability - " + n.compromiseProb);
		System.out.println("Adjacency List - " + n.adjList);
		System.out.println("Previous Status - " + n.networkComponentList);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	public double getCompromiseProb() {
		return compromiseProb;
	}

	public void setCompromiseProb(double compromiseProb) {
		this.compromiseProb = compromiseProb;
	}

	public ArrayList<Node> getAdj() {
		return adjList;
	}

	public void setAdj(ArrayList<Node> adj) {
		this.adjList = adj;
	}

	public String getPstatus() {
		return pstatus;
	}

	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}

	public ArrayList<NetworkComponent> getNclist() {
		return networkComponentList;
	}

	public void setNclist(ArrayList<NetworkComponent> nclist) {
		this.networkComponentList = nclist;
	}

}