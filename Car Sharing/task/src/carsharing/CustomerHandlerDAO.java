package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerHandlerDAO implements CustomerDAO {

    // JDBC driver name and database URL
    private Connection connection;
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public CustomerHandlerDAO() {

    }

    public void setConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            // Enable auto-commit mode
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println("Error connecting to database " + e.getMessage());
        }
    }


    @Override
    public void createCustomer(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CUSTOMER (NAME) VALUES (?)");
            statement.setString(1, customer.getName());
            //statement.setNull(2, Types.INTEGER); // Set RENTED_CAR_ID to NULL
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Company getCustomerById(int id) {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer newCustomer = new Customer(resultSet.getString("NAME"));
                newCustomer.setId(resultSet.getInt("ID"));
                //newCustomer.setRentedCarID(resultSet.getInt("RENTED_CAR_ID"));
                //The line from above was replaced with:
                // Check if RENTED_CAR_ID is NULL
                Integer rentedCarID = (Integer) resultSet.getObject("RENTED_CAR_ID");
                if (resultSet.wasNull()) {
                    // Handle NULL value
                    newCustomer.setRentedCarID(null); // or set it to whatever value you prefer
                } else {
                    // Handle non-NULL value
                    newCustomer.setRentedCarID(rentedCarID);
                }

                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void updateCustomer(Customer customer, Car car) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?");
            if (car != null) {
                statement.setInt(1, car.getId());
            } else {
                statement.setNull(1, Types.INTEGER); // Set RENTED_CAR_ID to NULL
            }
            statement.setInt(2, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCar(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?");
            statement.setInt(1, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int id) {

    }
}
