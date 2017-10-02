package database;

import mainClass.MainClass;
import classes.NetworkComponent;
import classes.Node;
import classes.Vulnerability;

public class InstallDatabase {

	public static void install() {
		ConfigureDatabase.loadVulnerabilities();
		ConfigureDatabase.loadNetworkComponent();
		ConfigureDatabase.loadNetworkComponentToVul();
		ConfigureDatabase.loadComponentToComponent();
		ConfigureDatabase.loadFunctions();
		ConfigureDatabase.loadFunctionToFunctions();
		ConfigureDatabase.loadFuntionToComponent();
	}

	public static void print() {
		for (int i = 0; i < MainClass.vulnerabilityList.size(); i++) {
			Vulnerability v = MainClass.vulnerabilityList.get(i);
			v.print();
			System.out.println("");
		}

		for (int i = 0; i < MainClass.networkList.size(); i++) {
			NetworkComponent v = MainClass.networkList.get(i);
			v.print();
			System.out.println("");
		}

		for (int i = 0; i < MainClass.nodeList.size(); i++) {
			Node v = MainClass.nodeList.get(i);
			v.print();
			System.out.println("");
		}
	}

}
