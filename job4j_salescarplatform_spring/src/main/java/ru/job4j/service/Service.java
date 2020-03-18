package ru.job4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.persistent.ConnectionInterface;
import ru.job4j.persistent.Connector;
import ru.job4j.models.Car;
import ru.job4j.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * the class for preparing data for processing with data base.
 */
@Component
public class Service implements ServiceInterface {

    /**
     * the connector to data base class instance.
     */
    private final ConnectionInterface connector;

    @Autowired
    public Service(final ConnectionInterface connector) {
        this.connector = connector;
    }

    /**
     * loads list of all offers from data base.
     *
     * @return the list of offers.
     */
    @Override
    public List<Car> loadTable() {
        List<Car> cars = this.connector.allCars();
        if (cars == null) {
            cars = new ArrayList<>();
        }
        return cars;
    }

    /**
     * checks credentials of the user.
     *
     * @param user - the user.
     * @return user's id or -1, if the user was not found.
     */
    @Override
    public int isCredential(User user) {
        int result = -1;
        User foundUser = this.connector.isCredential(user);
        if (foundUser != null) {
            result = foundUser.getId();
        }
        return result;
    }

    /**
     * loads the offers which tied to the user.
     *
     * @param user - the user who added offers.
     * @return - the list of offers.
     */
    @Override
    public List<Car> loadByUser(User user) {
        List<Car> cars = null;
        if (user.getId() > 0) {
            cars = this.connector.carsByUser(user);
        }
        if (cars == null) {
            cars = new ArrayList<>();
        }
        return cars;
    }

    /**
     * adds new user.
     *
     * @param user the user for adding.
     * @return user's id or -1 if the user was not added to the data base.
     */
    @Override
    public int addUser(User user) {
        return this.connector.addUser(user);
    }

    /**
     * adds a new offer.
     *
     * @param car the offer for car selling.
     * @return true if added; otherwise false.
     */
    @Override
    public boolean addCar(Car car) {
        return this.connector.addCar(car) != -1;
    }

    /**
     * changes status of the offer.
     *
     * @param car the offer for car selling.
     * @return true if changed; otherwise false.
     */
    @Override
    public boolean changeStatus(Car car) {
        return this.connector.changeStatus(car);
    }

    /**
     * the method returns the list of cars after filters applying.
     *
     * @param day   - current day tickets only.
     * @param photo - tickets with photos only.
     * @param brand - required brand only.
     * @return the list of the cars.
     */
    @Override
    public List<Car> filter(boolean day, boolean photo, String brand) {
        return this.connector.filter(day, photo, brand);
    }

    /**
     * the method returns list of car brands which were added to the data base.
     *
     * @return list of the brands.
     */
    @Override
    public List<String> allBrands() {
        List<String> brands = this.connector.allBrands();
        if (brands == null) {
            brands = new ArrayList<>();
        }
        return brands;
    }
}
