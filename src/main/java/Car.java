import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Car implements Runnable{

    private final Home home;
    public IntegerProperty pixels = new SimpleIntegerProperty(0);
    public SimpleStringProperty speed = new SimpleStringProperty("0");
    public SimpleStringProperty meters = new SimpleStringProperty("3000");

    public SimpleStringProperty position = new SimpleStringProperty("1");
    private Timer t;

    public Car(Home home) {
        this.home = home;
    }


    @Override
    public void run() {
        t = resumeRace();
        loop: while (!home.raceFinished){
            System.out.println("Running");
            try{
                synchronized (home){
                    home.wait();
                    if(home.racePaused){
                        System.out.println("Paused");
                        t.cancel();
                    }else{
                        System.out.println("Resumed");
                        t = resumeRace();
                    }
                }
            } catch (InterruptedException e) {
                resetCar();
                break loop;
            }
        }
    }

    private Timer resumeRace(){
        t =  new Timer();
        t.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
            Platform.runLater(()->{
                int metersPerSecond = Utils.randomNumber(80, 85);
                speed.set(String.valueOf((int)(metersPerSecond * 3.6)));
                meters.set(String.valueOf(Integer.parseInt(meters.get()) - metersPerSecond));

            });
            }
        },0,1000);
        return t;
    }

    private void resetCar(){
        pixels.set(0);
        t.cancel();
    }

    private String position() {
        List<Car> cars = new ArrayList<Car>();
        if (!home.car1.equals(this)) cars.add(home.car1);
        if (!home.car2.equals(this)) cars.add(home.car2);
        if (!home.car3.equals(this)) cars.add(home.car3);

       int myMetersLeft = Integer.parseInt(meters.get());
       int car1MetersLeft = Integer.parseInt(cars.get(0).meters.get());
       int car2MetersLeft = Integer.parseInt(cars.get(1).meters.get());

         if(myMetersLeft > car1MetersLeft && myMetersLeft > car2MetersLeft) {
             return "3";
         }else if(myMetersLeft <= car1MetersLeft && myMetersLeft <= car2MetersLeft) {
             return "1";
         }else{
             return "2";
         }
    }

}
