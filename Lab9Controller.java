package gersona;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

import static yangh.ImageIO.read;
import static yangh.ImageIO.write;

public class Lab9Controller {

    private File recentFile;

    @FXML
    private ImageView imageBox;
    @FXML
    private Label hexLabel;

    @FXML
    private Button filterButton;

    boolean toggle = true;

    public Image getImage(){
        return imageBox.getImage();
    }

    public void setImage(Image im){
        imageBox.setImage(im);
    }

    @FXML
    private Stage secondStage;

    @FXML
    private void load(ActionEvent event){
        //open image
        File file;
        FileChooser FChooser = new FileChooser();
        file = FChooser.showOpenDialog(null);
        Path path = file.toPath();
        try{
            imageBox.setImage(read(path));
            recentFile = file;
        } catch (InputMismatchException ee){
            System.out.println("invalid color, start over");
        } catch(IOException ioe){
            System.out.println("IOException when reading Image");
        } catch(IllegalArgumentException iae){
            System.out.println("invalid image type");
        }

    }
    @FXML
    private void reload(ActionEvent event){
        //reloads the most recently loaded image (does not show the file chooser dialog).
        try{
            Path path = recentFile.toPath();
            imageBox.setImage(read(path));
        } catch (NullPointerException ne){
            System.out.println("No MSOE FILE");
        } catch(IOException ioe){
            System.out.println("IOException when reading Image");
        } catch(IllegalArgumentException iae){
            System.out.println("invalid image type");
        }
    }

    @FXML
    private void save(ActionEvent event){
        FileChooser FChooser = new FileChooser();
        File file = FChooser.showSaveDialog(null);
        Path path = file.toPath();
        try{
            write(imageBox.getImage(), path);
        } catch (NullPointerException ne) {
            System.out.println("No Image to Save");
        } catch (InputMismatchException ee){
            System.out.println("invalid color, start over");
        }
    }

    @FXML
    private void greyscale(ActionEvent event){
        imageBox.setImage(ImageIO.transformImage(imageBox.getImage(),(y, color)->
                color.grayscale()));
    }

    @FXML
    private void negative(ActionEvent event){
        imageBox.setImage(ImageIO.transformImage(imageBox.getImage(),(y, color)->
                color.invert()));
    }

    @FXML
    private void red(ActionEvent event){
        imageBox.setImage(ImageIO.transformImage(imageBox.getImage(),(y, color)->
                new Color(color.getRed(),0,0,color.getOpacity())));
    }

    @FXML
    private void redGrey(ActionEvent event){
        //every pixel on an odd numbered line contains
        //only the red color data and every pixel on an even
        //numbered line contains the grayscale representation of the color.
        imageBox.setImage(ImageIO.transformImage(imageBox.getImage(),(y, color)->
        {if(y%2==0){
            return new Color(color.getRed(),0,0,color.getOpacity());
         }else{
            return color.grayscale();}
        }));

    }

    @FXML
    private void showFilter(ActionEvent event){
        if(toggle){
            open();
            filterButton.setText("Hide Filter");
        } else{
            close();
            filterButton.setText("Show Filter");
        }
        toggle = !toggle;
    }




    public void setStage(Stage sc) {
        this.secondStage = sc;
    }

    @FXML
    private void open() {
        secondStage.show();
    }

    @FXML
    private void close() {
        secondStage.close();
    }

}
