/**
 * 
 */
package ch.sws.ds.banksys.ebanking;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;

/**
 * @author feuzl1
 * 
 */
@Ignore
public class ServiceTest {

	@Test
	public void serviceTest() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 5495);
		EBankingService service = (EBankingService) registry
				.lookup(EBankingService.EBANKING_NAME);

		List<Account> accounts = service.getAccounts(1);
		Assert.assertNotNull(accounts);
		Assert.assertEquals(2, accounts.size());
	}

}
