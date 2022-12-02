import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.JAXBManager;
import utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Home implements Initializable {

    @FXML
    public Label speed_rb, speed_m, speed_f, pos_rb, pos_m, pos_f, meters_rb, meters_m, meters_f;

    @FXML
    public ImageView ferrari, mercedes, redbull;

    @FXML
    public Button btn_green, btn_red, btn_ambar;

    @FXML
    public AnchorPane pane;

    @FXML
    public ListView<String> winnersList;
    public Winners winners;
    public Thread t1, t2, t3;
    public Car car1, car2, car3;

    public Boolean racePaused = false, raceStarted = false, raceFinished = false;

    private Timer checker;
    public String winner = "";



    public Home(){
        winners = JAXBManager.unmarshall("src/main/resources/winners.xml", Winners.class);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        winnersList.setItems(FXCollections.observableArrayList(winners.winners));

        car3 = new Car(this, ferrari, "Ferrari", speed_f, pos_f, meters_f);
        car2 = new Car(this, mercedes, "Mercedes", speed_m, pos_m, meters_m);
        car1 = new Car(this, redbull, "Red Bull", speed_rb, pos_rb, meters_rb);

        btn_green.setOnAction(event -> {
            if (!raceStarted) {
                raceStarted = true;
                raceFinished = false;
                t3 = new Thread(car3);
                t2 = new Thread(car2);
                t1 = new Thread(car1);
                checker = activateWinnerAndPositionsRefresh();
                t3.start();
                t2.start();
                t1.start();
            } else {
                Utils.mostrarAlerta("Error", "La carrera ya ha comenzado", "Puedes pausar la carrera pulsando" +
                        " el boton amarillo o terminarla pulsando el boton rojo");
            }
        });

        btn_ambar.setOnAction(event -> {
            if (raceStarted && !raceFinished) {
                synchronized (this) {
                    racePaused = !racePaused;
                    if(racePaused) checker.cancel();
                    else checker = activateWinnerAndPositionsRefresh();
                    this.notifyAll();
                }
            } else {
                Utils.mostrarAlerta("Error", "La carrera no ha comenzado", "Puedes iniciar la carrera pulsando" +
                        " el boton verde");
            }
        });

        btn_red.setOnAction(event -> {
            if (raceStarted) {
                synchronized (this) {
                    raceFinished = false;
                    raceStarted = false;
                    racePaused = false;
                    checker.cancel();
                    t3.interrupt();
                    t2.interrupt();
                    t1.interrupt();
                }
            } else {
                Utils.mostrarAlerta("Error", "La carrera no ha comenzado", "Puedes iniciar la carrera pulsando" +
                        " el boton verde");
            }
        });

        Platform.runLater(() -> {
            Utils.closeRequest((Stage) pane.getScene().getWindow());
        });

    }

    public Timer activateWinnerAndPositionsRefresh() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    pos_f.setText(car3.position());
                    pos_m.setText(car2.position());
                    pos_rb.setText(car1.position());
                    if(raceFinished){
                        raceFinished = false;
                        Utils.mostrarAlerta("Carrera finalizada", "El ganador es: "+winner, "Puedes iniciar una nueva carrera pulsando el boton verde");
                        checker.cancel();
                        winners.addWinner(winner);
                        winnersList.setItems(FXCollections.observableArrayList(winners.winners));
                        JAXBManager.marshall("src/main/resources/winners.xml",winners);
                    }
                });

            }
        }, 500, 100);
        return t;
    }

}
