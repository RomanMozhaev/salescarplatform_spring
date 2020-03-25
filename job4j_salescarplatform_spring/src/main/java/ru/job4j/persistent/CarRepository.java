package ru.job4j.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.models.Car;

import java.util.Calendar;
import java.util.List;

/**
 * The repository for Car model.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("From Car c where c.user.id = :userId")
    List<Car> carsByUserId(@Param("userId") int userId);

    @Modifying
    @Query("update Car c set c.sold = :sold where c.id = :carId")
    int updateCarStatus(@Param("sold") boolean sold, @Param("carId") int carId);

    @Query("select distinct c.brand From Car c")
    List<String> allBrands();

    @Query("from Car c where c.brand =:carBrand and c.picture not like '' and c.date >:day")
    List<Car> carsByBrandPicDay(@Param("carBrand") String carBrand, @Param("day") Calendar day);

    @Query("from Car c where c.picture not like '' and c.date >:day")
    List<Car> carsByPicDay(@Param("day") Calendar day);

    @Query("from Car c where c.brand =:carBrand and c.date >:day")
    List<Car> carsByBrandDay(@Param("carBrand") String carBrand, @Param("day") Calendar day);

    @Query("from Car c where c.date >:day")
    List<Car> carsByDay(@Param("day") Calendar day);

    @Query("from Car c where c.brand =:carBrand and c.picture not like ''")
    List<Car> carsByBrandPic(@Param("carBrand") String carBrand);

    @Query("from Car c where c.picture not like ''")
    List<Car> carsByPic();

    @Query("from Car c where c.brand =:carBrand")
    List<Car> carsByBrand(@Param("carBrand") String carBrand);

    @Query("from Car")
    List<Car> allCars();
}
