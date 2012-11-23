package ch.sws.ds.banksys.counter;

import ch.sws.ds.banksys.common.console.Console;
import ch.sws.ds.banksys.counter.console.CounterConsole;

/**
 * @author kambl1
 *
 * Main
 */
public class Counter {		
	public static void main(String[] args) {
		Console console = new CounterConsole();
		console.start();
	}
}
