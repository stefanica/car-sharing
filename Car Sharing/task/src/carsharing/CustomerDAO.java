package carsharing;

import java.util.List;

public interface CustomerDAO {
    void createCustomer(Customer customer);
    Company getCustomerById(int id);
    List<Customer> getAllCustomers();
    void updateCustomer(Customer customer, Car car);
    void deleteCustomer(int id);
}
