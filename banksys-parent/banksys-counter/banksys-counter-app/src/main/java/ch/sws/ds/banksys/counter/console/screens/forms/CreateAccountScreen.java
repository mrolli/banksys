package ch.sws.ds.banksys.counter.console.screens.forms;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.CreateAccountAction;
import ch.sws.ds.banksys.counter.console.screens.errors.CreateAccountError;
import ch.sws.ds.banksys.counter.console.screens.messages.CreateAccountMessage;

/**
 * @author kambl1
 *
 * Eingabemaske um ein neues Bankkonto zu erstellen.
 */
public class CreateAccountScreen extends FormScreen {
	private static final String SCREEN_NAME = "Create Account";

	public CreateAccountScreen(Screen parentMenu) {
		super(SCREEN_NAME);
		addFormStep("Customer number", "Enter a customer number");
		addFormStep("Customer PIN", "Enter the customers PIN");
		addFormStep("Account PIN", "Enter a PIN for the account");
		addFormStep("Descripton", "Enter an account description");
		
		MessageScreen message = new CreateAccountMessage(parentMenu, getValueList());
		ErrorScreen error = new CreateAccountError(this, parentMenu);
		FormAction formAction = new CreateAccountAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
}
