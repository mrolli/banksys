/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToAddress;
import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToName;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_CASH;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_CASH_INIT;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_TRANSFER;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_ACCOUNTS_TRANSFER_INIT;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMERS_THIS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMER_ADDRESS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.BANKSYS_CUSTOMER_NAME;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.HTTP_BIND_ADDRESS;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.RMI_PORT;
import static ch.sws.ds.banksys.backend.utils.PropertyProvider.getProperty;

import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.atm.interfaces.AtmService;
import ch.sws.ds.banksys.backend.persistence.MappingUtils;
import ch.sws.ds.banksys.backend.persistence.clearing.ClearingService;
import ch.sws.ds.banksys.backend.persistence.clearing.RegistrationService;
import ch.sws.ds.banksys.backend.persistence.provided.Account;
import ch.sws.ds.banksys.backend.persistence.provided.AccountDAO;
import ch.sws.ds.banksys.backend.persistence.provided.Customer;
import ch.sws.ds.banksys.backend.persistence.provided.CustomerDAO;
import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.backend.service.clearing.ClearingImpl;
import ch.sws.ds.banksys.backend.utils.PropertyProvider;
import ch.sws.ds.banksys.common.AccountState;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.CustomerState;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.counter.interfaces.CounterService;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;

/**
 * Mainklasse des Banksys Backends.
 * 
 * @author feuzl1
 */
public class Main {

	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			AlreadyBoundException {
		createTechnicalAccounts();

		EBankingService eBankingService = new EBankingServiceImpl();
		CounterService counterService = new CounterServiceImpl();
		AtmService atmService = new AtmServiceImpl();

		EBankingService eBankingStub = (EBankingService) UnicastRemoteObject
				.exportObject(eBankingService, 0);
		CounterService counterStub = (CounterService) UnicastRemoteObject
				.exportObject(counterService, 0);
		AtmService atmStub = (AtmService) UnicastRemoteObject.exportObject(
				atmService, 0);

		Registry registry = LocateRegistry.createRegistry(Integer
				.parseInt(getProperty(RMI_PORT)));

		registry.bind(EBankingService.EBANKING_NAME, eBankingStub);
		registry.bind(CounterService.COUNTER_NAME, counterStub);
		registry.bind(AtmService.ATM_NAME, atmStub);

		Endpoint.publish(PropertyProvider.getProperty(HTTP_BIND_ADDRESS),
				new ClearingImpl());

		logger.info("Backend up and running!");
		// reset clearing registration
		RegistrationService.getInstance().unregisterBank(
				ClearingService.getInstance().getClearingNbr());
		RegistrationService.getInstance().registerBank();
		logger.info("Clearing number: "
				+ ClearingService.getInstance().getClearingNbr());

	}

	private static void createTechnicalAccounts() {
		Integer bankCustomer = Integer
				.parseInt(getProperty(BANKSYS_CUSTOMERS_THIS));
		Integer transferNbr = Integer
				.parseInt(getProperty(BANKSYS_ACCOUNTS_TRANSFER));
		Integer cashNbr = Integer.parseInt(getProperty(BANKSYS_ACCOUNTS_CASH));
		CustomerDAO customerDAO = DAOFactory.getInstance().createCustomerDAO();
		Customer findCustomer = customerDAO.findCustomer(bankCustomer);
		if (findCustomer == null) {
			customerDAO
					.insertCustomer(MappingUtils
							.convertToProvCustomer(new ch.sws.ds.banksys.common.Customer(
									bankCustomer,
									1234,
									convertToName(getProperty(BANKSYS_CUSTOMER_NAME)),
									convertToAddress(getProperty(BANKSYS_CUSTOMER_ADDRESS)),
									CustomerState.ACTIVE)));
		}

		AccountDAO accountDAO = DAOFactory.getInstance().createAccountDAO();
		Account transfer = accountDAO.findAccount(transferNbr);
		if (transfer == null) {
			accountDAO
					.insertAccount(MappingUtils
							.convertToProvAccount(new ch.sws.ds.banksys.common.Account(
									transferNbr,
									bankCustomer,
									new Money(
											new BigDecimal(
													getProperty(BANKSYS_ACCOUNTS_TRANSFER_INIT)),
											Currency.CHF), "Transfer",
									AccountState.OPEN)));
		}
		Account cash = accountDAO.findAccount(cashNbr);
		if (cash == null) {
			accountDAO.insertAccount(MappingUtils
					.convertToProvAccount(new ch.sws.ds.banksys.common.Account(
							cashNbr, bankCustomer, new Money(new BigDecimal(
									getProperty(BANKSYS_ACCOUNTS_CASH_INIT)),
									Currency.CHF), "Cash", AccountState.OPEN)));
		}

	}
}
