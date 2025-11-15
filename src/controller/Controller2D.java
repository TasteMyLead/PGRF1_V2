package controller;

import clip.ClipEdge;
import fill.ScanLine;
import fill.SeedFiller;
import math.Vector2D;
import fill.Filler;
import model.*;
import model.Point;
import model.Polygon;
import model.Rectangle;
import rasterize.*;
import view.Canvas;
import view.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;


public class Controller2D {

        private final Canvas canvas;
        private Toolbar toolbar;

        private final LineRasterizer lineRasterizer;
        private final PolygonRasterizer polygonRasterizer;

        private final SeedRasterizer seedRasterizer;
        private final ScanLineRasterizer scanLineRasterizer;

        private final ClipEdge clipEdge;

        //Barva
        private  Color primaryColor;
        private  Color secondaryColor;

        //Ukládání objektů
        private ActionStorage actionStorage;

        private PolygonModel currentPolygon = new Polygon();

        int choosenIndex;


        //Módy kreslení
        private Mode currentMode;

        //Pomocné
        private Point startPoint;
        private Point endPoint;

    public Controller2D(Canvas canvas) {
            this.canvas = canvas;
            lineRasterizer = new LineRasterizerTrivial(canvas.getRaster());
            polygonRasterizer = new PolygonRasterizer(lineRasterizer);

            seedRasterizer = new SeedRasterizer(canvas.getRaster());
            scanLineRasterizer = new ScanLineRasterizer(lineRasterizer, polygonRasterizer);

            clipEdge = new ClipEdge();
            //Ukládání objectů
            actionStorage = new ActionStorage();

            //Módy kreslení
            currentMode = Mode.DRAW_LINE;

            initListeners();
            canvas.repaint();

            primaryColor = new Color(0,0,0);
            secondaryColor = new Color(0,0,0);
        }

    //Dám cotroleru referenci na Toolbar
    public void setToolbar(Toolbar toolbar) {
            this.toolbar = toolbar;
    }

    //Metoda abychom dokola nedávali všechno false
    private void allFalse(){
        startPoint = null;
    }

    //Módy kreslení (line, polygon, edit)

    public void setMode(Mode mode) {
        this.currentMode = mode;
        toolbar.modeChanged(mode);

        System.out.println(mode);
        canvas.requestFocusInWindow();

        if (currentPolygon.getSize() >= 3) {
            actionStorage.addAction(currentPolygon);
            savePolygon(currentPolygon);
            drawScene();
        }
    }

    public Mode getMode() {
        return currentMode;
    }

    private void savePolygon(PolygonModel polygon) {
        actionStorage.addAction(polygon);

        if (getMode().equals(Mode.FILL_SCAN_LINE)) {
            ScanLine scanLine = new ScanLine((PolygonModel) actionStorage.getLastAction(),primaryColor,secondaryColor);
            actionStorage.addAction(scanLine);
        }

        this.currentPolygon = new Polygon();
    }

    //Vyčistění plátna
    public void clearCanvas(){
        actionStorage.clearActions();
        currentPolygon.clear();
        startPoint = null;
        currentPolygon = new Polygon();

        canvas.getRaster().clear();

        canvas.repaint();
        canvas.revalidate();
        drawScene();
    }

    //Barva
    public Color getPrimaryColor(){
            return primaryColor;
    }

    public void setPrimaryColor(Color color){
            primaryColor = color;
    }

    public Color getSecondaryColor(){return secondaryColor;}

    public void setSecondaryColor(Color color){secondaryColor = color;}

    //Vykreslí horizontální, vertikální a diagonální přímky
    private Point makeStraightLine(int x1, int y1, int x2, int y2){


        //Svisle a vodorovně
        if(Math.abs(x1 - x2) < Math.abs(y1 - y2)){
            float alpha = Math.abs((float)x1 - x2) /  Math.abs((float)y1 - y2);

            if(alpha < 0.225){
                return new Point(x1, y2, primaryColor);
            }
        }
        else{
            float alpha = Math.abs((float)y1 - y2) /  Math.abs((float)x1 - x2);

            if(alpha < 0.225){
                return new Point(x2, y1, primaryColor);
            }
        }


        //Diagonálně
        double c = Math.pow(((double)x1 - x2),2) +  Math.pow(((double)y1 - y2),2);

        int a =(int) Math.round(Math.sqrt(c/2));

        int x;
        int y;

        if (x2 > x1) x = x1 + a;
        else x = x1 - a;

        if (y2 > y1) y = y1 +  a;
        else y = y1 - a;


        //Zkontroluje jestli je mimo panel
        if (canvas.isOutOfBounderiesWidth(x)){
            if (x2 > x1) {
                x = canvas.getWidth() - 1;

                if (y2 > y1) y = y1 + x - x1;
                else y = y1 - (x - x1);

            }
            else{
                x = 0;

                if (y2 > y1) y = y1 + x1;
                else y = y1 - x1;
            }
        }
        else if (canvas.isOutOfBounderiesHeight(y)){

            if (y2 > y1){
                y = canvas.getHeight() - 1;
                if (x2 > x1) x = x1 + y - y1;
                else x = x1 - (y - y1);
            }
            else{
                y = 0;
                if (x2 > x1) x = x1 + y1;
                else x = x1 - y1;
            }
        }

        return new Point(Math.abs(x), Math.abs(y), primaryColor);
    }

    //Vykreslí Rectangle
    private void makeRectangle(int cx, int cy){
            Rectangle rectangle = new Rectangle();
            rectangle.addPoint(currentPolygon.getPoint(0));
            rectangle.addPoint(currentPolygon.getPoint(1));

            Point a = currentPolygon.getPoint(0);
            Point b = currentPolygon.getPoint(1);


            Vector2D ab = new Vector2D(b.getX() - a.getX(), b.getY() - a.getY());
            Vector2D ac = new Vector2D(cx - a.getX(), cy - a.getY());

            Vector2D normalAB = ab.normalOne();
            if (ac.getX() * normalAB.getX() + ac.getY() * normalAB.getY() < 0){
                normalAB = ab.normalTwo();
            }

            double scale = (ac.getX() * normalAB.getX() + ac.getY() * normalAB.getY()) / (normalAB.length() * normalAB.length());

            Vector2D heightVector = new Vector2D(normalAB.getX() * scale, normalAB.getY() * scale);


            Point cPoint = new Point((int)(b.getX() + heightVector.getX()), (int)(b.getY() + heightVector.getY()), primaryColor);
            Point dPoint = new Point((int)(a.getX() + heightVector.getX()), (int)(a.getY() + heightVector.getY()), primaryColor);

           rectangle.addPoint(cPoint);
           rectangle.addPoint(dPoint);

           currentPolygon = rectangle;
    }


    //Vykreslí uložené elementy
    private void drawScene(){
        canvas.getRaster().clear();

        List<Action> actions = actionStorage.getActions();

        for (Action action : actions){
            switch (action){
                case Model object -> drawObject(object);
                case Filler filler -> drawFiller(filler);
                default -> {}
            }
        }

        //Vykreslení Polygonu
        if (currentPolygon.getSize() > 2){
            polygonRasterizer.rasterize(currentPolygon);
        }

        canvas.repaint();
    }

    private void drawObject(Model object){
        switch (object) {
            case Line line -> {
                line.draw(lineRasterizer);
            }
            case PolygonModel polygonModel -> {
                polygonModel.draw(polygonRasterizer);
            }
            default -> {}
        }
    }

    private void drawFiller(Filler filler){
        switch (filler){
                case SeedFiller seedFiller -> seedFiller.fill(seedRasterizer);
                case ScanLine scanLine -> scanLine.fill(scanLineRasterizer);
                default -> {}
        }
    }


    private void initListeners() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = null;

                int x = e.getX();
                int y = e.getY();


                //Levé tlačítko myši
                if (SwingUtilities.isLeftMouseButton(e)) {


                    if (!canvas.isOutOfBounderies(x, y)){
                        switch (currentMode) {
                            case DRAW_LINE, DRAW_STRAIGHT_LINE, CLIP_EDGE -> {
                                startPoint = new Point(e.getX(), e.getY(), primaryColor);
                            }
                            case EDIT_LINE -> {
                                int index = 0;
                                for (Model object : actionStorage.getModels()) {
                                    if (object instanceof Line line) {
                                        int choosenPoint = line.getChoosenPoint(x, y);
                                        if (choosenPoint == 0) {
                                            choosenIndex = index;
                                            startPoint = line.getEndPoint();
                                        } else if (choosenPoint == 1) {
                                            choosenIndex = index;
                                            startPoint = line.getStartPoint();
                                        }
                                    }
                                    index++;
                                }
                            }
                            case DRAW_POLYGON -> {
                                currentPolygon.addPoint(x,y, primaryColor);
                            }
                            case DRAW_RECTANGLE -> {
                                if (currentPolygon.getSize() > 2){
                                    makeRectangle(x, y);
                                }else currentPolygon.addPoint(new Point(e.getX(), e.getY(), primaryColor));
                            }
                            case EDIT_POLYGON -> {
                                for (Model object : actionStorage.getModels()) {
                                    if (object instanceof PolygonModel polygon) {
                                        choosenIndex = polygon.getIndex(x,y);
                                        currentPolygon = polygon;
                                    }
                                }
                            }
                            case FILL_SEED_FILLER -> {
                                OptionalInt rgbBackGround = canvas.getRaster().getPixel(x,y);
                                Color backGroundColor = new Color(rgbBackGround.getAsInt());
                                SeedFiller seedFiller = new SeedFiller(x,y,primaryColor, secondaryColor, backGroundColor);

                                actionStorage.addAction(seedFiller);
                            }
                        }
                    }
                }


                //Pravé tlačítko myši
                //Mazaní
                else if (SwingUtilities.isRightMouseButton(e)) {

                    if (!canvas.isOutOfBounderies(x, y)){
                        switch (currentMode) {
                            case DRAW_LINE, DRAW_STRAIGHT_LINE, EDIT_LINE -> {
                                int index = 0;
                                for (Action action : actionStorage.getActions()) {
                                    if (action instanceof Line line) {
                                        if (line.getChoosenPoint(x, y) >= 0) {
                                            choosenIndex = index;
                                        }
                                    }
                                    index++;
                                }


                                actionStorage.removeAction(choosenIndex);
                            }
                            case DRAW_POLYGON ,EDIT_POLYGON -> {
                                currentPolygon.removePoint(currentPolygon.getIndex(x, y));
                            }
                        }
                        drawScene();
                    }
                }

                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                if (SwingUtilities.isLeftMouseButton(e)) {

                    if (!canvas.isOutOfBounderies(x, y)) {

                        switch (currentMode) {
                            case DRAW_LINE -> {
                                if (startPoint != null) {
                                    endPoint = new Point(x, y, primaryColor);

                                    actionStorage.addAction(new Line(startPoint, endPoint));
                                    startPoint = null;
                                }
                            }
                            case DRAW_STRAIGHT_LINE -> {
                                if (startPoint != null) {
                                    endPoint = makeStraightLine(startPoint.getX(), startPoint.getY(), x, y);

                                    actionStorage.addAction(new Line(startPoint, endPoint));
                                    startPoint = null;
                                }
                            }
                            case EDIT_LINE -> {
                                if (startPoint != null) {
                                    endPoint = new Point(x, y, primaryColor);

                                    actionStorage.updateAction(choosenIndex, new Line(startPoint, endPoint));
                                }
                            }
                            case  DRAW_POLYGON -> {
                                currentPolygon.setPoint(new Point(x, y,primaryColor), currentPolygon.lastIndex());
                            }
                            case DRAW_RECTANGLE -> {
                                if (currentPolygon.getSize() > 2){
                                    actionStorage.addAction(currentPolygon);
                                    currentPolygon = new Rectangle();
                                }
                            }
                            case CLIP_EDGE -> {
                                endPoint = new Point(x, y, primaryColor);


                                Edge clippingEdge = new Edge(startPoint, endPoint);
                                clipEdge.rasterize(clippingEdge,actionStorage);


                                startPoint = null;
                                endPoint = null;
                            }


                        }
                    }
                    choosenIndex = -1;
                    drawScene();
                    super.mouseReleased(e);

                }
            }
        });




        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();

                    if (!canvas.isOutOfBounderies(x, y)) {
                        drawScene();

                        switch (currentMode) {
                            case DRAW_LINE, CLIP_EDGE -> {
                                if (startPoint != null) {
                                    endPoint = new Point(x, y, primaryColor);

                                    lineRasterizer.rasterize(startPoint, endPoint, startPoint.getColor());
                                }
                            }
                            case EDIT_LINE -> {
                                if (startPoint != null) {
                                    endPoint = new Point(x, y, primaryColor);

                                    actionStorage.updateAction(choosenIndex, new Line(startPoint, endPoint));
                                }
                            }
                            case DRAW_STRAIGHT_LINE -> {
                                if (startPoint != null) {
                                    endPoint = makeStraightLine(startPoint.getX(), startPoint.getY(), x, y);
                                    lineRasterizer.rasterize(startPoint, endPoint, startPoint.getColor());
                                }
                            }
                            case DRAW_POLYGON -> {
                                if (currentPolygon.getSize() > 2) {
                                    currentPolygon.setPoint(x, y, primaryColor, currentPolygon.lastIndex());
                                }
                            }
                            case DRAW_RECTANGLE -> {
                                if (currentPolygon.getSize() > 2) {
                                    makeRectangle(x,y);
                                }
                            }
                            case EDIT_POLYGON -> {
                                currentPolygon.setPoint(x, y, primaryColor, choosenIndex);
                                drawScene();
                            }
                        }
                    }
                }
            }

        });


        //Výběr pomocí kláves
        //Q - Kreslení přímek
        //Držení Shift - Vodorovné, svislé a diagonály

        //R - Kreslení polygonu
        //T - Kreslení obdelníku
        //ENTER - uloží polygon (pokud je menší než 3 tak se smaže a vytvoří se nový)

        //E - Změnění na edit (edituju jen to co kreslím např když kreslím přímku tak můžu editovat jen přímky)
        canvas.addKeyListener(new KeyAdapter() {


            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    allFalse();
                    setMode(Mode.DRAW_POLYGON);
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    allFalse();
                    setMode(Mode.DRAW_LINE);
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    allFalse();
                    setMode(Mode.DRAW_RECTANGLE);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    allFalse();
                    toolbar.setEditMode();
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    setMode(Mode.DRAW_STRAIGHT_LINE);
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearCanvas();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (currentPolygon.getSize() > 2) {
                        actionStorage.addAction(currentPolygon);
                    }
                    currentPolygon = new Polygon();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    setMode(Mode.DRAW_LINE);
                }
            }
        });

    }
}




