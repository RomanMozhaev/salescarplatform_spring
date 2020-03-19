package ru.job4j.persistent;

import org.springframework.stereotype.Component;

import ru.job4j.models.Car;
import ru.job4j.models.User;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;

/**
 * the class for connecting to Data Base.
 */
@Component
public class Connector implements ConnectionInterface {
    /**
     * the Entity Manager Factory
     */
    private final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("SaleCar");

    /**
     * adds the new user to the database.
     *
     * @param user - the new user.
     * @return the unique user's id.
     */
    @Override
    public int addUser(User user) {
        Integer result = transaction(this.emf, em -> {
            em.persist(user);
            return user.getId();
        });
        return result != null ? result : -1;
    }

    /**
     * checks the credential for the sign in
     *
     * @param user - includes name and password for checking.
     * @return - User if found; otherwise null.
     */
    @Override
    public User isCredential(User user) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("credential");
            q.setParameter("userName", user.getName());
            q.setParameter("userPassword", user.getPassword());
            return (User) q.getSingleResult();
        });
    }

    /**
     * changes the status of the offer for selling.
     *
     * @param car - contains id and new status of the offer.
     * @return - true if changed; otherwise false.
     */
    @Override
    public boolean changeStatus(Car car) {
        Object result = transaction(this.emf, em -> {
            Query q = em.createNamedQuery("CarById");
            q.setParameter("carId", car.getId());
            Car foundCar = (Car) q.getSingleResult();
            foundCar.setSold(car.isSold());
            return em.merge(foundCar);
        });
        return result != null;
    }

    /**
     * adds the new offer for car selling.
     *
     * @param car - new car for selling
     * @return true if added; otherwise false.
     */
    @Override
    public boolean addCar(Car car) {
        Object result = transaction(this.emf, em -> {
            em.persist(car);
            return new Object();
        });
        return result != null;
    }

    /**
     * returns the list of the offers tied to the user.
     *
     * @param user the user which added offers.
     * @return the list of the offers.
     */
    @Override
    public List<Car> carsByUser(User user) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("CarsByUser");
            q.setParameter("userId", user.getId());
            return q.getResultList();
        });
    }

    /**
     * returns all offers.
     *
     * @return the list of all offers.
     */
    @Override
    public List<Car> allCars() {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("allCars");
            return q.getResultList();
        });
    }

    /**
     * returns all brands of cars which were added to the data base.
     *
     * @return the list of brands.
     */
    @Override
    public List<String> allBrands() {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("allBrands");
            return q.getResultList();
        });
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
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_BrandPicDay");
            q.setParameter("carBrand", brand);
            q.setParameter("day", day);
            return q.getResultList();
        });
    }

    /**
     * returns the cars which were filtered by picture and day parameter.
     *
     * @param day - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByPicDay(Calendar day) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_PicDay");
            q.setParameter("day", day);
            return q.getResultList();
        });
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
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_BrandDay");
            q.setParameter("carBrand", brand);
            q.setParameter("day", day);
            return q.getResultList();
        });
    }

    /**
     * returns the cars which were filtered by day parameter.
     *
     * @param day - the latest day of car ticket publication
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByDay(Calendar day) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_Day");
            q.setParameter("day", day);
            return q.getResultList();
        });
    }

    /**
     * returns the cars which were filtered by brand parameter.
     *
     * @param brand - the certain car brand
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrandPic(String brand) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_BrandPic");
            q.setParameter("carBrand", brand);
            return q.getResultList();
        });
    }

    /**
     * returns the cars which were filtered by picture parameter.
     * returns the car tickets only with pictures
     *
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByPic() {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_Pic");
            return q.getResultList();
        });
    }

    /**
     * returns the cars which were filtered by brand parameter.
     *
     * @param brand - the certain car brand
     * @return the list of cars
     */
    @Override
    public List<Car> filterCarsByBrand(String brand) {
        return transaction(this.emf, em -> {
            Query q = em.createNamedQuery("Car_Brand");
            q.setParameter("carBrand", brand);
            return q.getResultList();
        });
    }

    /**
     * the wrapper for methods, which return List of cars.
     *
     * @param factory   - Entity Manager factory.
     * @param operation - unique operation for each method.
     * @param <T>       - returning type.
     * @return - result of the unique operation.
     */
    private <T> T transaction(EntityManagerFactory factory, Function<EntityManager, T> operation) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = operation.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return null;
    }
}
