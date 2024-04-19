package carsharing;

import java.util.List;

public interface CarDAO {
    void createCar(Car car, int id);
    Car getCarById(int id);
    List<Car> getAllCars(int companyID);
    void updateCar(Car car);
    void deleteCar(int id);
}
