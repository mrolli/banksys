/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence.clearing;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.ws.Endpoint;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.sws.ds.banksys.backend.business.AccountManager;
import ch.sws.ds.banksys.backend.service.clearing.ClearingImpl;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.backend.utils.TestUtils;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;

import static ch.sws.ds.banksys.backend.utils.TestUtils.INITIAL_MONEY;

/**
 * @author feuzl1
 * 
 */
@Ignore(value = "Computer muss im BHF Netz (VPN) sein und die IP in den banksys.properties eingestellt.")
public class ClearingServiceTest {

	private ClearingService cs = ClearingService.getInstance();

	private AccountManager am = AccountManager.getInstance();

	@Before
	public void createAccountTest() throws RemoteException {
		TestUtils.createTechnicalAccounts();
	}

	@Test
	public void pingTest() throws MalformedURLException {
		long ping = ClearingService.getInstance().ping();
		Assert.assertTrue(ping > 0);
	}

	@Test
	@Ignore
	public void sendMoneyTransferTest() throws InterruptedException,
			InvalidAccountException {
		Endpoint.publish(PropertyProvider.getProperty("banksys.bind.address"),
				new ClearingImpl());

		Integer clearing = RegistrationService.getInstance().registerBank();

		Assert.assertNotNull(clearing);

		IBAN credit = new IBAN("CH", 26, 1);
		IBAN debit = new IBAN("CH", cs.getClearingNbr(), 2);
		Money money = new Money(new BigDecimal(100), Currency.CHF);
		MoneyTransfer mt = new MoneyTransfer(null, credit, debit, money,
				"Test", new Date());
		cs.process(mt);

		Thread.sleep(1000 * 60 * 15);

		RegistrationService.getInstance().unregisterBank(clearing);

		Assert.assertEquals(INITIAL_MONEY + 100, am.getBalance(debit)
				.getMoney().intValue());
	}

	@Test
	public void isValidClearing() {
		Assert.assertTrue(cs.isValidClearingNbr(cs.getClearingNbr()));
		Assert.assertFalse(cs.isValidClearingNbr(123456));
	}
}
