package org.asteriskjava.pbx.internal.asterisk.wrap.actions;

public interface ManagerAction
{

	// Converts an iManagerAction into an asterisk-java ManagerAction.
	org.asteriskjava.manager.action.ManagerAction getAJAction();

}
