package ch.sws.ds.banksys.counter.console.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.Formater;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.exceptions.ConversionException;
import ch.sws.ds.banksys.common.exceptions.AccountNotEmptyException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.counter.net.CounterServiceProxy;

/**
 * @author kambl1
 *
 * Action zum löschen eines bestehenden Bankkontos.
 */
public class DeleteAccountAction extends FormAction {
	private static final String ACTION_NAME = "Delete Acount";
	private static Logger logger = Logger.getLogger(DeleteAccountAction.class);
	
	public DeleteAccountAction(FormScreen parentScreen, MessageScreen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
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
				int accountNumber = getValueAsInt(0);
				int pin = getValueAsInt(1);
				CounterServiceProxy service = CounterServiceProxy.getInstance();
				IBAN iban = new IBAN(service.getCountryCode(), service.getClearingNbr(), accountNumber);
				if (service.isValidAccount(iban) && service.isOwnCustomer(iban)) {
					Customer customer = service.getCustomer(iban);
					customer = service.getCustomer(iban);
					if (customer.getPin() == pin) {
						CounterServiceProxy.getInstance().deleteAccount(iban.getAccountNumber());
						addResult(ResultKeys.IBAN, Formater.formatIBAN(iban));
						return true;
					}
					setErrorMessage("The entered PIN is incorrect!");
				} else {
					setErrorMessage("The entered account number is invalid!");
				}
			} catch (ConversionException | AccountNotEmptyException | InvalidCustomerException | InvalidAccountException | CommunicationException e) {
				setErrorMessage(e.getMessage());
			}
		} else {
			setErrorMessage("Not all data was entered correctly!");
		}
		return false;
	}
}
