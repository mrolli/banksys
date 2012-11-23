package ch.sws.ds.banksys.counter.console.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.exceptions.BankcardException;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.exceptions.ConversionException;
import ch.sws.ds.banksys.common.console.utils.BankcardManager;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.counter.net.CounterServiceProxy;

/**
 * @author kambl1
 *
 * Action zum erstellen eines neuen Bankkontos.
 */
public class CreateAccountAction extends FormAction {
	private static final String ACTION_NAME = "Create Acount";
	private static Logger logger = Logger.getLogger(CreateAccountAction.class);
	
	public CreateAccountAction(FormScreen parentScreen, MessageScreen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
		super(ACTION_NAME, parentScreen, nextScreen, errorScreen, valueList);
	}
	
	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.Action#setErrorMessage(java.lang.String)
	 */
	@Override
	protected void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
		logger.trace(errorMessage);
	}

	/* (non-Javadoc)
	 * @see ch.sws.ds.banksys.common.console.FormAction#doForm(java.lang.String)
	 */
	@Override
	protected boolean doForm(String value) {
		if (dataIsComplete()) {
			try {
				Integer customerNumber = getValueAsInteger(0);
				int customerPin = getValueAsInt(1);
				int accountPin = getValueAsInt(2);
				String description = getValue(3);
				Integer accountNumber = CounterServiceProxy.getInstance().createAccount(customerNumber, customerPin, description);
				BankcardManager.createCard( CounterServiceProxy.getInstance().getClearingNbr(), accountNumber, accountPin);
				addResult(ResultKeys.ACCOUNT_NUMBER, accountNumber);
				return true;
			} catch (ConversionException | BankcardException | InvalidCustomerException | CommunicationException e) {
				setErrorMessage(e.getMessage());
			}
		} else {
			setErrorMessage("Not all data was entered correctly!");
		}
		return false;
	}
}
