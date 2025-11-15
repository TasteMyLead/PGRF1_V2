package clip;

import controller.Action;
import controller.ActionStorage;
import model.*;

import java.util.ArrayList;

public class ClipEdge extends Clipper {

    public void rasterize(Edge clippingEdge, ActionStorage actionStorage) {

        for(int index : actionStorage.getModelIndexes()){

            Model model = (Model) actionStorage.getActions().get(index);

            if(model instanceof PolygonModel polygonModel) {
                //models.set(i, clipPolygon(polygonModel, clippingEdge));
            }
            else if(model instanceof Line line) {
                actionStorage.updateAction(index, clipLine(line, clippingEdge));
            }
        }
    }

    private PolygonModel clipPolygon(PolygonModel polygonModel, Edge clippingEdge) {


        return  polygonModel;
    };

    private Line clipLine(Line line, Edge clippingEdge) {

        Point intersection = clippingEdge.getIntersectionLine(line);
        if (intersection == null) return line;
        else return new Line(line.getX1(), line.getY1(), intersection.getX(),intersection.getY(), line.getColor());
    };
}
