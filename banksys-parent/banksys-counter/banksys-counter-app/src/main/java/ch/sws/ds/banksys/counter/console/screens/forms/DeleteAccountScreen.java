package ch.sws.ds.banksys.counter.console.screens.forms;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.DeleteAccountAction;
import ch.sws.ds.banksys.counter.console.screens.errors.DeleteAccountError;
import ch.sws.ds.banksys.counter.console.screens.messages.DeleteAccountMessage;

/**
 * @author kambl1
 *
 * Eingabemaske um ein Bankkonto zu löschen.
 */
public class DeleteAccountScreen extends FormScreen {
	private static final String SCREEN_NAME = "Delete Account";

	public DeleteAccountScreen(Screen parentMenu) {
		super(SCREEN_NAME);
		addFormStep("Account number", "Enter account number to delete");
		addFormStep("PIN", "Enter the customer PIN of this account");
		
		MessageScreen message = new DeleteAccountMessage(parentMenu, getValueList());
		ErrorScreen error = new DeleteAccountError(this, parentMenu);
		FormAction formAction = new DeleteAccountAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
}
