/**
 * 
 */
package ch.sws.ds.banksys.backend.service.clearing;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToMoneyTransfer;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.dibas.clearing.Clearing;

import ch.sws.ds.banksys.backend.business.TransactionManager;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;

/**
 * @author feuzl1
 * 
 */
@WebService(endpointInterface = "org.dibas.clearing.Clearing")
public class ClearingImpl implements Clearing {

	private static Logger logger = Logger.getLogger(ClearingImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dibas.clearing.Clearing#process(java.util.List)
	 */
	@Override
	public void process(final List<org.dibas.clearing.MoneyTransfer> transfer) {
		logger.info(String.format(
				"Incoming moneyTransfers '%d'! Execute asynchronous...",
				transfer.size()));
		for (org.dibas.clearing.MoneyTransfer mt : transfer) {
			MoneyTransfer moneyTransfer = convertToMoneyTransfer(mt);
			try {
				TransactionManager.getInstance().simpleClearingTransfer(
						moneyTransfer);
			} catch (AmountNotSufficientException e) {
				logger.warn("Cannot execute moneyTransfer!", e);
			} catch (InvalidAccountException e) {
				ClearingService.getInstance().process(
						new MoneyTransfer(moneyTransfer.getNumber(),
								moneyTransfer.getDebit(), moneyTransfer
										.getCredit(),
								moneyTransfer.getAmount(), moneyTransfer
										.getText(), moneyTransfer.getValuta()));
			}
		}
		logger.info(String.format(
				"Done executing incoming clearing request...", transfer.size()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dibas.clearing.Clearing#ping()
	 */
	@Override
	public void ping() {
		logger.trace("PING");
	}

}
