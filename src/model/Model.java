package model;

import controller.Action;
import rasterize.LineRasterizer;

import java.awt.*;

public abstract class Model extends Action {
    void draw(LineRasterizer lineRasterizer){}
}
