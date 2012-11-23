package ch.sws.ds.banksys.counter.console.screens.forms;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.counter.console.actions.WithdrawMoneyAction;
import ch.sws.ds.banksys.counter.console.screens.errors.WithdrawMoneyError;
import ch.sws.ds.banksys.counter.console.screens.messages.WithdrawMoneyMessage;

/**
 * @author kambl1
 *
 * Eingabemaske um ein Betrag vom eigenen Bankkonto abzuheben.
 */
public class WithdrawMoneyScreen extends FormScreen {
	private static final String SCREEN_NAME = "Withdraw Money";

	public WithdrawMoneyScreen(Screen parentMenu) {
		super(SCREEN_NAME);
		addFormStep("Amount", "Enter the amount to withdraw (Integer in CHF)");
		
		MessageScreen message = new WithdrawMoneyMessage(parentMenu, getValueList());
		ErrorScreen error = new WithdrawMoneyError(this, parentMenu);
		FormAction formAction = new WithdrawMoneyAction(this, message, error, getValueList());
		setFormAction(formAction);
	}
}
