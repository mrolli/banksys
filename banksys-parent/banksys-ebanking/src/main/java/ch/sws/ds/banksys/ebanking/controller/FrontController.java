package ch.sws.ds.banksys.ebanking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.Account;
import ch.sws.ds.banksys.common.Currency;
import ch.sws.ds.banksys.common.Customer;
import ch.sws.ds.banksys.common.IBAN;
import ch.sws.ds.banksys.common.Money;
import ch.sws.ds.banksys.common.MoneyTransfer;
import ch.sws.ds.banksys.common.exceptions.AmountNotSufficientException;
import ch.sws.ds.banksys.common.exceptions.InvalidAccountException;
import ch.sws.ds.banksys.common.exceptions.InvalidCustomerException;
import ch.sws.ds.banksys.common.exceptions.UnknownClearingException;
import ch.sws.ds.banksys.ebanking.exception.BackendCommunicationException;
import ch.sws.ds.banksys.ebanking.exception.EmptyAccountListException;
import ch.sws.ds.banksys.ebanking.exception.IllegalInputException;
import ch.sws.ds.banksys.ebanking.exception.LoginException;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;
import ch.sws.ds.banksys.ebanking.weblogic.AccountListViewBean;
import ch.sws.ds.banksys.ebanking.weblogic.Action;
import ch.sws.ds.banksys.ebanking.weblogic.ErrorListViewBean;
import ch.sws.ds.banksys.ebanking.weblogic.StringUtils;
import ch.sws.ds.banksys.ebanking.weblogic.TransferListViewBean;

/**
 * Servlet implementation class FrontController.
 * 
 * Der FrontController verarbeitet sämtliche dynamischen Requests der EBanking-
 * Applikation. Statische Dateien wie CSS-, JavaScript-Dateien und Bilder werden
 * hingegen vom Default Servlet ausgeliefert.
 */
@WebServlet(urlPatterns = {"/pages/*"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger-Instanz des FrontControllers
	 */
	private Logger logger;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontController() {
        super();
        logger = Logger.getLogger(FrontController.class);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatchRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatchRequest(request, response);
	}

	/**
	 * Request dispatcher Methode.
	 * 
	 * Jeder eintreffende Request wird hier anhand der durch {@link FrontController.getAction}
	 * ermittelten Action auf die passende Actionhandlermethode dispatcht.
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dispatchRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		logger.debug("--- Beginn processing new request ---");

		Action action = getAction(request);
		HttpSession session = request.getSession();
		if (action != Action.LOGIN && 
				(session == null || session.getAttribute("customer") == null)) {
			logger.trace("No valid session available, forward to login");
			action = Action.LOGIN;
		}
		logger.debug("Dispatching action " + action.toString());
		
		try {
			switch (action) {
				case LOGIN:
					handleLogin(request, response);
					break;
				case LOGOUT:
					handleLogout(request, response);
					break;
				case ACCOUNTOVERVIEW:
					handleAccountOverview(request, response);
					break;
				case ACCOUNTSTATEMENT:
					handleAccountStatement(request, response);
					break;
				case PAYMENTTRANSACTION:
					handlePaymentTransaction(request, response);
					break;
				case PAYMENTTRANSFER:
					handlePaymentTransfer(request, response);
					break;
				default:
					logger.fatal("Unable to dispatch " + action.toString());
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				logger.error(e);
				final RequestDispatcher dispatcher = this.getServletContext()
						.getRequestDispatcher("/WEB-INF/jsp/Error.jspx");
				dispatcher.include(request, response);
			} catch (Exception ex) {
				logger.fatal(ex);
			}
		}
	}
	
	/**
	 * Handlermethode für Loginfunktionalität.
	 * 
	 * Die Handlermethode kapselt die Loginlogik und leitet die Anfragen an
	 * das {@link Login.jspx} weiter. Ist das Login erfolgreich, wird an
	 * {@link handleAccountOverview} weitergeleitet. 
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ErrorListViewBean elvb = new ErrorListViewBean();
		request.setAttribute("errorList", elvb);

		if (request.getMethod().equals("POST")) {
			try {
				Integer customerNumber = null;
				try {
					customerNumber = Integer.parseInt(request.getParameter("customerNumber"));
					if (customerNumber == null) {
						throw new Exception("Illegal customer number provided");
					}
				} catch (Exception e) {
					logger.trace(e.getMessage());
					elvb.addError("Sie haben eine ungültige Benutzernummer eingegeben.");
				}
				
				Integer customerPin = null;
				try {
					customerPin = Integer.parseInt(request.getParameter("customerPin"));
					if (customerPin == null) {
						throw new Exception("Illegal pin provided");
					}
				} catch (Exception e) {
					logger.trace(e.getMessage());
					elvb.addError("Sie haben einen ungültigen Pin eingegeben.");
				}
				
				// if input validation errors occured, throw
				if (elvb.getCount() > 0) {
					throw new IllegalInputException("Illegal input detected.");
				}
				
				EBankingService ebs = getEBankingService(request);
				if (ebs.isValidCustomer((int) customerNumber, (int) customerPin)) {
					HttpSession session = request.getSession(true);
					Customer customer = ebs.getCustomer(customerNumber);
					session.setAttribute("customer", customer);

					logger.trace("Login successful; user=" + customer.getNumber());
					handleAccountOverview(request, response);
					return;
				} else {
					logger.trace("Login not successful");
					throw new LoginException("Invalid customer " + customerNumber);
				}
			} catch (LoginException e) {
				elvb.addError("Ihre Zugangsdaten sind ungültig. Die Anmeldung war nicht erfolgreich");
				logger.trace(e);
			} catch (IllegalInputException e) {
				logger.trace(e);
			} catch (InvalidCustomerException e) {
				logger.trace(e);
				elvb.addError("Ihre Zugangsdaten sind ungültig. Die Anmeldung war nicht erfolgreich");
			}
 		}
		
		logger.trace("Show login form");
		final RequestDispatcher dispatcher = this.getServletContext()
					.getRequestDispatcher("/WEB-INF/jsp/Login.jspx");
		dispatcher.include(request, response);
	}
	
	/**
	 * Handlermethode für Logoutfunktionalität.
	 * 
	 * Die Handlermethode löscht den Customer aus der Session und invalidiert letztere.
	 * Im Anschluss wird an die View {@link Logout.jspx} weitergeleitet.
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null) {
			session.removeAttribute("customer");
			session.invalidate();
		}
		final RequestDispatcher dispatcher = this.getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/Logout.jspx");
		dispatcher.include(request, response);
	}
	
	/**
	 * Handlermethode zur Anzeige der Kontenübersicht.
	 * 
	 * Die Konten des angemeldeten Benutzers werden vom Backend abgefragt und via
	 * RequestScope der nachfolgenden View ({@link AccountOverview.jspx}) weitergereicht.
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleAccountOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		int customerNumber = ((Customer) session.getAttribute("customer")).getNumber();
		logger.trace("Getting accounts for customer " + customerNumber);
		List<Account> accounts = getEBankingService(request).getAccounts(customerNumber);
		logger.trace("Found " + accounts.size() + " accounts");
		request.setAttribute("accountList", new AccountListViewBean(accounts));
		request.setAttribute("currentDate", new Date());

		final RequestDispatcher dispatcher = this.getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/AccountOverview.jspx");
		dispatcher.include(request, response);
	}
	
	/**
	 * Handlermethode für den Kontoauszug.
	 * 
	 * Aufgrund des Request Parameters werden alle relevanten Transaktionen
	 * vom Backend abgefragt und via RequestScope der nachfolgenden View
	 * ({@link AccountStatement.jspx}) zur Verfügung gestellt.
	 * 
	 * Wird kein Konto per Request Parameter übergeben, wird der Auszug des ersten
	 * verfügbaren Kontos angezeigt.
	 * 
	 * Wird kein Konto gefunden, wird eine entsprechende Exception geworfen, da diese
	 * Situation bei korrekter Datenlage nicht vorkommen kann.
	 *  
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleAccountStatement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		int customerNumber = ((Customer) session.getAttribute("customer")).getNumber();
		logger.trace("Getting accounts for customer " + customerNumber);
		List<Account> accounts = getEBankingService(request).getAccounts(customerNumber);
		logger.trace("Found " + accounts.size() + " accounts");
		
		// determine account to show up
		Account account = null;
		String accountParam = request.getParameter("account");
		if (accountParam != null && !accountParam.equals("")) {
			int accountNo = Integer.parseInt(accountParam);
			for (Account acc : accounts) {
				if (acc.getNumber() == accountNo) {
					account = acc;
					logger.debug("Chose account " + accountNo + " by request param");
					break;
				}
			}
		}
		if (account == null) {
			if (accounts.size() == 0) {
				logger.debug("Customer " + customerNumber + " has zero accounts.");
				throw new EmptyAccountListException("Customer " + customerNumber + " has no accounts.");
			} else {
				account = accounts.get(0);
				logger.debug("Chose first account " + account.getNumber() + " as request param was missing");
			}
		}
		
		// fill request with attributes
		EBankingService ebs = getEBankingService(request);
		logger.debug("Fetched clearing number: " + ebs.getClearingNbr());
		List<MoneyTransfer> transfers = null;
		try {
			transfers = ebs.getMoneyTransfers(account.getNumber());
		} catch (InvalidAccountException e) {
			logger.error("Invalid account detected", e);
		}		
		logger.debug("Found " + transfers.size() + " transfers for account " + account.getNumber());

		request.setAttribute("clearingNumber", ebs.getClearingNbr());
		request.setAttribute("account", account);
		request.setAttribute("accountList", new AccountListViewBean(accounts));
		request.setAttribute("transferList", new TransferListViewBean(transfers, account, ebs.getClearingNbr()));
		request.setAttribute("currentDate", new Date());

		final RequestDispatcher dispatcher = this.getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/AccountStatement.jspx");
		dispatcher.include(request, response);
	}
	
	/**
	 * Handlermethode zur Verarbeitung von Zahlungen.
	 * 
	 * Mithilfe der View {@link PaymentTransaction.jspx} verarbeitet die Methode
	 * Zahlungen des Kunden an andere Konten. 
	 * 
	 * Server side input validation wird von der Methode übernommen. Validieren 
	 * die eingehenden Daten nicht, wird die View mit Hinweis auf Fehleingaben 
	 * erneut angezeigt.
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handlePaymentTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int customerNumber = ((Customer) session.getAttribute("customer")).getNumber();
		ErrorListViewBean elvb = new ErrorListViewBean();
		request.setAttribute("errorList", elvb);
		request.setAttribute("save_succeeded", 0);

		logger.trace("Getting accounts for customer " + customerNumber);
		List<Account> accounts = getEBankingService(request).getAccounts(customerNumber);
		logger.trace("Found " + accounts.size() + " accounts");
		request.setAttribute("accountList", new AccountListViewBean(accounts));

		if (request.getMethod().equals("POST")) {
			EBankingService ebs = getEBankingService(request);
			String countryCode = ebs.getCountryCode();
			int clearingNumber = ebs.getClearingNbr();
			
			try {
				Integer creditAccountNo;
				IBAN creditAccount = null;
				try {
					creditAccountNo = Integer.parseInt(request.getParameter("creditAccount"));
					boolean found = false;
					for (Account acc : accounts) {
						if ((int) acc.getNumber() == (int) creditAccountNo) {
							found = true;
							break;
						}
					}
					if (!found) {
						logger.trace("Source account was not found in account list.");
						throw new IllegalInputException(null);
					}
					creditAccount = new IBAN(countryCode, clearingNumber, creditAccountNo);
				} catch (Exception e) {
					logger.trace("Source account was not legal input data");
					elvb.addError("Sie haben ein ungültiges Quellkonto ausgewählt");
				}

				Integer debitClearingNo = null;
				try {
					debitClearingNo = Integer.parseInt(request.getParameter("debitBank"));
				} catch (Exception e) {
					logger.trace("Illegal debit bank provided");
					elvb.addError("Sie haben eine ungültige Zielbank eingegeben.");
				}
				
				Integer debitAccountNo = null;
				try {
					debitAccountNo = Integer.parseInt(request.getParameter("debitAccount"));
				} catch (Exception e) {
					logger.trace("Illegal debit account provided");
					elvb.addError("Sie haben ein ungültiges Zielkonto eingegeben.");
				}
				
				Integer integerDigits = null;
				Integer fractionDigits = null;
				try {
					integerDigits = Integer.parseInt(request.getParameter("amountFranken"));
					fractionDigits = Integer.parseInt(request.getParameter("amountRappen"));
				} catch (Exception e) {
					logger.trace("Destination account was not legal input data");
					elvb.addError("Sie haben einen ungültigen Betrag eingegeben.");
				}
				
				// if input validation errors occured, throw
				if (elvb.getCount() > 0) {
					throw new IllegalInputException("Illegal input detected.");
				}

				// no errors so far, process request data
				IBAN debitAccount = new IBAN(countryCode, debitClearingNo, debitAccountNo);
				BigDecimal cents = new BigDecimal(fractionDigits).divide(new BigDecimal(100));
			    BigDecimal amount = new BigDecimal(integerDigits).add(cents);
			    if(amount.doubleValue() <= 0) {
			    	logger.trace("Destination account was not legal input data");
					elvb.addError("Sie haben einen ungültigen Betrag eingegeben.");
					throw new IllegalArgumentException("Illegal input detected.");
			    }
			    Money money = new Money(amount, Currency.CHF);
			    
			    String text = StringUtils.sanitizeInput(request.getParameter("description"));
				
			    MoneyTransfer moneyTransfer = new MoneyTransfer(null, creditAccount, debitAccount, money, text, new Date());
				ebs.executeMoneyTransfer(moneyTransfer);
			    request.setAttribute("save_succeeded", 1);
			} catch (RemoteException e) {
				logger.error(e);
				throw new BackendCommunicationException(e.getMessage());
			} catch (InvalidAccountException e) {
				logger.debug(e);
				elvb.addError("Der Auftrag konnte aufgrund eines ungültigen Kontos nicht verarbeitet werden.");
			} catch (UnknownClearingException e) {
				logger.debug(e);
				elvb.addError("Die angegebene Bank des Zielkontos ist unbekannt.");
			} catch (AmountNotSufficientException e) {
				logger.debug(e);
				elvb.addError("Der Betrag ihres Auftrages überschreitet den Saldo des Belastungskonto.");
			} catch (IllegalInputException e) {
				logger.debug(e);
			}
		}

		final RequestDispatcher dispatcher = this.getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/PaymentTransaction.jspx");
		dispatcher.include(request, response);
	}

	/**
	 * Handlermethode zur Verarbeitung von Kontoüberträgen.
	 * 
	 * Mithilfe der View {@link PaymentTransfer.jspx} verarbeitet die Methode
	 * Kontoüberträge von einen Konto des angemeldeten Benutzers auf ein anderes
	 * seiner Konto.
	 * 
 	 * Server side input validation wird von der Methode übernommen. Validieren 
 	 * die eingehenden Daten nicht, wird die View mit Hinweis auf Fehleingaben 
 	 * erneut angezeigt.
	 * 
	 * @param request	Eintreffender Request
	 * @param response	Auszuliefernde Response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handlePaymentTransfer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int customerNumber = ((Customer) session.getAttribute("customer")).getNumber();
		ErrorListViewBean elvb = new ErrorListViewBean();
		request.setAttribute("errorList", elvb);
		request.setAttribute("save_succeeded", 0);

		logger.trace("Getting accounts for customer " + customerNumber);
		List<Account> accounts = getEBankingService(request).getAccounts(customerNumber);
		logger.trace("Found " + accounts.size() + " accounts");
		request.setAttribute("accountList", new AccountListViewBean(accounts));

		if (request.getMethod().equals("POST")) {
			EBankingService ebs = getEBankingService(request);
			String countryCode = ebs.getCountryCode();
			int clearingNumber = ebs.getClearingNbr();
			
			try {
				Integer creditAccountNo;
				IBAN creditAccount = null;
				try {
					creditAccountNo = Integer.parseInt(request.getParameter("creditAccount"));
					boolean found = false;
					for (Account acc : accounts) {
						if ((int) acc.getNumber() == (int) creditAccountNo) {
							found = true;
							break;
						}
					}
					if (!found) {
						logger.trace("Source account was not found in account list.");
						throw new IllegalInputException(null);
					}
					creditAccount = new IBAN(countryCode, clearingNumber, creditAccountNo);
				} catch (Exception e) {
					logger.trace("Source account was not legal input data");
					elvb.addError("Sie haben ein ungültiges Quellkonto ausgewählt");
				}

				Integer debitAccountNo;
				IBAN debitAccount = null;
				try {
					debitAccountNo = Integer.parseInt(request.getParameter("debitAccount"));
					boolean found = false;
					for (Account acc : accounts) {
						if ((int) acc.getNumber() == (int) debitAccountNo) {
							found = true;
							break;
						}
					}
					if (!found) {
						logger.trace("Destination account was not found in account list.");
						throw new IllegalInputException(null);
					}
					debitAccount = new IBAN(countryCode, clearingNumber, debitAccountNo);
				} catch (Exception e) {
					logger.trace("Destination account was not legal input data");
					elvb.addError("Sie haben ein ungültiges Zielkonto ausgewählt");
				}
				
				if (creditAccount.equals(debitAccount)) {
					logger.trace("Source and destination account are identical.");
					elvb.addError("Quell- und Zielkonto dürfen nicht identisch sein.");
				}
				
				Integer integerDigits = null;
				Integer fractionDigits = null;
				try {
					integerDigits = Integer.parseInt(request.getParameter("amountFranken"));
					fractionDigits = Integer.parseInt(request.getParameter("amountRappen"));
				} catch (Exception e) {
					elvb.addError("Sie haben einen ungültigen Betrag eingegeben.");
				}
				
				// if input validation errors occured, throw
				if (elvb.getCount() > 0) {
					throw new IllegalInputException("Illegal input detected.");
				}
		    		
				BigDecimal cents = new BigDecimal(fractionDigits).divide(new BigDecimal(100));
			    BigDecimal amount = new BigDecimal(integerDigits).add(cents);
			    if(amount.doubleValue() <= 0) {
			    	logger.trace("Destination account was not legal input data");
					elvb.addError("Sie haben einen ungültigen Betrag eingegeben.");
					throw new IllegalArgumentException("Illegal input detected.");
			    }
			    Money money = new Money(amount, Currency.CHF);
			    
			    String text = StringUtils.sanitizeInput(request.getParameter("description"));
				
			    MoneyTransfer moneyTransfer = new MoneyTransfer(null, creditAccount, debitAccount, money, text, new Date());
				ebs.executeMoneyTransfer(moneyTransfer);
			    request.setAttribute("save_succeeded", 1);
			} catch (RemoteException e) {
				logger.error(e);
				throw new BackendCommunicationException(e.getMessage());
			} catch (InvalidAccountException e) {
				logger.debug(e);
				elvb.addError("Der Auftrag konnte aufgrund eines ungültigen Kontos nicht verarbeitet werden.");
			} catch (UnknownClearingException e) {
				// this case should never happen
				logger.error(e);
				elvb.addError("Der Auftrag konnte aufgrund eines ungültigen Kontos nicht verarbeitet werden.");
			} catch (AmountNotSufficientException e) {
				logger.debug(e);
				elvb.addError("Der Betrag ihres Auftrages überschreitet den Saldo des Belastungskonto.");
			} catch (IllegalInputException e) {
				logger.debug(e);
			}
		}
		
		final RequestDispatcher dispatcher = this.getServletContext()
				.getRequestDispatcher("/WEB-INF/jsp/PaymentTransfer.jspx");
		dispatcher.include(request, response);
	}

	/**
	 * Utility-Methode zur Isolierung der angefragten Action.
	 * 
	 * Anhand der PathInfo des Requests ermittelt die Methoden die
	 * Action, die der Request erwartet.
	 * 
	 * Wenn keine oder eine unbekannte Action gefunden wird, wird eine 
	 * Default-Action zurückgegeben.
	 * 
	 * @param request	Eintreffender Request
	 * @return 	Die ermittelte Action
	 */
	private Action getAction(HttpServletRequest request) {		
		String path = request.getPathInfo();
		if (path == null || path.equals("/")) {
			logger.debug("Found empty path; assuming " + Action.ACCOUNTOVERVIEW.name());
			return Action.ACCOUNTOVERVIEW;
		} else {
			Action action;
			String actionString = request.getPathInfo().substring(1).toUpperCase();
			try {
				action = Action.valueOf(actionString);
				logger.debug("Determined known action " + action.name());
			} catch (IllegalArgumentException e) {
				action = Action.LOGIN;
				logger.warn("Encountered unknown action " + actionString + ", action now set to " + action.name());
			}
	
			return action;
		}
	}
	
	/**
	 * Utility-Methode für den Zugriff auf den Backendservice.
	 * 
	 * Die Methode liefert das BackendService-Objekt an den Aufrufer zurück.
	 * 
	 * Das Request Objekt wird nur für den Zugriff auf den ServletContext benötigt.
	 * 
	 * @param request	Eintreffender Request
	 * @return	EBankingService Instanz
	 */
    public static EBankingService getEBankingService(HttpServletRequest request) {
        return (EBankingService) request.getServletContext().getAttribute("EBankingService");
    }
}
