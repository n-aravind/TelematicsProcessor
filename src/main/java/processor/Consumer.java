package processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Car;
import utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Consumer extends Thread {

    private List<Car> carList = new ArrayList<>();
    private FileWriter fileWriter = null;

    public Consumer() {
        super();
        writeEmptyHtmlFile();
    }

    private void writeEmptyHtmlFile() {
        try {
            fileWriter = new FileWriter(new File("src/main/resources/report.html"));
            fileWriter.write("<!DOCTYPE html><html><meta http-equiv=\"refresh\" content=\"5\"/><body>");
            fileWriter.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        Path path = Paths.get("src/main/resources/");
        try {
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey watchKey;
            while (true) {
                watchKey = watchService.poll(500, TimeUnit.MILLISECONDS);
                if (watchKey != null) {
                    watchKey.pollEvents().forEach(event -> processEvent(event.context()));
                }
                try {
                    watchKey.reset();
                } catch (NullPointerException e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processEvent(Object context) {
        ObjectMapper mapper = new ObjectMapper();
        Utils.sleepMs(500);
        File file = new File("src/main/resources/" + context.toString());
        try {
            Car car = mapper.readValue(file, Car.class);
            carList.add(car);
            writeToHtml(carList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    private void writeToHtml(List<Car> carList) {
        try {
            fileWriter = new FileWriter(new File("src/main/resources/report.html"));
            fileWriter.write("<!DOCTYPE html><html><meta http-equiv=\"refresh\" content=\"5\"/><body>");
            fileWriter.write(getHeader(carList));
            fileWriter.write("<p><b>All Telematic Entries : </b></p>");
            fileWriter.write(getTable(carList));
            fileWriter.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String getTable(List<Car> carList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table align=\"left\"border=\"1\"><tr><th>VIN Number</th><th>Odometer Miles</th><th>Gallons Of Fuel Remaining</th><th>Date/Time</th></tr>");
        for (int i = carList.size() - 1; i >= 0; i--) {
            stringBuilder.append("<tr><td align=\"center\">").append(carList.get(i).getVinNumber()).append("</td><td align=\"center\">").append(carList.get(i).getOdometerMiles()).append("</td><td align=\"center\">").append(carList.get(i).getRemainingFuelInGallons()).append("</td><td align=\"center\">").append(carList.get(i).getDate()).append("</td></tr>");
        }
        stringBuilder.append("</table>");
        return stringBuilder.toString();
    }

    private Double getAverageFuelRemaining(List<Car> carList) {
        List<Double> remainingFuelInGallons = new ArrayList<>();
        for (Car car : carList) {
            remainingFuelInGallons.add(car.getRemainingFuelInGallons());
        }
        return remainingFuelInGallons.stream().mapToDouble(t -> t).average().getAsDouble();
    }

    private Double getAverageOdometerReading(List<Car> carList) {
        List<Double> odometerReadingList = new ArrayList<>();
        for (Car car : carList) {
            odometerReadingList.add(car.getOdometerMiles());
        }
        return odometerReadingList.stream().mapToDouble(t -> t).average().getAsDouble();
    }

    private String getHeader(List<Car> carList) {
        return "<h2><table align=\"center\" style=\"width:75%\" border=\"0\"><tr><th>Number Of Telematic Entries</th><th>Average Odometer Reading</th><th>Average Fuel Remaining</th></tr><tr><td align=\"center\">" + carList.size() + "</td><td align=\"center\">" + getFormattedDecimal(getAverageOdometerReading(carList)) + "</td><td align=\"center\">" + getFormattedDecimal(getAverageFuelRemaining(carList)) + "</td></tr></table></h2>";
    }

    private String getFormattedDecimal(double amount) {
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(amount);
    }
}
