package gersona;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lab9 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //configure 1st stage
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Lab9Controller.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Image Manipulator");
        primaryStage.setScene(new Scene(root));

        //add new stage
        Stage stage2 = new Stage();
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("KernelController.fxml"));
        Parent root2 = loader2.load();
        stage2.setScene(new Scene(root2));
        stage2.setTitle("Kernal");

        // Get the controller and make 'em talk
        Lab9Controller controller = loader.getController();
        controller.setStage(stage2);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
