import controller.Controller2D;
import view.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(900, 600);
        Controller2D controller = new Controller2D(window.getCanvas());
        window.setController(controller);
    }
}