package carsharing;

public class Customer {
    private String name;
    private int id;
    private Integer rentedCarID = null;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRentedCarID(Integer rentedCarID) {
        this.rentedCarID = rentedCarID;
    }

    public Integer getRentedCarID() {
        return rentedCarID;
    }
}
