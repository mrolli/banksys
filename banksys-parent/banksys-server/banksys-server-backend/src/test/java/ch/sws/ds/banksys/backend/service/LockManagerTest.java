/**
 * 
 */
package ch.sws.ds.banksys.backend.service;

import junit.framework.Assert;

import org.junit.Test;

import ch.sws.ds.banksys.backend.locks.LockManager;
import ch.sws.ds.banksys.common.IBAN;

/**
 * @author feuzl1
 * 
 */
public class LockManagerTest {

	private LockManager lockmanager = LockManager.getInstance();

	public static boolean lock = false;

	@Test
	public void lockTest() throws InterruptedException {
		final IBAN iban = new IBAN("ch", 123, 1234);

		lockmanager.getLock(iban);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					LockManager.getInstance().getLock(iban);

					LockManagerTest.lock = true;

					LockManager.getInstance().releaseLock(iban);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();

		Thread.sleep(500);
		Assert.assertFalse(lock);
		lockmanager.releaseLock(iban);
		Thread.sleep(100);
		Assert.assertTrue(lock);
	}

}
