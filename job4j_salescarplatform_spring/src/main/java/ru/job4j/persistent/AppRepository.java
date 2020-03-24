package ru.job4j.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Calendar;
import java.util.List;

@Repository
public interface AppRepository<T, Integer> extends JpaRepository<T, Integer> {

    List<T> carsByUserId(@Param("userId") int userId);

    @Modifying
    boolean updateCarStatus(@Param("sold") boolean sold, @Param("carId") int carId);

    List<String> allBrands();

    List<T> carsByBrandPicDay(@Param("carBrand") String carBrand, @Param("day") Calendar day);

    List<T> carsByPicDay(@Param("day") Calendar day);

    List<T> carsByBrandDay(@Param("carBrand") String carBrand, @Param("day") Calendar day);

    List<T> carsByDay(@Param("day") Calendar day);

    List<T> carsByBrandPic(@Param("carBrand") String carBrand);

    List<T> carsByPic();

    List<T> carsByBrand(@Param("carBrand") String carBrand);

    List<T> allCars();
}
