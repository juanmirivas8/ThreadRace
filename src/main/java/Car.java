import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Car implements Runnable {

    private final Home home;
    private final ImageView carImage;
    private final String name;
    private final Label speedLabel, positionLabel, metersLabel;
    public IntegerProperty pixels = new SimpleIntegerProperty(0);
    public SimpleStringProperty speed = new SimpleStringProperty("0");
    public volatile SimpleStringProperty meters = new SimpleStringProperty("2500");

    public SimpleStringProperty position = new SimpleStringProperty("3");
    private Timer t;

    public Car(Home home, ImageView car, String name, Label speedLabel, Label positionLabel, Label metersLabel) {
        this.home = home;
        this.carImage = car;
        this.name = name;
        this.speedLabel = speedLabel;
        this.positionLabel = positionLabel;
        this.metersLabel = metersLabel;

        Platform.runLater(() -> {
            speedLabel.textProperty().bind(speed);
            metersLabel.textProperty().bind(meters);
            carImage.xProperty().bind(pixels);
        });
    }


    @Override
    public void run() {
        t = resumeRace();
        while (!home.raceFinished) {
            try {
                synchronized (home) {
                    home.wait();
                    if (home.racePaused) {
                        t.cancel();
                    } else {
                        t = resumeRace();
                    }
                }
            } catch (InterruptedException e) {
                reset();
                break;
            }
        }
    }

    private Timer resumeRace() {
        t = new Timer();
        t.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    int metersPerSecond = Utils.randomNumber(75, 85);
                    speed.set(String.valueOf((int) (metersPerSecond * 3.6)));
                    meters.set(String.valueOf(Integer.parseInt(meters.get()) - metersPerSecond));
                    position.set(position());
                    pixels.set(pixels.get() + (metersPerSecond * 700) / 3000);
                    if(Integer.parseInt(meters.get()) <= 0){
                        home.winner = name;
                        meters.set("0");
                        home.raceFinished = true;
                        home.t3.interrupt();
                        home.t2.interrupt();
                        home.t1.interrupt();
                    }
                });

            }
        }, 0, 200);
        return t;
    }

    private void reset() {
        home.raceStarted = false;
        home.racePaused = false;
        Platform.runLater(() -> {
            pixels.set(0);
            speed.set("0");
            meters.set("2500");
            position.set("3");
        });
        t.cancel();
    }

    public String position() {
        List<Car> cars = new ArrayList<>();
        if (home.car1 != null && !this.equals(home.car1)) cars.add(home.car1);
        if (home.car2 != null && !this.equals(home.car2)) cars.add(home.car2);
        if (home.car3 != null && !this.equals(home.car3)) cars.add(home.car3);

        int myMetersLeft = Integer.parseInt(meters.get());
        int car1MetersLeft = Integer.parseInt(cars.get(0).meters.get());
        int car2MetersLeft = Integer.parseInt(cars.get(1).meters.get());

        if (myMetersLeft >= car1MetersLeft && myMetersLeft >= car2MetersLeft) {
            return "3";
        } else if (myMetersLeft <= car1MetersLeft && myMetersLeft <= car2MetersLeft) {
            return "1";
        } else {
            return "2";
        }
    }
}
