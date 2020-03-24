package ru.job4j.persistent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.models.Car;
import ru.job4j.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
public class ConnectorSpringData implements ConnectionInterface {

//    @Autowired
    private AppRepository<User, Integer> userAppRepository;
//    @Autowired
    private AppRepository<Car, Integer> carAppRepository;

    @Autowired
    ConnectorSpringData(final AppRepository<User, Integer> userAppRepository,
                        final AppRepository<Car, Integer> carAppRepository) {
        this.carAppRepository = carAppRepository;
        this.userAppRepository = userAppRepository;
    }


    @Override
    public int addUser(User user) {
        return userAppRepository.save(user).getId();
    }

    @Override
    public boolean addCar(Car car) {
        carAppRepository.save(car);
        return true;
    }

    @Override
    public List<Car> carsByUser(User user) {
        return carAppRepository.carsByUserId(user.getId());
    }

    @Override
    public List<Car> allCars() {
        List<Car> cars = new ArrayList<>();
        carAppRepository.findAll().forEach(cars::add);
        return carAppRepository.allCars();
    }

    @Override
    public User isCredential(User user) {
        Optional<User> optional = userAppRepository.findById(user.getId());
        return optional.orElse(null);
    }

    @Override
    public boolean changeStatus(Car car) {
        return carAppRepository.updateCarStatus(car.isSold(), car.getId());
    }

    @Override
    public List<String> allBrands() {
        return carAppRepository.allBrands();
    }

    @Override
    public List<Car> filterCarsByBrandPicDay(String brand, Calendar day) {
        return carAppRepository.carsByBrandPicDay(brand, day);
    }

    @Override
    public List<Car> filterCarsByPicDay(Calendar day) {
        return carAppRepository.carsByPicDay(day);
    }

    @Override
    public List<Car> filterCarsByBrandDay(String brand, Calendar day) {
        return carAppRepository.carsByBrandDay(brand, day);
    }

    @Override
    public List<Car> filterCarsByDay(Calendar day) {
        return carAppRepository.carsByDay(day);
    }

    @Override
    public List<Car> filterCarsByBrandPic(String brand) {
        return carAppRepository.carsByBrandPic(brand);
    }

    @Override
    public List<Car> filterCarsByPic() {
        return carAppRepository.carsByPic();
    }

    @Override
    public List<Car> filterCarsByBrand(String brand) {
        return carAppRepository.carsByBrand(brand);
    }
}
