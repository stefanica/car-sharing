package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHandler  implements CompanyDAO{

    // JDBC driver name and database URL
    private Connection connection;
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public DBHandler() {

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
    public void createCompany(Company company) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COMPANY (name) VALUES (?)");
            statement.setString(1, company.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Company getCompanyById(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Company newCompany = new Company(resultSet.getString("NAME"));
                newCompany.setId(resultSet.getInt("ID"));
                return newCompany;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COMPANY");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Company newCompany = new Company(resultSet.getString("NAME"));
                newCompany.setId(resultSet.getInt("ID"));
                companies.add(newCompany);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void updateCompany(Company company) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE COMPANY SET NAME = ? WHERE ID = ?");
            statement.setString(1, company.getName());
            statement.setInt(3, company.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM COMPANY WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
