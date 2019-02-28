package yangh;

import edu.msoe.cs1021.ImageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;


public class KernelController {

    Lab9Controller controller = new Lab9Controller();

    @FXML
    private TextField k00;
    @FXML
    private TextField k01;
    @FXML
    private TextField k02;
    @FXML
    private TextField k10;
    @FXML
    private TextField k11;
    @FXML
    private TextField k12;
    @FXML
    private TextField k20;
    @FXML
    private TextField k21;
    @FXML
    private TextField k22;

    private TextField[] TextArr = {k00, k01, k02,
            k10,k11,k12, k20,k21,k22};

    @FXML
    private void blur(ActionEvent event){
        //updating filter kernel values for blurring an image
        double[] kernel = { 0.0,  1.0/9,  0.0,
                1.0/9, 5.0/9, 1.0/9,
                0.0,  1.0/9,  0.0};
        for(int i=0; i<9; i++){
            TextArr[i].setText(""+kernel[i]);
        }
        Image blurredImage = ImageUtil.convolve(controller.getImage(), kernel);
        controller.setImage(blurredImage);
    }

    @FXML
    private void sharpen(ActionEvent event){
        //updating filter kernel values for sharpening an image
        double[] kernel = { 0.0, -1.0,  0.0,
                -1.0,  5.0, -1.0,
                0.0, -1.0,  0.0};
        for(int i=0; i<9; i++){
            TextArr[i].setText(""+kernel[i]);
        }
        Image sharpedImage = ImageUtil.convolve(controller.getImage(), kernel);
        controller.setImage(sharpedImage);
    }

    @FXML
    void apply(ActionEvent event){
        //applying a filter operation to an image.
        double[] kernel;
        kernel = new double[9];
        for(int i=0; i<9; i++){
            kernel[i] = Double.parseDouble(TextArr[i].getText());
        }
        Image customImage = ImageUtil.convolve(controller.getImage(), kernel);
        controller.setImage(customImage);
    }

}
