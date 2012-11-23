package ch.sws.ds.banksys.counter.console.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.Address;
import ch.sws.ds.banksys.common.Name;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.Formater;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.exceptions.ConversionException;
import ch.sws.ds.banksys.counter.net.CounterServiceProxy;

/**
 * @author kambl1
 *
 * Action zum erstellen eines neuen Kunden.
 */
public class CreateCustomerAction extends FormAction{
	private static final String ACTION_NAME = "Create Customer";
	private static Logger logger = Logger.getLogger(CreateCustomerAction.class);
	
	public CreateCustomerAction(FormScreen parentScreen, MessageScreen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
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
				Name name = new Name(getValue(0), getValue(1));
				int pin = getValueAsInt(2);
				Address address = new Address(getValue(3), getValueAsInt(4), getValue(5), getValueAsInt(6), getValue(7));
				Integer customerNumber = CounterServiceProxy.getInstance().createCustomer(name, address, pin);
				addResult(ResultKeys.CUSTOMER_NUMBER, customerNumber);
				addResult(ResultKeys.ADDRESS_LINE_1, Formater.formatAddressLine1(address));
				addResult(ResultKeys.ADDRESS_LINE_2, Formater.formatAddressLine2(address));
				addResult(ResultKeys.ADDRESS_LINE_3, Formater.formatAddressLine3(address));
				return true;
			} catch (ConversionException | CommunicationException e) {
				setErrorMessage(e.getMessage());
			}
		} else {
			setErrorMessage("Not all data was entered correctly!");
		}
		return false;
	}
}
