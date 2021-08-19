package View;

import Controller.TDrawController;
import Model.DiagramModel;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import static java.lang.System.exit;

public class TDrawLauncher extends Application {
    Stage primaryStage;
    FXMLLoader loader;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage stage) {
        primaryStage = stage;
        loader = new FXMLLoader();

        // Title and Icon:
        primaryStage.setTitle("TDraw");
        try {
            primaryStage.getIcons().add(new Image("/logo.jpg"));
        } catch (Exception err) {
            System.out.println("Failed to load program logo:");
            err.printStackTrace();
        }

        // Show stage
        primaryStage.show();

        // Load view
        loader.setLocation(getClass().getResource("/View.fxml"));
        Group root;
        try {
            root = loader.<Group>load();
            Scene diagram = new Scene(root, 900, 500);
            primaryStage.setScene(diagram);
        } catch (Exception err) {
            System.out.println("Fatal error: failed to load root.");
            err.printStackTrace();
            exit(1);
        }

        // Start controller
        TDrawController controller = (TDrawController)loader.getController();
        controller.SetModel(new DiagramModel());
        controller.SetPrimaryStage(primaryStage);
    }
}