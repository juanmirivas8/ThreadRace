import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    public Label time_rb,time_m,time_f,speed_rb,speed_m,speed_f,pos_rb,pos_m,pos_f,meters_rb,meters_m,meters_f;

    @FXML
    public ImageView ferrari,mercedes,redbull;

    @FXML
    public Button btn_green,btn_red,btn_ambar;

    private Thread t1,t2,t3;
    private Car car1,car2,car3;

    public Boolean racePaused = false, raceStarted = false, raceFinished = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        car3 = new Car(this);

        btn_green.setOnAction(event ->{
            ferrari.xProperty().bindBidirectional(car3.pixels);
            t3 = new Thread(car3);
            t3.start();
        });

        btn_ambar.setOnAction(event -> {
            synchronized (this){
               this.notifyAll();
            }
        });
    }
}
