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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        car1 = new Car(this,redbull);
        car2 = new Car(this,mercedes);
        car3 = new Car(this,ferrari);

        btn_green.setOnAction(event ->{
            ferrari.xProperty().bindBidirectional(car3.pixels);
            t3 = new Thread(car3);
            t3.start();
        });

        btn_ambar.setOnAction(event -> {
            t3.interrupt();
        });
    }
}
