import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

import java.util.Timer;

public class Car implements Runnable{

    private final Home home;
    public IntegerProperty pixels = new SimpleIntegerProperty(0);
    public IntegerProperty speed = new SimpleIntegerProperty(0);
    public IntegerProperty time = new SimpleIntegerProperty(0);
    public IntegerProperty meters = new SimpleIntegerProperty(3000);
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
                pixels.set(pixels.get()+10);
            }
        },0,100);
        return t;
    }

    private void resetCar(){
        pixels.set(0);
        t.cancel();
    }

}
