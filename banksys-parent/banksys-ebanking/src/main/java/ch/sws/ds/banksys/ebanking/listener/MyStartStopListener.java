package ch.sws.ds.banksys.ebanking.listener;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import ch.sws.ds.banksys.ebanking.controller.DummyEBankingService;
import ch.sws.ds.banksys.ebanking.controller.EBankingServiceProvider;
import ch.sws.ds.banksys.ebanking.exception.BackendCommunicationException;
import ch.sws.ds.banksys.ebanking.interfaces.EBankingService;

/**
 * Application Lifecycle Listener
 * 
 * Mit Hilfe des Properties-Files wird der gewünschte BackendService
 * instanziert oder von der Registry geholt und im ServletContext
 * abgelegt.
 *
 */
@WebListener
public class MyStartStopListener implements ServletContextListener {
	private static final String PROPERTIES_FILE = "/webapp.properties";
	private static final String KEY_RMI_HOST = "rmi.host";
	private static final String KEY_RMI_PORT = "rmi.port";

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	System.out.println("EBankingApp is about to start...");
		try {
			Properties properties = new Properties();
		    properties.load(MyStartStopListener.class.getResourceAsStream((PROPERTIES_FILE)));
		    ServletContext sc = event.getServletContext();
		    
		    EBankingServiceProvider ebsp = EBankingServiceProvider.valueOf(
		    		properties.getProperty("ebanking.serviceprovider"));
		    
		    EBankingService ebs = getEBankingService(ebsp, properties);
		    sc.setAttribute("EBankingService", ebs);
		} catch (NullPointerException e) {
			throw new RuntimeException("Properties file not found.");
		} catch (Exception e) {
			throw new RuntimeException("Invalid properties file: " + e.getMessage());
		}
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("EBankingApp is about to stop...");
    }
    
    /**
     * Factory methode zur Erzeugung oder Beschaffung eines EBankingService.
     * 
     * Der betriebsbereite EBankingService wird im ServletContext deponiert.
     */
    public static EBankingService getEBankingService(EBankingServiceProvider type, Properties props) {
        EBankingService ebs;

        switch (type) {
            case TEST:
                ebs = new DummyEBankingService();
                break;
            case PROD:
            	try {
            		String remoteHost = props.getProperty(KEY_RMI_HOST); 
            		int remotePort = Integer.parseInt(props.getProperty(KEY_RMI_PORT));
	                Registry registry = LocateRegistry.getRegistry(remoteHost, remotePort);
	                ebs = (EBankingService) registry.lookup(EBankingService.EBANKING_NAME);
	                break;
            	} catch (Exception e) {
            		throw new BackendCommunicationException("Unable to communicate with an EBankingService");
            	}
            default:
                throw new RuntimeException("Unknown service provider: " + type);
        }

        return ebs;
    }
}
