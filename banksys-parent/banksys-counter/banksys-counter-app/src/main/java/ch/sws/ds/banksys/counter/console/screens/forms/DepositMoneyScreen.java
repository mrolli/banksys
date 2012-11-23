package ch.sws.ds.banksys.counter.console.screens.forms;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.DepositMoneyAction;
import ch.sws.ds.banksys.counter.console.screens.errors.DepositMoneyError;
import ch.sws.ds.banksys.counter.console.screens.messages.DepositMoneyMessage;

/**
 * @author kambl1
 *
 * Eingabemaske um ein Betrag auf das eigene Bankkonto einzuzahlen.
 */
public class DepositMoneyScreen extends FormScreen {
	private static final String SCREEN_NAME = "Deposit Money";

	public DepositMoneyScreen(Screen parentMenu) {
		super(SCREEN_NAME);
		addFormStep("Amount", "Enter the amount to deposit (Integer in CHF)");
		
		MessageScreen message = new DepositMoneyMessage(parentMenu, getValueList());
		ErrorScreen error = new DepositMoneyError(this, parentMenu);
		FormAction formAction = new DepositMoneyAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
}
