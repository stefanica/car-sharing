package carsharing;

public class Company {
    private String name;
    private int id;

    public Company(String name) {
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

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
