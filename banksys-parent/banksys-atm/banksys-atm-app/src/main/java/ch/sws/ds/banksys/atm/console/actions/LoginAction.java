package ch.sws.ds.banksys.atm.console.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.atm.net.AtmServiceProxy;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.Screen;
import ch.sws.ds.banksys.common.console.Session;
import ch.sws.ds.banksys.common.console.exceptions.BankcardException;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.utils.BankcardManager;

/**
 * @author kambl1
 *
 * Action zum Einloggen mit der Bankkarte um dem dazugeh�rigen PIN.
 */
public class LoginAction extends FormAction {
	private static final String ACTION_NAME = "Login Action";
	private static final String COUNTRY_CODE = "CH";
	private static Logger logger = Logger.getLogger(LoginAction.class);

	public LoginAction(FormScreen parentScreen, Screen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
		super(ACTION_NAME, parentScreen, nextScreen, errorScreen, valueList);
	}
	
	/**
	 * Prüft ob der einloggende Benutzer (Bankkonto) zur eigenen Bank gehört.
	 * @return True falls der Kunde zur eigenen Bank gehört, sonst false
	 * @throws CommunicationException
	 * @throws NumberFormatException
	 * @throws BankcardException
	 */
	private static boolean checkIsOwnCustomer() throws CommunicationException, NumberFormatException, BankcardException {
		int clearingNumber = AtmServiceProxy.getInstance().getClearingNbr();
		if (clearingNumber == BankcardManager.getBankNumber()) {
			return true;
		}
		return false;
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
				BankcardManager.loadCard();
				if (BankcardManager.getPin().equals(getValue(0))) {
					AtmServiceProxy service = AtmServiceProxy.getInstance();
					Session session = Session.getInstance();
					if (checkIsOwnCustomer()) {
						IBAN iban = new IBAN(service.getCountryCode(), service.getClearingNbr(), BankcardManager.getAccountNumber());
						if (service.isValidAccount(iban)) {
							session.setIBAN(iban);
							session.setIsOwnCustomer(true);
							return true;
						}
						setErrorMessage("Account is not valid!");
					} else {
						IBAN iban = new IBAN(COUNTRY_CODE, BankcardManager.getBankNumber(), BankcardManager.getAccountNumber());
						service.isValidClearingNumber(Integer.valueOf(BankcardManager.getBankNumber()));
						session.setIBAN(iban);
						session.setIsOwnCustomer(false);
						return true;
					}
				} else {
					setErrorMessage("The entered PIN is invalid!");
				}
			} catch (BankcardException | CommunicationException e) {
				setErrorMessage(e.getMessage());
			}
		} else {
			setErrorMessage("Not all data was entered correctly!");
		}
		return false;
	}
}
