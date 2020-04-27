package ru.job4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.repository.ConnectionInterface;
import ru.job4j.domain.Car;
import ru.job4j.domain.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * the class for preparing data for processing with data base.
 */
@Service
public class ServiceImpl implements ServiceInterface {

    /**
     * the connector to data base class instance.
     */
    private final ConnectionInterface connector;

    @Autowired
    public ServiceImpl(final ConnectionInterface connector) {
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
        return this.connector.addCar(car);
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
        List<Car> result;
        if (day) {
            Calendar now = new GregorianCalendar();
            Calendar calendarDay = new GregorianCalendar(
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DATE)
            );
            if (photo) {
                if (!brand.equals("none")) {
                    result = this.connector.filterCarsByBrandPicDay(brand, calendarDay);
                } else {
                    result = this.connector.filterCarsByPicDay(calendarDay);
                }
            } else {
                if (!brand.equals("none")) {
                    result = this.connector.filterCarsByBrandDay(brand, calendarDay);
                } else {
                    result = this.connector.filterCarsByDay(calendarDay);
                }
            }
        } else {
            if (photo) {
                if (!brand.equals("none")) {
                    result = this.connector.filterCarsByBrandPic(brand);
                } else {
                    result = this.connector.filterCarsByPic();
                }
            } else {
                if (!brand.equals("none")) {
                    result = this.connector.filterCarsByBrand(brand);
                } else {
                    result = this.connector.allCars();
                }
            }
        }

        return result;
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
