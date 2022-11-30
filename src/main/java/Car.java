import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;

import java.util.Timer;

public class Car implements Runnable{

    private final Home home;
    public IntegerProperty pixels = new SimpleIntegerProperty(0);

    public Car(Home home) {
        this.home = home;
    }
    @Override
    public void run() {
        Timer t =  new Timer();
        try{
            t.scheduleAtFixedRate(new java.util.TimerTask() {
                @Override
                public void run() {
                    pixels.set(pixels.get()+10);
                }
            },0,100);
            synchronized (home){
                home.wait();
            }
        }catch (InterruptedException e){
            t.cancel();
        }
    }
}
