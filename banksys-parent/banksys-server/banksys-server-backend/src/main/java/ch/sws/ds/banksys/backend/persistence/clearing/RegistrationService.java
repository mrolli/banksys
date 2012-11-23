/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence.clearing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.dibas.clearing.BankInfo;
import org.dibas.clearing.GetBankList;
import org.dibas.clearing.GetBankListResponse;
import org.dibas.clearing.RegisterBank;
import org.dibas.clearing.RegisterBankResponse;
import org.dibas.clearing.UnregisterBank;

import ch.sws.ds.banksys.backend.utils.PropertyProvider;

import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_NAME;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_REGISTRATION_URL;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.HTTP_BIND_ADDRESS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;

/**
 * @author feuzl1
 * 
 */
public class RegistrationService {

	private org.dibas.clearing.RegistrationService webservice;

	private static RegistrationService instance;

	private RegistrationService() throws MalformedURLException {
		webservice = new org.dibas.clearing.RegistrationService(new URL(
				PropertyProvider.getProperty(BANKSYS_REGISTRATION_URL)));
	}

	public static synchronized RegistrationService getInstance() {
		if (instance == null) {
			try {
				instance = new RegistrationService();
			} catch (MalformedURLException e) {
				throw new RuntimeException("Error binding RegistrationService",
						e);
			}
		}
		return instance;
	}

	/**
	 * Returniert die Bankliste der clearingstelle.
	 * 
	 * @return bankliste
	 */
	public List<BankInfo> getBankList() {
		GetBankList gbl = new GetBankList();
		GetBankListResponse bankList = webservice.getRegistrationPort()
				.getBankList(gbl);
		return bankList.getBankInfo();
	}

	/**
	 * Registriert die Bank bei der Clearingstelle.
	 * 
	 * @return Clearing nummer
	 */
	public Integer registerBank() {
		RegisterBank rb = new RegisterBank();
		rb.setName(getProperty(BANKSYS_NAME));
		rb.setAddress(getProperty(HTTP_BIND_ADDRESS));
		RegisterBankResponse registerBank = webservice.getRegistrationPort()
				.registerBank(rb);
		return registerBank.getBankNr();
	}

	public void unregisterBank(int bankNumber) {
		UnregisterBank ub = new UnregisterBank();
		ub.setBankNr(bankNumber);
		webservice.getRegistrationPort().unregisterBank(ub);
	}

}
