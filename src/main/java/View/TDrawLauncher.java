package View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class TDrawLauncher extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage stage) {
        View view = new View(stage, new FXMLLoader());
        view.SetTitleAndIcons();
        view.ShowStage();
        view.LoadView();
        view.StartController();
    }
}