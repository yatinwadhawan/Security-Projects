package classes;

import java.util.Objects;

import burlap.mdp.core.action.Action;

public class MAction implements Action {

	// We have three fields so that to we can decide what action is taken on
	// which node.
	private String nodeName;
	private String action;
	private String actionName;

	public MAction(String n, String action) {
		this.nodeName = n;
		this.action = action;
		this.actionName = n + "-" + action;
	}

	@Override
	public boolean equals(Object obj) {
		MAction that = (MAction) obj;
		if (that.nodeName == this.nodeName && that.action == this.action
				&& that.actionName == this.actionName) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nodeName, action, actionName);
	}

	@Override
	public String actionName() {
		return this.actionName;
	}

	@Override
	public Action copy() {
		return new MAction(this.nodeName, this.action);
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
