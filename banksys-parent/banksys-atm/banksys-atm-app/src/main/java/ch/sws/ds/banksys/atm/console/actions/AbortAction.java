package ch.sws.ds.banksys.atm.console.actions;

import ch.sws.ds.banksys.common.console.Action;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.common.console.Session;

/**
 * @author kambl1
 *
 * Action zum invalidieren des Einloggvorgangs.
 */
public class AbortAction extends Action {
	private static final String ACTION_NAME = "Abort";

	public AbortAction(Screen nextScreen) {
		super(ACTION_NAME, nextScreen);
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Action#doIt(java.lang.String)
	 */
	@Override
	protected boolean doIt(String value) {
		Session.getInstance().clearSession();
		return true;
	}
}
