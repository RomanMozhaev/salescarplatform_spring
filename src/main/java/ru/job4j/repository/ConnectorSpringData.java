package ru.job4j.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.domain.Car;
import ru.job4j.domain.User;

import java.util.Calendar;
import java.util.List;

/**
 * the persistence layer working via Spring Data JPA
 */
@Component
public class ConnectorSpringData implements ConnectionInterface {

    /**
     * the repository for User model.
     */
    private UserRepository userAppRepository;

    /**
     * the repository for Car model.
     */
    private CarRepository carAppRepository;

    @Autowired
    ConnectorSpringData(final UserRepository userAppRepository,
                        final CarRepository carAppRepository) {
        this.carAppRepository = carAppRepository;
        this.userAppRepository = userAppRepository;
    }

    /**
     * adds the new user to the database.
     *
     * @param user - the new user.
     * @return the unique user's id.
     */
    @Transactional
    @Override
    public int addUser(User user) {
        int id = -1;
        User user1 = this.userAppRepository.findByUsername(user.getUsername());
        if (user1 == null) {
            id = this.userAppRepository.save(user).getId();
        }
        return id;
    }

    /**
     * adds the new offer for car selling.
     *
     * @param car - new car for selling
     * @return true if added; otherwise false.
     */
    @Transactional
    @Override
    public boolean addCar(Car car) {
        carAppRepository.save(car);
        return true;
    }

    /**
     * returns the list of the offers tied to the user.
     *
     * @param user the user which added offers.
     * @return the list of the offers.
     */
    @Override
    public List<Car> carsByUser(User user) {
        return carAppRepository.carsByUserId(user.getId());
    }

    /**
     * returns all offers.
     *
     * @return the list of all offers.
     */
    @Override
    public List<Car> allCars() {
        return carAppRepository.allCars();
    }

    /**
     * changes the status of the offer for selling.
     *
     * @param car - contains id and new status of the offer.
     * @return - true if changed; otherwise false.
     */
    @Transactional
    @Override
    public boolean changeStatus(Car car) {
        return carAppRepository.updateCarStatus(car.isSold(), car.getId()) > 0;
    }

    /**
     * returns all brands of cars which were added to the data base.
     *
     * @return the list of brands.
     */
    @Override
    public List<String> allBrands() {
        return carAppRepository.allBrands();
    }

    /**
     * returns the cars which were filtered by brand, picture and day parameter.
     *
     * @param brand - the certain car brand
     * @param day   - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrandPicDay(String brand, Calendar day) {
        return carAppRepository.carsByBrandPicDay(brand, day);
    }

    /**
     * returns the cars which were filtered by picture and day parameter.
     *
     * @param day - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByPicDay(Calendar day) {
        return carAppRepository.carsByPicDay(day);
    }

    /**
     * returns the cars which were filtered by brand and day parameter.
     *
     * @param brand - the certain car brand
     * @param day   - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrandDay(String brand, Calendar day) {
        return carAppRepository.carsByBrandDay(brand, day);
    }

    /**
     * returns the cars which were filtered by day parameter.
     *
     * @param day - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByDay(Calendar day) {
        return carAppRepository.carsByDay(day);
    }

    /**
     * returns the cars which were filtered by brand parameter.
     *
     * @param brand - the certain car brand
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrandPic(String brand) {
        return carAppRepository.carsByBrandPic(brand);
    }

    /**
     * returns the cars which were filtered by picture parameter.
     * returns the car tickets only with pictures
     *
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByPic() {
        return carAppRepository.carsByPic();
    }

    /**
     * returns the cars which were filtered by brand parameter.
     *
     * @param brand - the certain car brand
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrand(String brand) {
        return carAppRepository.carsByBrand(brand);
    }
}
