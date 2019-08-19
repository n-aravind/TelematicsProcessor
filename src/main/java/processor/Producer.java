package processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Car;

import java.io.File;
import java.util.Scanner;

public class Producer extends Thread {
    private String vinNumber;
    private double odometerMiles;
    private double remainingFuelInGallons;

    public Producer() {
        super();
    }

    public void run(){
        while(true){
            System.out.println("****************************************");
            displayUserEntryOptions();
            System.out.println("****************************************");
            Car car = new Car(this.vinNumber,this.odometerMiles,this.remainingFuelInGallons);
            writeToTempFile(car);
            System.out.println("Updated Details Successfully");
        }
    }

    private void displayUserEntryOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter VIN number : ");
        this.vinNumber = scanner.nextLine();
        try {
            System.out.print("Enter Odometer Reading in miles : ");
            this.odometerMiles = scanner.nextDouble();
            System.out.print("Enter remaining fuel in gallons : ");
            this.remainingFuelInGallons = scanner.nextDouble();
        }catch (Exception e){
            System.out.println("Invalid type. Please enter a number");
            displayUserEntryOptions();
        }
    }

    private void writeToTempFile(Car car) {
        ObjectMapper mapper = new ObjectMapper();
        File file;
        try {
            // TODO CODEREVIEW do not put generated files under the src tree
            file = File.createTempFile(String.valueOf(System.currentTimeMillis()),".json",new File("src/main/resources/"));
            mapper.writeValue(file,car);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.run();
    }
}
