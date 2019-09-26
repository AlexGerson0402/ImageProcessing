package gersona;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface Transformable {
    Color apply(int y, Color color);
}
