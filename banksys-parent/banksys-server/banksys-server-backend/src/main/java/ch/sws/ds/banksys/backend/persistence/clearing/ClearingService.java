/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence.clearing;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToProvMoneyTransfer;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_NAME;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.CLEARING_URL;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dibas.clearing.BankInfo;

import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.MoneyTransfer;

/**
 * Abstrahierter Clearing-Webservice.
 * 
 * @author feuzl1
 */
public class ClearingService {

	private org.dibas.clearing.ClearingService webservice;

	private static ClearingService instance;

	private static List<BankInfo> bankList;

	private static Logger logger = Logger.getLogger(ClearingService.class);

	private ClearingService() throws MalformedURLException {
		webservice = new org.dibas.clearing.ClearingService(new URL(
				PropertyProvider.getProperty(CLEARING_URL)));

	}

	public static synchronized ClearingService getInstance() {
		if (instance == null) {
			try {
				instance = new ClearingService();
			} catch (MalformedURLException e) {
				throw new RuntimeException("Error binding Clearing service!", e);
			}
		}
		return instance;
	}

	/**
	 * Ping methode
	 * 
	 * @return vergangene zeit in milis.
	 */
	public long ping() {
		long start = System.currentTimeMillis();
		webservice.getClearingPort().ping();
		return System.currentTimeMillis() - start;
	}

	/**
	 * Übermittelt die MoneyTransfers
	 * 
	 * @param moneyTransfers
	 */
	public void process(MoneyTransfer moneyTransfers) {
		List<org.dibas.clearing.MoneyTransfer> mts = new ArrayList<>();
		mts.add(convertToProvMoneyTransfer(moneyTransfers));
		webservice.getClearingPort().process(mts);
		logger.debug("Clearing moneyTransfer sent. " + moneyTransfers);
	}

	/**
	 * Liest über die Webservice-methode getBankList die registrierten Banken
	 * aus. Ist diese Bank bereits mit Endpointadresse registriert, wird die
	 * clearing nr zurückgegeben. Andernfalls wird die Bank neu registriert (und
	 * die Clearing nummer zurückgegeben).
	 * 
	 * @return Clearing nummer
	 */
	public Integer getClearingNbr() {
		String bankname = getProperty(BANKSYS_NAME);
		Integer clearingNbr = lookup(bankname);
		if (clearingNbr != null)
			return clearingNbr;
		else {
			Integer clearing = RegistrationService.getInstance().registerBank();
			return clearing;
		}
	}

	/**
	 * Validiert eine clearing nummer gegenüber der banklist.
	 * 
	 * @param clearing
	 * @return
	 */
	public boolean isValidClearingNbr(Integer clearing) {
		if (bankList != null && contains(clearing)) {
			return true;
		} else {
			bankList = RegistrationService.getInstance().getBankList();
			return contains(clearing);
		}
	}

	private boolean contains(Integer clearing) {
		for (BankInfo bi : bankList) {
			if (bi.getNr() == clearing.intValue())
				return true;
		}
		return false;
	}

	private Integer lookup(String name) {
		if (bankList == null)
			bankList = RegistrationService.getInstance().getBankList();
		for (BankInfo bi : bankList) {
			if (bi.getName().equals(name) && bi.getAddress() != null) {
				return bi.getNr();
			}
		}
		return null;
	}
}
