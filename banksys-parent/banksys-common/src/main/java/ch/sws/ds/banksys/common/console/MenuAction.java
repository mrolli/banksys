package ch.sws.ds.banksys.common.console;

/**
 * @author kambl1
 *
 * Die MenuAction überführt nur in einen nächsten Screen und kennt keinen Fehlerfall.
 */
public class MenuAction extends Action {
	/**
	 * @param name Name der Action
	 * @param nextScreen Nächster Screen
	 */
	public MenuAction(String name, Screen nextScreen) {
		super(name, nextScreen);
	}
	
	/**
	 * @param nextScreen Nächster Screen
	 */
	public MenuAction(Screen nextScreen) {
		super(nextScreen.getName(), nextScreen);
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Action#doIt(java.lang.String)
	 */
	@Override
	protected boolean doIt(String value) {
		return true;
	}
}
