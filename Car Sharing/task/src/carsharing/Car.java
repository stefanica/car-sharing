package carsharing;

public class Car {
    private String name;
    private int id;
    private Integer companyID = null;

    public Car(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCompanyID(Integer id) {
        this.companyID = id;
    }

    public Integer getCompanyID() {
        return this.companyID;
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
