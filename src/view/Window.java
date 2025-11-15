package view;

import controller.Controller2D;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private Canvas canvas;
    private Toolbar toolbar;

    public Window(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("PGRF1_V2");
        setVisible(true);
        setPreferredSize(new Dimension(width, height + 100));

        canvas = new Canvas(width, height);

        add(canvas);

        pack();

        canvas.setFocusable(true);
        canvas.grabFocus();

    }

    public void setController(Controller2D controller) {
        toolbar = new Toolbar(controller, canvas);
        add(toolbar, BorderLayout.NORTH);
        revalidate();
    }

    public Canvas getCanvas() {return canvas;}
}
