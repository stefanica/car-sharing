package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarHandlerDAO implements CarDAO{

    // JDBC driver name and database URL
    private Connection connection;
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public CarHandlerDAO() {

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
    public void createCar(Car car, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)");
            statement.setString(1, car.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Car getCarById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Car newCar = new Car(resultSet.getString("NAME"));
                newCar.setId(resultSet.getInt("ID"));
                newCar.setCompanyID(resultSet.getInt("COMPANY_ID"));
                return newCar;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> getAllCars(int companyID) {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Car newCar = new Car(resultSet.getString("NAME"));
                newCar.setId(resultSet.getInt("ID"));
                cars.add(newCar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> getAllCarsForCustomer(int companyID) {
        CustomerHandlerDAO customerHandlerDAO = new CustomerHandlerDAO();
        customerHandlerDAO.setConnection();

        List<Customer> customers = customerHandlerDAO.getAllCustomers();
        List<Integer> rentedCarsIDS = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            rentedCarsIDS.add(customers.get(i).getRentedCarID());
        }

        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (!rentedCarsIDS.contains(resultSet.getInt("ID"))) {
                    Car newCar = new Car(resultSet.getString("NAME"));
                    newCar.setId(resultSet.getInt("ID"));
                    cars.add(newCar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public void updateCar(Car car) {

    }

    @Override
    public void deleteCar(int id) {

    }
}
