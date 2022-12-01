package utils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

import java.util.logging.Logger;

public class Utils {

    private final static Logger LOGGER = MyLogger.getLogger("/logging.properties");

    public static boolean mostrarConfirmacion(String header,String description) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(header);
        alert.setContentText(description);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    public static void mostrarAlerta(String title, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    public static void closeRequest(Stage stage){
        stage.setOnCloseRequest(windowEvent -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Confirmacion de cierre");
            a.setHeaderText("¿Esta seguro de salir del programa?");
            Stage s =(Stage)a.getDialogPane().getScene().getWindow();
            s.initOwner(stage);
            s.toFront();
            a.showAndWait().filter(buttonType -> buttonType== ButtonType.OK).ifPresentOrElse(buttonType -> {Platform.exit();System.exit(0);},windowEvent::consume);
        });
    }

    public static String showDialogString(Stage stage, String title, String header, String description, int max_characters){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(description);
        Stage s =(Stage)dialog.getDialogPane().getScene().getWindow();
        s.initOwner(stage);
        s.toFront();
        Utils.addTextLimiter(dialog.getEditor(),max_characters);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    public static Integer randomNumber(Integer inf, Integer sup) {
        Integer aux = 0;
        if (inf > sup) {
            aux = inf;
            inf = sup;
            sup = aux;
        }
        return  Integer.valueOf((int)(Math.random() * (sup - inf + 1) + inf));
    }


}
