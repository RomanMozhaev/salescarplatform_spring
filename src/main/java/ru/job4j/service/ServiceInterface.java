package ru.job4j.service;

import ru.job4j.domain.Car;
import ru.job4j.domain.User;

import java.util.List;

public interface ServiceInterface {

    List<Car> loadTable();

    List<Car> loadByUser(User user);

    List<Car> filter(boolean day, boolean photo, String model);

    int addUser(User user);

    boolean addCar(Car car);

    boolean changeStatus(Car car);

    List<String> allBrands();


}
