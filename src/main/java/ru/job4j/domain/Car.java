package ru.job4j.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;

/**
 * the model of the offer for car selling.
 */
@Entity
@Table(name = "car_tickets")
public class Car {
    /**
     * unique id.
     */
    @Id
    @GeneratedValue
    private int id;
    /**
     * car type.
     */
    @Column(nullable = false)
    private String type;
    /**
     * car brand.
     */
    @Column(nullable = false)
    private String brand;
    /**
     * car model.
     */
    @Column(nullable = false)
    private String model;
    /**
     * the usage of the vehicle.
     */
    @Column(nullable = false)
    private int usage;
    /**
     * year of the manufacture.
     */
    @Column(nullable = false)
    private int year;
    /**
     * additional information.
     */
    @Column(nullable = false)
    private String description;
    /**
     * the price in RUB.
     */
    @Column(nullable = false)
    private long price;
    /**
     * the path to the picture file.
     */
    @Column(nullable = false)
    private String picture;
    /**
     * the creation date.
     */
    @Column(nullable = false)
    private Calendar date;
    /**
     * the status of the offer; true if sold;
     */
    @Column(nullable = false)
    private boolean sold;
    /**
     * the user who added this offer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "id"))
    private User user;

    public Car() {
    }

    public Car(Map<String, Object> carFields) {
        if (carFields.size() == 8) {
            this.type = (String) carFields.get("type");
            this.brand = (String) carFields.get("brand");
            this.model = (String) carFields.get("model");
            this.usage = Integer.parseInt((String) carFields.get("usage"));
            this.year = Integer.parseInt((String) carFields.get("year"));
            this.description = (String) carFields.get("desc");
            this.price = Integer.parseInt((String) carFields.get("price"));
            this.picture = (String) carFields.get("picPath");
            this.date = new GregorianCalendar();
            this.sold = false;
            this.user = null;
        }
        if (carFields.size() == 2) {
            this.id = (Integer) carFields.get("id");
            this.sold = (Boolean) carFields.get("status");
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return usage == car.usage
                && year == car.year
                && price == car.price
                && sold == car.sold
                && Objects.equals(type, car.type)
                && Objects.equals(brand, car.brand)
                && Objects.equals(model, car.model)
                && Objects.equals(description, car.description)
                && Objects.equals(picture, car.picture)
                && Objects.equals(date, car.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, brand, model, usage, year, description, price, picture, date, sold);
    }
}
