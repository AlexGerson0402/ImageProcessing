package gersona;


import edu.msoe.cs1021.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImageIO {
    //static Scanner read;

    public static Image read(Path path)throws NullPointerException,IllegalArgumentException,IOException{
        //Reads in the specified image file and returns
        //a javafx.scene.image.Image object containing the image.
        //QUESTION!!!
        String filename = path.getFileName().toString();
        String filetype = "";
        String[] validtypes = {".jpg", ".png", ".tiff", ".msoe",".bmsoe",
        ".JPG",".PNG",".TIFF",".MSOE",".BMSOE"};
        boolean valid = false;
        for(int i=0; i<filename.toCharArray().length; i++){
            if(filename.toCharArray()[i] == '.'){
                for(int j=i; j<filename.toCharArray().length; j++){
                    filetype += filename.toCharArray()[j];
                }
            }
        }
        for(String type : validtypes){
            if(filetype.equals(type)){
                valid = true;
            }
        }
        //throw exception here
        if(filetype.equals(".msoe")){
            return readMSOE(path);
        } else if(filetype.equals(".bmsoe")){
            return readBMSOE(path);
        } else if(valid == false){
            throw new IllegalArgumentException("no Image"); //QUESTION!!!
        } else if(valid){
            return ImageUtil.readImage(path);
        } else{
            return null;
        }


    }

    public static void write(Image image, Path path){
        //Writes the specified image to the specified path.
        //If the extension on the file passed to either of these methods is .msoe
        //then the appropriate private class method below is called to do the actual work.
        String filename = path.getFileName().toString();
        String filetype = "";
        String[] validtypes = {".jpg", ".png", ".tiff", ".msoe",".bmsoe",
                ".JPG",".PNG",".TIFF",".MSOE",".BMSOE"};
        boolean valid = false;
        for(int i=0; i<filename.toCharArray().length; i++){
            if(filename.toCharArray()[i] == '.'){
                for(int j=i; j<filename.toCharArray().length; j++){
                    filetype += filename.toCharArray()[j];
                }
            }
        }
        for(String type : validtypes){
            if(filetype.equals(type)){
                valid = true;
            }
        }
        try{
            if(filetype.equals(".msoe")){
                writeMSOE(image, path);
            } else if(filetype.equals(".bmsoe")){
                writeBMSOE(image, path);
            } else if(valid == false){
                throw new IllegalArgumentException("invalid file extension"); //QUESTION!!!
            } else if(valid){
                ImageUtil.writeImage(path, image);
            }
        } catch (IOException ioe){
            System.out.println("IOException when reading Image");
        } catch(IllegalArgumentException iae){
            System.out.println("invalid image type");
        } catch (NullPointerException ne){
            System.out.println("No MSOE FILE");
        }
    }

    public static Image readMSOE(Path path) throws NullPointerException{
        //Reads an image file in .msoe format.
        int width, height;
        File file = path.toFile();
        Scanner read;
        WritableImage msoe;
        try{
            read = new Scanner(file);
           if(!read.nextLine().equals("MSOE")){
                System.out.println("not a MSOE file");
                return null;
            }
            width = Integer.parseInt(read.next());
            height = Integer.parseInt(read.next());
            msoe = new WritableImage(width,height);
            PixelWriter write = msoe.getPixelWriter();

            //traverse through rows and columns
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    write.setColor(j,i, Color.web(read.next()));
                }
            }
            return msoe;

        } catch (IOException ier){
            System.out.println("IO exception");
            return null;
        } catch (IllegalArgumentException iie){
            System.out.println("");
            return null;
        }

    }

    public static Image transformImage(Image image, Transformable transform) {
        WritableImage im = (WritableImage) image;
        int width = (int)im.getWidth();
        int height = (int)im.getHeight();
        PixelReader read = im.getPixelReader();
        PixelWriter write = im.getPixelWriter();
        for(int i=0; i<height; i++){
            for(int j=0;j<width; j++){
                Color newpix = transform.apply(i,read.getColor(j,i));
                write.setColor(j,i,newpix);
            }
        }
        return im;
    }

    public static void writeMSOE(Image image, Path path){
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        PixelReader read = image.getPixelReader();
        try {
            File file = path.toFile();
            PrintWriter write = new PrintWriter(file);
            write.println("MSOE");
            write.println(width+" "+height);
            for(int i=0; i<height; i++){
                for(int j=0;j<width; j++){
                    write.print(read.getColor(j,i)+" ");
                }
                write.print("\n");
            }
            write.close();

        } catch (IOException ie){
            System.out.println("IO exception, no msoe image");
        }
    }

    public static Image readBMSOE(Path path){
        int width, height;
        WritableImage msoe;
        try{
            File file = path.toFile();
            DataInputStream in = new DataInputStream(
                    new FileInputStream(file));
            for(int i=0;i<5;i++){
                Byte doNothing = in.readByte();
            }

            width = in.readInt();
            height = in.readInt();
            msoe = new WritableImage(width,height);
            PixelWriter write = msoe.getPixelWriter();

            //traverse through rows and columns
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    write.setColor(j,i, intToColor(in.readInt()));
                }
            }
            return msoe;

        } catch (IOException ier){
            System.out.println("IO exception");
            return null;
        } catch (IllegalArgumentException iie){
            System.out.println("");
            return null;
        }
    }

    public static void writeBMSOE(Image image, Path path){
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        PixelReader read = image.getPixelReader();
        try {
            File file = path.toFile();
            DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(file));
            try{
                out.writeByte('B');out.writeByte('M');out.writeByte('S');
                out.writeByte('O');out.writeByte('E');
                out.writeInt(width); out.writeInt(height);

                for(int i=0; i<height; i++){
                    for(int j=0;j<width; j++){
                        out.writeInt(colorToInt(read.getColor(j,i)));
                    }
                }
            } finally {
                out.close();
            }

        } catch (IOException ie){
            System.out.println("IO exception, no msoe image");
        }
    }

    private static Color intToColor(int color) {
        double red = ((color >> 16) & 0x000000FF)/255.0;
        double green = ((color >> 8) & 0x000000FF)/255.0;
        double blue = (color & 0x000000FF)/255.0;
        double alpha = ((color >> 24) & 0x000000FF)/255.0;
        return new Color(red, green, blue, alpha);
    }
    private static int colorToInt(Color color) {
        int red = ((int)(color.getRed()*255)) & 0x000000FF;
        int green = ((int)(color.getGreen()*255)) & 0x000000FF;
        int blue = ((int)(color.getBlue()*255)) & 0x000000FF;
        int alpha = ((int)(color.getOpacity()*255)) & 0x000000FF;
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }


}

