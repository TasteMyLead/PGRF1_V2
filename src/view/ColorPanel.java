package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;

public class ColorPanel extends JPanel {
    private Color selectedColor;

    public interface ColorSelectedListener {
        void colorSelected(Color color);
    }

    public ColorPanel(ColorSelectedListener listener) {
        setLayout(new GridLayout(3,4,2,3));
        setBackground(Color.DARK_GRAY);

        Color[] colors = {
                Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
                Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA,
                Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY,
        };

        for (Color color : colors) {
            JPanel colorSquare = new JPanel();
            colorSquare.setBackground(color);
            colorSquare.setPreferredSize(new Dimension(10,10));
            add(colorSquare);

            colorSquare.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            colorSquare.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    listener.colorSelected(color);
                }
            });

            add(colorSquare);
        }
    }
}
