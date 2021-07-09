package View;

import Controller.TDrawController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static java.lang.System.exit;

public class View {
    Stage stage;
    FXMLLoader loader;

    public View(Stage s, FXMLLoader f) {
        stage = s;
        loader = f;
    }

    // Interface: ----------------------------------------------
    public void SetTitleAndIcons() {
        // Title and Icon:
        stage.setTitle("TDraw");
        try {
            stage.getIcons().add(new Image("/logo.jpg"));
        } catch (Exception err) {
            System.out.println("Failed to load program logo:");
            err.printStackTrace();
        }
    }

    public void ShowStage() {
        stage.show();
    }

    public void LoadView() {
        // Get view
        loader.setLocation(getClass().getResource("/View.fxml"));
        Group root;
        try {
            root = loader.<Group>load();
            Scene diagram = new Scene(root, 900, 500);
            stage.setScene(diagram);
        } catch (Exception err) {
            System.out.println("Fatal error: failed to load root.");
            err.printStackTrace();
            exit(1);
        }
    }

    public void StartController() {
        TDrawController controller = (TDrawController)loader.getController();
        controller.PassViewToDiagram(this);
//        controller.CreateDiagram(stage);
    }
}
