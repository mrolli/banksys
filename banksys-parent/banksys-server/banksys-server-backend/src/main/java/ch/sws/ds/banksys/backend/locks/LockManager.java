/**
 * 
 */
package ch.sws.ds.banksys.backend.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import ch.sws.ds.banksys.common.IBAN;

/**
 * Biedet eine Möglichkeit um Konti anhand ihrer Iban zu locken.
 * 
 * @author feuzl1
 * 
 */
public class LockManager {

	private static LockManager instance;

	private ReentrantLock lock = new ReentrantLock();

	private Map<IBAN, Condition> locks = new HashMap<>();

	private static Logger logger = Logger.getLogger(LockManager.class);

	private LockManager() {
	}

	public static synchronized LockManager getInstance() {
		if (instance == null) {
			instance = new LockManager();
		}
		return instance;
	}

	/**
	 * Vordert das lock auf dem gegebenen Konto an (blockierend). Wenn der lock
	 * erteilt wurde, returniert die methode.
	 * 
	 * @param iban
	 *            iban
	 * @throws InterruptedException
	 */
	public void getLock(IBAN iban) throws InterruptedException {
		logger.trace(String.format("Aquire lock for '%s'...", iban.toString()));
		try {
			lock.lock();
			while (locks.containsKey(iban)) {
				locks.get(iban).await();
			}
			locks.put(iban, lock.newCondition());
			logger.trace(String.format("Lock on '%s'!", iban.toString()));
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gibt ein Lock auf einer IBAN wieder frei.
	 * 
	 * @param iban
	 *            iban
	 */
	public void releaseLock(IBAN iban) {
		logger.trace(String.format("Release lock for '%s'...", iban.toString()));
		try {
			lock.lock();
			while (locks.containsKey(iban)) {
				Condition remove = locks.remove(iban);
				remove.signalAll();
				logger.trace(String.format("Released lock on '%s'!",
						iban.toString()));
			}
		} finally {
			lock.unlock();
		}
	}

}
