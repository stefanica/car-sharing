package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        createCompanyTable();
        createCarTable();
        createCustomerTable();

        Scanner scanner = new Scanner(System.in);
        DBHandler companyDAO = new DBHandler();
        companyDAO.setConnection();

        CustomerHandlerDAO customerHandlerDAO = new CustomerHandlerDAO();
        customerHandlerDAO.setConnection();

        while (true) {
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 0) {
                break;
            }

            if (option == 2) {
                if (customerHandlerDAO.getAllCustomers().isEmpty()) {
                    System.out.println("The customer list is empty!");
                } else {
                    customerList(customerHandlerDAO);
                }

            }

            if (option == 3) {
                System.out.println("Enter the customer name:");
                String name = scanner.nextLine();
                customerHandlerDAO.createCustomer(new Customer(name));
                System.out.println("The customer was added!");
            }

            if (option == 1) {
                while (true) {
                    System.out.println("1. Company list\n" +
                            "2. Create a company\n" +
                            "0. Back");

                    int secondOption = Integer.parseInt(scanner.nextLine());

                    if (secondOption == 0) {
                        break;
                    }

                    //Listing companies from table
                    if (secondOption == 1) {
                        listCompanies(companyDAO);
                    }

                    //Create and add a new company
                    if (secondOption == 2) {
                        System.out.println("Enter the company name:");
                        String name = scanner.nextLine();
                        companyDAO.createCompany(new Company(name));
                    }

                }
            }

        }

    }

    public static void newCompany(Company companyDAO) {
    }

    public static void customerList(CustomerHandlerDAO customerHandlerDAO) {
        List<Customer> customerList = customerHandlerDAO.getAllCustomers();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Customer list:");
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println((i + 1) + ". " + customerList.get(i).getName());
            }
            System.out.println("0. Back");
            /*
            customerList.forEach( x -> {
                System.out.println(x.getId() + ". " + x.getName());
            });*/

            int option = Integer.parseInt(scanner.nextLine());

            if (option == 0) {
                break;
            }

            if (option > 0) {
                Customer currentCustomer = customerList.get(option - 1);
                manageRentedCars(currentCustomer, customerHandlerDAO);
                break;
            }

        }

    }

    public static void manageRentedCars(Customer customer, CustomerHandlerDAO customerHandlerDAO) {
        Scanner scanner = new Scanner(System.in);
        DBHandler companyDAO = new DBHandler();
        companyDAO.setConnection();

        CarHandlerDAO carHandlerDAO = new CarHandlerDAO();
        carHandlerDAO.setConnection();

        while (true) {
            System.out.println("1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 0) {
                break;
            }

            if (option == 2 || option == 3) {
                if (customer.getRentedCarID() == null) {
                    System.out.println("You didn't rent a car!");
                } else {
                    if (option == 2) {
                        customerHandlerDAO.removeCar(customer);
                        customer.setRentedCarID(null);
                        System.out.println("You've returned a rented car!");
                    }
                    if (option == 3) {
                        Car car = carHandlerDAO.getCarById(customer.getRentedCarID());
                        if (car == null) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            System.out.println("Your rented car:");
                            System.out.println(car.getName());
                            System.out.println("Company:");
                            System.out.println(companyDAO.getCompanyById(car.getCompanyID()).getName());
                        }
                    }
                }
            }

            if (option == 1) {
                //System.out.println("Customer rent car ID= " + customer.getRentedCarID());
                if (customer.getRentedCarID() != null) {
                    System.out.println("You've already rented a car!");
                } else {
                    //Trying to rent a car
                    chooseRentCompany(companyDAO, carHandlerDAO, customer);

                    /*while (true) {
                        //System.out.println("Choose a company:");
                        int companyOption = Integer.parseInt(scanner.nextLine());

                        if (companyOption == 0) {
                            break;
                        } else {
                            System.out.println("test 1");

                        }

                    } */
                }
            }


        }


    }

    public static void chooseRentCompany(DBHandler companyDAO, CarHandlerDAO carHandlerDAO, Customer customer) {
        Scanner scanner = new Scanner(System.in);
        List<Company> companiesList = companyDAO.getAllCompanies();
        if (companiesList.isEmpty()) {
            System.out.println("The company list is empty");
        } else {
            while (true) {
                System.out.println("Choose a company:");
                companiesList.forEach(x -> {
                    System.out.println(x.getId() + ". " + x.getName());
                });
                System.out.println("0. Back");

                int option = Integer.parseInt(scanner.nextLine());

                if (option == 0) {
                    break;
                }
                if (option > 0) {
                    chooseRentCar(carHandlerDAO, companiesList.get(option - 1), customer);
                    break;
                }
            }
        }
    }

    public static void chooseRentCar(CarHandlerDAO carHandlerDAO, Company company, Customer customer) {
        Scanner scanner = new Scanner(System.in);
        CustomerHandlerDAO customerHandlerDAO = new CustomerHandlerDAO();
        customerHandlerDAO.setConnection();

        List<Car> carList = carHandlerDAO.getAllCarsForCustomer(company.getId());
        if (carList.isEmpty()) {
            System.out.println("No available cars in the '" + company.getName() + "' company");
        } else {
            while (true) {
                System.out.println("Choose a car:");
                for (int i = 0; i < carList.size(); i++) {
                    System.out.println((i + 1) + ". " + carList.get(i).getName());
                }
                int option = Integer.parseInt(scanner.nextLine());

                if (option == 0) {
                    break;
                }

                if (option > 0) {
                    customerHandlerDAO.updateCustomer(customer, carList.get(option - 1));
                    System.out.println("You rented '" + carList.get(option - 1).getName() + "'");
                    customer.setRentedCarID(carList.get(option - 1).getId());
                    break;
                }

            }
        }
        System.out.println("");
    }

    public static void listCompanies(CompanyDAO companyDAO) {
        Scanner scanner = new Scanner(System.in);
        List<Company> companiesList = companyDAO.getAllCompanies();
        if (companiesList.isEmpty()) {
            System.out.println("The company list is empty");
        } else {
            while (true) {
                System.out.println("Choose a company:");
                companiesList.forEach(x -> {
                    System.out.println(x.getId() + ". " + x.getName());
                });
                System.out.println("0. Back");

                int option = Integer.parseInt(scanner.nextLine());

                if (option == 0) {
                    break;
                }
                if (option > 0) {
                    manageCars(companiesList.get(option - 1));
                    break;
                }

            }
        }

    }

    public static void manageCars(Company company) {
        Scanner scanner = new Scanner(System.in);
        CarHandlerDAO carHandlerDAO = new CarHandlerDAO();
        carHandlerDAO.setConnection();
        System.out.println("");
        System.out.println("'" + company.getName() + "'" + " company");
        while (true) {
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 0) {
                break;
            }

            if (option == 1) {
                /*
                List<Car> carList = carHandlerDAO.getAllCars(company.getId());
                if (carList.isEmpty()) {
                    System.out.println("The car list is empty!");
                    break;
                } else {
                    listCars(carList, company);
                } */

                listCars(carHandlerDAO, company);
            }

            if (option == 2) {
                System.out.println("");
                System.out.println("Enter the car name:");
                String name = scanner.nextLine();
                carHandlerDAO.createCar(new Car(name), company.getId());
                System.out.println("The car was added!");
                System.out.println("");
            }

        }
    }

    public static void listCars(CarHandlerDAO carHandlerDAO, Company company) {
        List<Car> carList = carHandlerDAO.getAllCars(company.getId());
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < carList.size(); i++) {
                System.out.println((i + 1) + ". " + carList.get(i).getName());
            }
            /*
            carList.forEach(x -> {
                System.out.println(x.getId() + ". " + x.getName());
            }); */
        }
        System.out.println("");
    }

    public static void createCustomerTable() {
        // Parse command-line arguments to get the database file name
        String databaseFileName = "carsharing";

        // Construct the JDBC URL
        String jdbcUrl = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            // Enable auto-commit mode
            connection.setAutoCommit(true);

            // Create COMPANY table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT DEFAULT NULL," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                    ")";
            statement.executeUpdate(createTableSQL);

            System.out.println("CUSTOMER table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating COMPANY table: " + e.getMessage());
        }
    }

    public static void createCarTable() {
        // Parse command-line arguments to get the database file name
        String databaseFileName = "carsharing";

        // Construct the JDBC URL
        String jdbcUrl = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            // Enable auto-commit mode
            connection.setAutoCommit(true);

            // Create COMPANY table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                    ")";
            statement.executeUpdate(createTableSQL);

            System.out.println("COMPANY table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating COMPANY table: " + e.getMessage());
        }
    }

    public static void createCompanyTable() {
        // Parse command-line arguments to get the database file name
        String databaseFileName = "carsharing";

        // Construct the JDBC URL
        String jdbcUrl = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            // Enable auto-commit mode
            connection.setAutoCommit(true);

            // Create COMPANY table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(255) UNIQUE NOT NULL" +
                    ")";
            statement.executeUpdate(createTableSQL);

            System.out.println("COMPANY table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating COMPANY table: " + e.getMessage());
        }
    }

    public static void modifyTable() {
        String jdbcUrl = "jdbc:h2:./src/carsharing/db/carsharing";
        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            // Enable auto-commit mode
            connection.setAutoCommit(true);

            // Modify ID column to auto-increment
            statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN ID INT AUTO_INCREMENT");

            // Add unique constraint to NAME column
            statement.executeUpdate("ALTER TABLE COMPANY ADD CONSTRAINT UK_NAME UNIQUE (NAME)");

            // Modify NAME column to not null
            statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN NAME VARCHAR(255) NOT NULL");

            /*
            // Add constraints to COMPANY table
            String alterTableSQL = "ALTER TABLE COMPANY " +
                    "ADD PRIMARY KEY (ID), " +
                    "MODIFY COLUMN ID INT AUTO_INCREMENT, " +
                    "ADD CONSTRAINT UK_NAME UNIQUE (NAME), " +
                    "MODIFY COLUMN NAME VARCHAR(255) NOT NULL";
            statement.executeUpdate(alterTableSQL);*/

            System.out.println("Constraints added to COMPANY table successfully.");

        } catch (SQLException e) {
            System.err.println("Error adding constraints to COMPANY table: " + e.getMessage());
        }
    }
}