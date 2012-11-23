package ch.sws.ds.banksys.atm;

import ch.sws.ds.banksys.atm.console.AtmConsole;
import ch.sws.ds.banksys.common.console.Console;

/**
 * @author kambl1
 *
 * Main
 */
public class Atm {		
	public static void main(String[] args) {
		Console console = new AtmConsole();
		console.start();
	}
}
