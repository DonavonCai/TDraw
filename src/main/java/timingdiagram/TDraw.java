package timingdiagram;
// grouping
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
// icon
import javafx.scene.image.Image;
// fxml
import javafx.fxml.FXMLLoader;

import static java.lang.System.exit;

public class TDraw extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        // Title and Icon:
        primaryStage.setTitle("TDraw");
        try {
            primaryStage.getIcons().add(new Image("/logo.jpg"));
        } catch (Exception err) {
            System.out.println("Failed to load program logo:");
            err.printStackTrace();
        }
        primaryStage.show();

        // Layout:
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/TDraw.fxml"));
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

        FXMLController controller = loader.getController();
        controller.setStageAndBindWidth(primaryStage); // updates width of page to stage passed in

    }
}