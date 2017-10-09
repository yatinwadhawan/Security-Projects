package database;

import java.util.ArrayList;

import mainClass.MainClass;
import classes.Node;
import classes.RewardVariables;

public class NodesImportance {

	// Create Rewards List using Importance and Reward specified by the
	// defender.

	public static void rewardFunction() {

		for (int i = 0; i < MainClass.nodeList.size(); i++) {

			double reward = 10.0;
			Node n = MainClass.nodeList.get(i);
			ArrayList<RewardVariables> ls = MainClass.rewardValueMap.get(n
					.getSymbol());

			for (int j = 0; j < ls.size(); j++) {
				RewardVariables r = ls.get(j);
				if (!r.getSymbol().equals("RO"))
					reward = reward * r.getValue();
			}
			System.out
					.println(n.getName() + "-" + n.getSymbol() + "-" + reward);
			MainClass.reward.put(n.getName(), reward);
		}

	}

	public static void alternateRewardFunction() {
		for (int i = 0; i < MainClass.nodeList.size(); i++) {
			Node n = MainClass.nodeList.get(i);
			MainClass.reward.put(n.getName(), n.getReward());
		}
	}

}
