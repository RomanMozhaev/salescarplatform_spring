package ru.job4j.repository;

import ru.job4j.domain.Car;
import ru.job4j.domain.User;

import java.util.Calendar;
import java.util.List;

public interface ConnectionInterface {

    int addUser(User user);

    boolean addCar(Car car);

    List<Car> carsByUser(User user);

    List<Car> allCars();

    boolean changeStatus(Car car);

    List<String> allBrands();

    List<Car> filterCarsByBrandPicDay(String brand, Calendar day);

    List<Car> filterCarsByPicDay(Calendar day);

    List<Car> filterCarsByBrandDay(String brand, Calendar day);

    List<Car> filterCarsByDay(Calendar day);

    List<Car> filterCarsByBrandPic(String brand);

    List<Car> filterCarsByPic();

    List<Car> filterCarsByBrand(String brand);

}