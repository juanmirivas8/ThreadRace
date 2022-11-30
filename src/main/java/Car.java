import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

import java.util.Timer;

public class Car implements Runnable{

    private Home home;
    private ImageView car;
    public IntegerProperty pixels = new SimpleIntegerProperty(0);

    public Car(Home home, ImageView car) {
        this.home = home;
        this.car = car;
    }
    @Override
    public synchronized void run() {
        Timer t =  new Timer();
        try{

            t.scheduleAtFixedRate(new java.util.TimerTask() {
                @Override
                public void run() {
                    pixels.set(pixels.get()+10);
                }
            },0,100);
            wait();
        }catch (InterruptedException e){
            t.cancel();

        }
    }
}
