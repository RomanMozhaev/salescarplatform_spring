package ru.job4j.connector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.job4j.models.Car;
import ru.job4j.models.User;

import javax.persistence.*;
import java.beans.PropertyEditorSupport;
import java.net.UnknownServiceException;
import java.util.*;
import java.util.function.Function;

/**
 * the class for connecting to Data Base.
 */
public class Connector implements ConnectionInterface {

    /**
     * the Entity Manager Factory
     */
    private final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("SaleCar");

    /**
     * the Instance of this class.
     */
    private static final Connector INSTANCE = new Connector();

    /**
     * the default constructor.
     */
    private Connector() {
    }

    /**
     * the getter for singleton.
     *
     * @return the instance.
     */
    public static Connector getInstance() {
        return INSTANCE;
    }

    /**
     * adds the new user to the database.
     *
     * @param user - the new user.
     * @return the unique user's id.
     */
    @Override
    public int addUser(User user) {
        int result = -1;
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            result = user.getId();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return result;
    }

    /**
     * checks the credential for the sign in
     *
     * @param user - includes name and password for checking.
     * @return - User if found; otherwise null.
     */
    @Override
    public User isCredential(User user) {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        User foundUser = null;
        try {
            tx.begin();
            Query q = em.createQuery("select u From User u where u.name like :userName and u.password like :userPassword");
            q.setParameter("userName", user.getName());
            q.setParameter("userPassword", user.getPassword());
            foundUser = (User) q.getSingleResult();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return foundUser;
    }

    /**
     * changes the status of the offer for selling.
     *
     * @param car - contains id and new status of the offer.
     * @return - true if changed; otherwise false.
     */
    @Override
    public boolean changeStatus(Car car) {
        boolean result = false;
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query q = em.createQuery("select c From Car c where c.id = :carId");
            q.setParameter("carId", car.getId());
            Car foundCar = (Car) q.getSingleResult();
            foundCar.setSold(car.isSold());
            em.merge(foundCar);
            tx.commit();
            result = true;
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return result;
    }

    /**
     * adds the new offer for car selling.
     *
     * @param car - new car for selling
     * @return true if added; otherwise false.
     */
    @Override
    public int addCar(Car car) {
        int result = -1;
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(car);
            tx.commit();
            result = car.getId();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return result;
    }

    /**
     * returns the list of the offers tied to the user.
     *
     * @param user the user which added offers.
     * @return the list of the offers.
     */
    @Override
    public List<Car> carsByUser(User user) {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Car> cars = null;
        try {
            tx.begin();
            Query q = em.createQuery("select c From Car c where c.user.id = :userId");
            q.setParameter("userId", user.getId());
            cars = q.getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return cars;
    }

    /**
     * returns all offers.
     *
     * @return the list of all offers.
     */
    @Override
    public List<Car> allCars() {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Car> cars = null;
        try {
            tx.begin();
            Query q = em.createQuery("select c From Car c");
            cars = q.getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return cars;
    }

    /**
     * returns all brands of cars which were added to the data base.
     *
     * @return the list of brands.
     */
    @Override
    public List<String> allBrands() {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<String> brands = null;
        try {
            tx.begin();
            Query q = em.createQuery("select distinct c.brand From Car c");
            brands = q.getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return brands;
    }

    /**
     * returns the list of cars after filters terms.
     *
     * @param day   - tickets for the current day only.
     * @param photo - tickets with photos only.
     * @param brand - tickets of the required car brand.
     * @return the list of the cars.
     */
    public List<Car> filter(boolean day, boolean photo, String brand) {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Car> list = null;
        try {
            tx.begin();
            StringBuilder query = new StringBuilder("select c From Car c");
            boolean where = false;
            if (!brand.equals("none")) {
                query.append(" where c.brand =:carBrand");
                where = true;
            }
            if (photo) {
                if (where) {
                    query.append(" and");
                } else {
                    query.append(" where");
                    where = true;
                }
                query.append(" c.picture != ''");
            }
            if (day) {
                if (where) {
                    query.append(" and");
                } else {
                    query.append(" where");
                }

                query.append(" c.date >:today");
            }

            String qqq = query.toString();
            Query q = em.createQuery(qqq);
            if (!brand.equals("none")) {
                q.setParameter("carBrand", brand);
            }
            if (day) {
                Calendar now = new GregorianCalendar();
                q.setParameter("today", new GregorianCalendar(
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DATE)
                ));
            }
            list = q.getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        return list;
    }
}
