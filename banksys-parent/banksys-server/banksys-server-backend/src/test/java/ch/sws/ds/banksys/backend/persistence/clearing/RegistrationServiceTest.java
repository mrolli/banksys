/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence.clearing;

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.dibas.clearing.BankInfo;
import org.junit.Ignore;
import org.junit.Test;

import ch.sws.ds.banksys.backend.service.clearing.ClearingImpl;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;

/**
 * @author feuzl1
 * 
 */
@Ignore(value = "Computer muss im BHF Netz (VPN) sein und die IP in den banksys.properties eingestellt.")
public class RegistrationServiceTest {

	private static Logger logger = Logger
			.getLogger(RegistrationServiceTest.class);

	@Test
	public void getBankListTest() throws MalformedURLException {
		getBankList();
	}

	private void getBankList() {
		List<BankInfo> bankList = RegistrationService.getInstance()
				.getBankList();
		Assert.assertNotNull(bankList);
		for (BankInfo bi : bankList) {
			logger.info(bi.getNr() + " " + bi.getName() + " " + bi.getAddress());

		}
	}

	@Test
	public void registrationTest() {
		Endpoint.publish(PropertyProvider.getProperty("banksys.bind.address"),
				new ClearingImpl());

		Integer clearing = RegistrationService.getInstance().registerBank();

		Assert.assertNotNull(clearing);
		logger.info(clearing);

		getBankList();

		RegistrationService.getInstance().unregisterBank(clearing);

	}

	@Test
	public void testUnregister() {
		RegistrationService.getInstance().unregisterBank(16);
	}

}
