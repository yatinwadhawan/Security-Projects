package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import mainClass.MainClass;
import classes.Node;
import classes.RewardVariables;

public class NodesImportance {

	// Create Rewards List using Importance and Reward specified by the
	// defender.

	public static void rewardFunction() throws IOException {

		File fout = new File(MainClass.ADDRESS + "rewards.text");
		FileOutputStream fos = new FileOutputStream(fout, true);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i < MainClass.nodeList.size(); i++) {

			double reward = 30.0;
			Node n = MainClass.nodeList.get(i);
			ArrayList<RewardVariables> ls = MainClass.rewardValueMap.get(n
					.getSymbol());

			for (int j = 0; j < ls.size(); j++) {
				RewardVariables r = ls.get(j);
				if (!r.getSymbol().equals("RO"))
					reward = reward * r.getValue();
			}
			String str = n.getSymbol() + " - " + n.getName() + " - " + reward;
			System.out.println(str);

			bw.write(str);
			bw.newLine();

			MainClass.reward.put(n.getName(), reward);
		}

		bw.close();
	}

	// Adding rewards on the basis of random values assigned to each function
	public static void alternateRewardFunction() {
		for (int i = 0; i < MainClass.nodeList.size(); i++) {
			Node n = MainClass.nodeList.get(i);
			MainClass.reward.put(n.getName(), n.getReward());
		}
	}

}
