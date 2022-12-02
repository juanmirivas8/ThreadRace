import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    public Label speed_rb,speed_m,speed_f,pos_rb,pos_m,pos_f,meters_rb,meters_m,meters_f;

    @FXML
    public ImageView ferrari,mercedes,redbull;

    @FXML
    public Button btn_green,btn_red,btn_ambar;

    @FXML
    public AnchorPane pane;

    public Thread t1,t2,t3;
    public Car car1,car2,car3;

    public Boolean racePaused = false, raceStarted = false, raceFinished = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        car3 = new Car(this, ferrari);
        car2 = new Car(this, mercedes);
        car1 = new Car(this, redbull);
        btn_green.setOnAction(event ->{
            if(!raceStarted){
                raceStarted = true;
                raceFinished = false;
                t3 = new Thread(car3);
                t3.start();
            }else{
                Utils.mostrarAlerta("Error","La carrera ya ha comenzado","Puedes pausar la carrera pulsando" +
                        " el boton amarillo o terminarla pulsando el boton rojo");
            }
        });

        btn_ambar.setOnAction(event -> {
            if(raceStarted && !raceFinished) {
                synchronized (this) {
                    racePaused = !racePaused;
                    this.notifyAll();
                }
            }else{
                Utils.mostrarAlerta("Error","La carrera no ha comenzado","Puedes iniciar la carrera pulsando" +
                        " el boton verde");
            }
        });

        btn_red.setOnAction(event -> {
            if(raceStarted){
                synchronized (this){
                    raceFinished = true;
                    raceStarted = false;
                    racePaused = false;
                    t3.interrupt();
                }
            }else{
                Utils.mostrarAlerta("Error","La carrera no ha comenzado","Puedes iniciar la carrera pulsando" +
                        " el boton verde");
            }
        });

        Platform.runLater(() ->{
            Utils.closeRequest((Stage)pane.getScene().getWindow());
        });

        bindings();
    }

    private void bindings(){
        ferrari.xProperty().bindBidirectional(car3.pixels);
        speed_f.textProperty().bindBidirectional(car3.speed);
        pos_f.textProperty().bindBidirectional(car3.position);
        meters_f.textProperty().bindBidirectional(car3.meters);

        mercedes.xProperty().bindBidirectional(car2.pixels);
        speed_m.textProperty().bindBidirectional(car2.speed);
        pos_m.textProperty().bindBidirectional(car2.position);
        meters_m.textProperty().bindBidirectional(car2.meters);

        redbull.xProperty().bindBidirectional(car1.pixels);
        speed_rb.textProperty().bindBidirectional(car1.speed);
        pos_rb.textProperty().bindBidirectional(car1.position);
        meters_rb.textProperty().bindBidirectional(car1.meters);
    }
}
