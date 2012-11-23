package ch.sws.ds.banksys.atm.console.screens.forms;

import ch.sws.ds.banksys.atm.console.actions.LoginAction;
import ch.sws.ds.banksys.atm.console.screens.errors.LoginError;
import ch.sws.ds.banksys.atm.console.screens.menus.AtmMenu;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.common.console.utils.BankcardManager;

public class WelcomeScreen extends FormScreen {
	private static final String SCREEN_NAME = "Welcome";
	
	public WelcomeScreen() {
		super(SCREEN_NAME);
		addFormStep("PIN", "Enter the PIN for the simulated bankcard");
		
		Screen message = new AtmMenu(this);
		ErrorScreen error = new LoginError(this);
		FormAction formAction = new LoginAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Screen#onShow()
	 */
	@Override
	public void onShow() {
		addText("");
		addText("The bankcard is simulated with a properties file.");
		addText("The file '" + BankcardManager.getBankcardFileName() + "' should be in the folder '" + BankcardManager.getBankcardFolder() + "'.");
	}
}
