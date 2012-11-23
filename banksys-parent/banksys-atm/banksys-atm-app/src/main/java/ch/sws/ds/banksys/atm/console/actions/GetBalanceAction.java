package ch.sws.ds.banksys.atm.console.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.atm.net.AtmServiceProxy;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.Formater;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Session;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;

/**
 * @author kambl1
 *
 * Action zum Überprüfen des Saldos des eigenen Kontos.
 */
public class GetBalanceAction extends FormAction {
	private static final String ACTION_NAME = "Get Balance";
	private static Logger logger = Logger.getLogger(GetBalanceAction.class);
	
	public GetBalanceAction(MessageScreen nextScreen, ErrorScreen errorScreen) {
		super(ACTION_NAME, null, nextScreen, errorScreen, new ArrayList<String>());
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
		try {
			IBAN iban = Session.getInstance().getIBAN();
			Money balance = AtmServiceProxy.getInstance().getBalance(iban);
			addResult(ResultKeys.IBAN, Formater.formatIBAN(iban));
			addResult(ResultKeys.BALANCE, Formater.formatMoney(balance));
			return true;
		} catch (InvalidAccountException | CommunicationException e) {
			setErrorMessage(e.getMessage());
		}
		return false;
	}
}
