import java.util.List;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.Domain;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class WorldGenerator implements DomainGenerator {

	@Override
	public Domain generateDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	protected class StateWorld implements FullStateModel {

		@Override
		public State sample(State s, Action a) {

			s = s.copy();
			WState state = (WState) s;
			
			return null;
		}

		@Override
		public List<StateTransitionProb> stateTransitions(State arg0,
				Action arg1) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	protected class Reward implements RewardFunction {

		@Override
		public double reward(State arg0, Action arg1, State arg2) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	protected class Terminal implements TerminalFunction {

		@Override
		public boolean isTerminal(State arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
