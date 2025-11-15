package view;



import controller.Controller2D;
import controller.Mode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Toolbar extends JPanel {

    private final Controller2D controller;
    private Canvas canvas;

    //Reference
    private JButton editButton;

    private JButton primaryColorChooserButton;
    private JButton secondaryColorChooserButton;

    private JButton drawLineButton;
    private JButton drawPolygonButton;
    private JButton drawRectangleButton;

    private JButton fillSeedFillerButton;
    private JButton fillScanLineButton;

    private JButton clipEdgeButton;

    public Toolbar(Controller2D controller, Canvas canvas) {
        this.controller = controller;
        controller.setToolbar(this);

        this.canvas = canvas;


        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(0, 50));
        setBackground(Color.DARK_GRAY);


        //Buttons - Save, Delete and Edit
        generalButtonsPanel();
        //Color
        colorPanel();
        //Módy kreslení a tvarů
        drawModsPanel();
        //Módy vyplňování
        fillModsPanel();
        //Módy ořezávání
        clipModsPanel();


        modeChanged(Mode.DRAW_LINE);

    }

    private void generalButtonsPanel() {
        JPanel mainButtonsPanel = new JPanel();
        mainButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainButtonsPanel.setBackground(Color.DARK_GRAY);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saveImage();
            canvas.requestFocusInWindow();
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            controller.clearCanvas();
            canvas.requestFocusInWindow();
        });

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            setEditMode();
        });



        mainButtonsPanel.add(saveButton);
        mainButtonsPanel.add(deleteButton);
        mainButtonsPanel.add(editButton);

        add(mainButtonsPanel);
    }
    private void colorPanel(){
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        colorPanel.setBackground(Color.DARK_GRAY);


        primaryColorChooserButton = new JButton();
        primaryColorChooserButton.addActionListener(e -> {colorChooser(primaryColorChooserButton); });

        primaryColorChooserButton.setPreferredSize(new Dimension(25, 25));
        primaryColorChooserButton.setBackground(Color.BLACK);

        colorPanel.add(primaryColorChooserButton);

        secondaryColorChooserButton = new JButton();
        secondaryColorChooserButton.addActionListener(e -> {colorChooser(secondaryColorChooserButton); });

        secondaryColorChooserButton.setPreferredSize(new Dimension(25, 25));
        secondaryColorChooserButton.setBackground(Color.BLACK);

        colorPanel.add(secondaryColorChooserButton);

        ColorPanel colorButtonsPanel = new ColorPanel(color -> {
            primaryColorChooserButton.setBackground(color);
            controller.setPrimaryColor(color);
            canvas.requestFocusInWindow();
        });
        colorPanel.add(colorButtonsPanel);

        add(colorPanel);
    }
    private void drawModsPanel(){
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        modePanel.setBackground(Color.DARK_GRAY);


        drawLineButton = new JButton("Line");
        drawLineButton.addActionListener(e -> {
            controller.setMode(Mode.DRAW_LINE);
        });
        modePanel.add(drawLineButton);

        drawPolygonButton = new JButton("Poly.");
        drawPolygonButton.addActionListener(e -> {
            controller.setMode(Mode.DRAW_POLYGON);
        });
        modePanel.add(drawPolygonButton);

        drawRectangleButton = new JButton("Rect.");
        drawRectangleButton.addActionListener(e -> {
            controller.setMode(Mode.DRAW_RECTANGLE);
        });
        modePanel.add(drawRectangleButton);


        add(modePanel);
    }
    private void fillModsPanel(){
        JPanel fillPanel = new JPanel();
        fillPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        fillPanel.setBackground(Color.DARK_GRAY);

        fillSeedFillerButton = new JButton("Fill Seed.");
        fillSeedFillerButton.addActionListener(e -> {
            controller.setMode(Mode.FILL_SEED_FILLER);
        });
        fillPanel.add(fillSeedFillerButton);

        fillScanLineButton = new JButton("Fill Scan.");
        fillScanLineButton.addActionListener(e -> {
            controller.setMode(Mode.FILL_SCAN_LINE);
        });
        fillPanel.add(fillScanLineButton);


        add(fillPanel);
    }
    private void clipModsPanel(){
        JPanel clipPanel = new JPanel();
        clipPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        clipPanel.setBackground(Color.DARK_GRAY);

        clipEdgeButton = new JButton("Clip Edge.");
        clipEdgeButton.addActionListener(e -> {
            controller.setMode(Mode.CLIP_EDGE);
        });
        clipPanel.add(clipEdgeButton);

        add(clipPanel);
    }

    public void setEditMode(){
        switch (controller.getMode()) {
            case DRAW_LINE -> {
                controller.setMode(Mode.EDIT_LINE);
            }
            case EDIT_LINE -> {
                controller.setMode(Mode.DRAW_LINE);
            }
            case DRAW_POLYGON, DRAW_RECTANGLE ->  {
                controller.setMode(Mode.EDIT_POLYGON);
                editButton.setBackground(new Color(153,255,255));
            }
            case EDIT_POLYGON ->  {
                controller.setMode(Mode.DRAW_POLYGON);
            }
            default -> controller.setMode(Mode.EDIT_LINE);
        }
    }

    //Aby se updatnuli tlačítka při kliknutí (jejich vzhled i když kliknu na klávesu pro změnění režimu)
    public void modeChanged(Mode mode) {
        editButton.setBackground(UIManager.getColor("Button.background"));

        drawPolygonButton.setBackground(UIManager.getColor("Button.background"));
        drawLineButton.setBackground(UIManager.getColor("Button.background"));
        drawRectangleButton.setBackground(UIManager.getColor("Button.background"));

        fillSeedFillerButton.setBackground(UIManager.getColor("Button.background"));
        fillScanLineButton.setBackground(UIManager.getColor("Button.background"));

        clipEdgeButton.setBackground(UIManager.getColor("Button.background"));

        switch (mode) {
            case DRAW_LINE, DRAW_STRAIGHT_LINE -> drawLineButton.setBackground(new Color(153,255,255));
            case DRAW_POLYGON -> drawPolygonButton.setBackground(new Color(153,255,255));
            case DRAW_RECTANGLE ->  drawRectangleButton.setBackground(new Color(153,255,255));

            case EDIT_LINE -> {
                editButton.setBackground(new Color(153,255,255));
                drawLineButton.setBackground(new Color(153,255,255));
            }
            case EDIT_POLYGON -> {
                editButton.setBackground(new Color(153,255,255));
                drawPolygonButton.setBackground(new Color(153,255,255));
                drawRectangleButton.setBackground(new Color(153,255,255));
            }

            case FILL_SEED_FILLER -> fillSeedFillerButton.setBackground(new Color(153,255,255));
            case FILL_SCAN_LINE -> fillScanLineButton.setBackground(new Color(153,255,255));

            case CLIP_EDGE ->  clipEdgeButton.setBackground(new Color(153,255,255));
        }
    }

    private void colorChooser(JButton button) {
        Color color = button.getBackground();
        JColorChooser chooser = new JColorChooser(color);

        color = JColorChooser.showDialog(chooser, "Vyber si barvu", color);

        if (color != null) {
            button.setBackground(color);

            if (button == primaryColorChooserButton) {
                controller.setPrimaryColor(color);
            }
            else if (button == secondaryColorChooserButton) {
                controller.setSecondaryColor(color);
            }
        }
        canvas.requestFocusInWindow();
    }

    private void saveImage(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Uložit Obrázek");
        fileChooser.setSelectedFile(new File("Novy Obrazek.png"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                ImageIO.write(canvas.getBufferedImage(), "png", fileToSave);
                System.out.println("Saved: " + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Color getPrimaryColor() {
        return primaryColorChooserButton.getBackground();
    }
}
