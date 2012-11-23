/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence;

import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.backend.persistence.provided.SequenceDAO;

/**
 * @author feuzl1
 * 
 */
public class SequenceDao {

	private static SequenceDao instance;

	private SequenceDAO provDAO;

	private SequenceDao() {
		provDAO = DAOFactory.getInstance().createSequenceDAO();
	}

	public static synchronized SequenceDao getInstance() {
		if (instance == null) {
			instance = new SequenceDao();
		}
		return instance;
	}

	/**
	 * Liefert den nächsten Wert von einer Sequence. Falls diese noch nicht
	 * existiert, wird Sie erstellt.
	 * 
	 * @param sequence
	 *            name der Sequence
	 * @return nächster wert.
	 */
	public synchronized Integer getValue(final String sequence) {
		if (!provDAO.incrementValue(sequence)) {
			// create sequence
			provDAO.createSequence(sequence, 100);
		}
		return provDAO.getValue(sequence);
	}
}
