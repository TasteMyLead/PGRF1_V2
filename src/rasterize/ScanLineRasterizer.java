package rasterize;

import model.Edge;
import model.Point;
import model.PolygonModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ScanLineRasterizer {
    private final LineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;

    public ScanLineRasterizer(LineRasterizer lineRasterizer, PolygonRasterizer polygonRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.polygonRasterizer = polygonRasterizer;
    }


    public void rasterize(PolygonModel polygonModel, Color primaryColor, Color secondaryColor) {
        ArrayList<Edge> edges = getEdges(polygonModel);

        int yMin = edges.get(0).getY1();
        int yMax = edges.get(0).getY2();

        for (Edge edge : edges) {
            if (yMin > edge.getY1()) yMin = edge.getY1();
            if (yMax < edge.getY2()) yMax = edge.getY2();
        }


        for (int y = yMin; y<=yMax; y++) {
            if (primaryColor == secondaryColor) {
                fill(getIntersections(y, edges), y, primaryColor);
            }
            else  fill(getIntersections(y, edges), y, primaryColor, secondaryColor);
        }

        //Vykreslení okraje Polygonu
        polygonRasterizer.rasterize(polygonModel);
    }

    //Vykreslení
    private void fill(ArrayList<Integer> intersections, int y, Color primaryColor){
        for (int i = 0; i < intersections.size() - 1; i += 2){
            int x1 = intersections.get(i);
            int x2 = intersections.get(i+1);

            lineRasterizer.rasterize(x1,y,x2,y,primaryColor);
        }
    }

    private void fill(ArrayList<Integer> intersections, int y, Color primaryColor, Color secondaryColor){
        for (int i = 0; i < intersections.size() - 1; i += 2){
            int x1 = intersections.get(i);
            int x2 = intersections.get(i+1);

            if(y % 2 == 0) lineRasterizer.rasterize(x1,y,x2,y,primaryColor);
            else lineRasterizer.rasterize(x1,y,x2,y,secondaryColor);
        }
    }


    //Průsečíky
    private ArrayList<Integer> getIntersections(int y, ArrayList<Edge> edges){
        ArrayList<Integer> intersections = new ArrayList<>();

        for (Edge edge : edges){
            if(edge.isIntersection(y)) intersections.add(edge.getIntersection(y));
        }

        Collections.sort(intersections);
        return intersections;
    }


    //Hrany
    private ArrayList<Edge> getEdges(PolygonModel polygonModel) {
        ArrayList<Edge> edges = new ArrayList<>();

        //Seznam všech potřebných hran
        for (int i = 0; i < polygonModel.getSize(); i++) {

            int indexA = i;
            int indexB;

            //Když jsme na konci polygonu tak mu zadáme první bod
            if (i == polygonModel.getSize() -1) indexB = 0;
            else indexB = i + 1;

            System.out.println(i + " Bod: indexA: " + indexA + " indexB: " + indexB);

            Point a = polygonModel.getPoint(indexA);
            Point b = polygonModel.getPoint(indexB);

            Edge edge = new Edge(a, b);

            //Uložené hrany
            //Nemůžou být horizontální
            //Musí být správně orientované
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }
        return edges;
    }
}
