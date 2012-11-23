package ch.sws.ds.banksys.atm.console.actions;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.atm.net.AtmServiceProxy;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.console.ErrorScreen;
import ch.sws.ds.banksys.common.console.FormAction;
import ch.sws.ds.banksys.common.console.FormScreen;
import ch.sws.ds.banksys.common.console.Formater;
import ch.sws.ds.banksys.common.console.MessageScreen;
import ch.sws.ds.banksys.common.console.ResultKeys;
import ch.sws.ds.banksys.common.console.Session;
import ch.sws.ds.banksys.common.console.exceptions.BankcardException;
import ch.sws.ds.banksys.common.console.exceptions.CommunicationException;
import ch.sws.ds.banksys.common.console.exceptions.ConversionException;
import ch.sws.ds.banksys.common.console.utils.BankcardManager;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;

/**
 * @author kambl1
 *
 * Action um einen bestimmten Betrag von eigenen Konto abzuheben.
 */
public class WithdrawMoneyAction extends FormAction {
	private static final String ACTION_NAME = "Withdraw Money";
	private static final String WITHDRAW_MESSAGE ="Bankomatbezug";
	private static Logger logger = Logger.getLogger(WithdrawMoneyAction.class);
	
	public WithdrawMoneyAction(FormScreen parentScreen, MessageScreen nextScreen, ErrorScreen errorScreen, ArrayList<String> valueList) {
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
				int moneyValue = getValueAsInt(0);
				if (moneyValue > 0) {
					Money amount = new Money(new BigDecimal(moneyValue), Currency.CHF);
					IBAN iban = Session.getInstance().getIBAN();
					Integer transactionNumber = Integer.valueOf(0);
					if (Session.getInstance().isOwnCustomer()) {
						transactionNumber = AtmServiceProxy.getInstance().withdrawMoney(iban, amount, WITHDRAW_MESSAGE);
						Money balance = AtmServiceProxy.getInstance().getBalance(iban);
						addResult(ResultKeys.BALANCE, Formater.formatMoney(balance));
					} else {
						if (BankcardManager.checkWithdrawLimit(amount)) {
							transactionNumber = AtmServiceProxy.getInstance().withdrawMoney(iban, amount, WITHDRAW_MESSAGE);
							BankcardManager.increaseWithdrawAmount(amount);
						} else {
							setErrorMessage("The card limit is too low for this transaction!");
							return false;
						}
					}
					addResult(ResultKeys.TRANSACTION_NUMBER, transactionNumber);
					addResult(ResultKeys.IBAN, Formater.formatIBAN(iban));
					addResult(ResultKeys.AMOUNT, Formater.formatMoney(amount));
					return true;
				}
				setErrorMessage("Invalid amount entered, must be greater than zero.");
			} catch (ConversionException | BankcardException | InvalidAccountException | AmountNotSufficientException | UnknownClearingException | CommunicationException e) {
				setErrorMessage(e.getMessage());
			}
		} else {
			setErrorMessage("Not all data was entered correctly!");
		}
		return false;
	}
}
