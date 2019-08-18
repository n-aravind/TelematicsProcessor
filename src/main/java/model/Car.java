package model;

import lombok.Data;

import java.util.Date;

@Data
public class Car {
    private String vinNumber;
    private double odometerMiles;
    private double remainingFuelInGallons;
    private Date date;

    public Car() {
    }

    public Car(String vinNumber, double odometerMiles, double remainingFuelInGallons) {
        this.vinNumber = vinNumber;
        this.odometerMiles = odometerMiles;
        this.remainingFuelInGallons = remainingFuelInGallons;
        this.date = new Date();
    }
}
