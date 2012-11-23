/**
 * 
 */
package ch.sws.ds.banksys.backend.persistence;

import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertTOCustomer;
import static ch.sws.ds.banksys.backend.persistence.MappingUtils.convertToProvCustomer;
import ch.sws.ds.banksys.backend.persistence.provided.CustomerDAO;
import ch.sws.ds.banksys.backend.persistence.provided.DAOFactory;
import ch.sws.ds.banksys.common.Customer;

/**
 * @author feuzl1
 * 
 */
public class CustomerDao {

	private static final String CUSTOMER_SEQ = "CustomerSeq";

	private static CustomerDao instance;

	private CustomerDAO provDAO;

	private CustomerDao() {
		this.provDAO = DAOFactory.getInstance().createCustomerDAO();
	}

	public static synchronized CustomerDao getInstance() {
		if (instance == null) {
			instance = new CustomerDao();
		}
		return instance;
	}

	public Customer getByNr(Integer nr) {
		return convertTOCustomer(provDAO.findCustomer(nr));
	}

	/**
	 * Speichert einen Customer. Anhand der nummer wird entschieden ob neu oder
	 * update. Wenn neu, wird eine neue nummer generiert und dann dem Customer
	 * zugeordnet. Dies geschieht in einem synchronisierten block.
	 * 
	 * @param customer
	 *            Customer
	 * @return persistenter Customer
	 */
	public Customer save(Customer customer) {
		if (customer.getNumber() == null) { // new
			synchronized (this) {
				Integer newNumber = SequenceDao.getInstance().getValue(
						CUSTOMER_SEQ);
				customer.setNumber(newNumber);
				provDAO.insertCustomer(convertToProvCustomer(customer));
				return customer;
			}

		} else { // update (no lock)
			provDAO.updateCustomer(convertToProvCustomer(customer));
			return customer;
		}
	}

}
