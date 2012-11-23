package ch.sws.ds.banksys.counter.console.screens.forms;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.CreateCustomerAction;
import ch.sws.ds.banksys.counter.console.screens.errors.CreateCustomerError;
import ch.sws.ds.banksys.counter.console.screens.messages.CreateCustomerMessage;

/**
 * @author kambl1
 *
 * Eingabemaske um einen neuen Kunden zu Erstellen.
 */
public class CreateCustomerScreen extends FormScreen {
	private static final String SCREEN_NAME = "Create Customer";

	public CreateCustomerScreen(Screen parentMenu) {
		super(SCREEN_NAME);
		addFormStep("Firstname", "Enter the firstname");
		addFormStep("Lastname", "Enter the lastname");
		addFormStep("PIN", "Enter a PIN");
		addFormStep("Streetname", "Enter streetname");
		addFormStep("Housenumber", "Enter housenumber");
		addFormStep("City", "Enter city");
		addFormStep("Postal code", "Enter postal code");
		addFormStep("Country", "Enter country");
		
		MessageScreen message = new CreateCustomerMessage(parentMenu, getValueList());
		ErrorScreen error = new CreateCustomerError(this, parentMenu);
		FormAction formAction = new CreateCustomerAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
}
