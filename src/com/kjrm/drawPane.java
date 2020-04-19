package com.kjrm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * A utility tool for my SS13 project, "NSV13". Specifically for its physics module, "QWER2D". This tool lets you define hitboxes for ships that appear in the game
 *
 * @author kjrm / Kmc2000
 * @version 1.1
 *
 *
 */

public class drawPane extends JFrame {
    private Image edit; //The image we're currently editing
    public int width, height; //The width, height, of the image we're editing. Not our own height
    public ArrayList<Dot> dots; //An arraylist of dot objects, that are draw on screen, and hold the points at which the user clicked to then generate a hitbox.
    public Image bg; //A background image, defaults to space.

    /**
     * Constructor for drawPane objects. No params necessary. We prompt the user to select a file here, and if they don't, we just quit before doing anything expensive.
     */

    public drawPane()
    {
        super("Hitbox tool");
        dots = new ArrayList<Dot>();
        bg = new ImageIcon("images/bg.png").getImage();
        JFileChooser dialogue = new JFileChooser();
        dialogue.setCurrentDirectory(new File(System.getProperty("user.home")));
        if (dialogue.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = dialogue.getSelectedFile();
            String filename = selectedFile.getName();
            String extension = filename.substring(filename.lastIndexOf(".")); //Get the substring of the string after its . this almost always refers to the file extension, as windows etc. won't let you name something foo.foo.png
            if(selectedFile.canRead()){ //First off. Was this file deleted or something? And are we allowed to view this file? Wouldn't want to accidentally priv-esc this would we...
                if(extension.equals(".png") || extension.equals(".jpg")){
                    setup(selectedFile.getAbsolutePath());
                    return;
                }
                else{
                    System.out.println("This program only accepts image files under PNG or JPEG format (.png, .jpg).");
                }
            }
        }
        System.exit(1); //If we got this far, something went wrong. So let's exit.
    }

    /**
     * Sets the background and foreground "edit" image. This also handles window creation and sizing.
     * @param filename = the name of the image file to set as the current "edit" image.
     */

    public void setup(String filename)
    {
        edit = new ImageIcon(filename).getImage();
        height = edit.getHeight(null);
        width = edit.getWidth(null);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageHolder foo = new ImageHolder(edit, this);
        setLayout(null);
        Dimension max = new Dimension(width, height);
        foo.setPreferredSize(max);
        foo.setMaximumSize(max);
        setContentPane(foo);
        foo.addMouseListener(new clickListener(foo, this));

        genButton gen = new genButton("Out", this);
        gen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genButton source = (genButton) e.getSource();
                source.getSource().printOutput();
            }
        });
        foo.setBackground(Color.cyan);
        gen.setVisible(true);
        gen.setSize(20,20);
        getContentPane().add(gen);
        setResizable(false); //Nope
        pack();
        repaint();
        setVisible(true);
    }

    /**
     * Method to add a "clicked" point stored as a dot object.
     * @param x = the x coordinate of the click
     * @param y = the y coordinate of the click
     */

    public void addDot(int x, int y)
    {
        dots.add(new Dot(x,y));
        repaint();
    }

    /**
     *
     * Method to convert the stored dots into copy-pastable BYONDcode. Simply slot this in under your ship as required.
     * This method will write to output.txt and open it in your file viewer.
     *
     */

    public void printOutput()
    {
        String out = "collision_positions = list(";
        for(Dot dot : dots){
            int x = dot.getX()-(edit.getWidth(null) / 2);
            System.out.println(x);
            int y = height-dot.getY()-(edit.getHeight(null) / 2); //We flip the Y around, because java and byond use a different Y origin
            out += "new /datum/vector2d("+x+","+y+")";
            if(dots.indexOf(dot) < dots.size()-1 ){
                out += ", ";
            }
        }
        out += ")";
        System.out.println(out);
        dots = new ArrayList<Dot>();
        repaint();
        try {
            FileWriter writer = new FileWriter("output.txt");
            writer.write(out);
            writer.close();
            Desktop.getDesktop().open(new File("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 *
 * Begin nested classes
 *
 */

/**
 *
 * A class to extend JButton, but with a reference to the drawing panel that's holding it, so that when you click the button, it calls "source's" output method.
 *
 */

class genButton extends JButton
{
    private drawPane source;
    public genButton(String text, drawPane source)
    {
        super(text);
        this.source = source;
    }
    public drawPane getSource()
    {
        return this.source;
    }

}

/**
 *
 * A JComponent used to store images on the canvas, and handle drawing the dots over them with pixel coordinates relative to the image itself.
 *
 */

class ImageHolder extends JComponent
{
    private Image image;
    private drawPane parent;

    public ImageHolder(Image image, drawPane parent)
    {
        this.image = image;
        this.parent = parent;
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.drawImage(parent.bg, 0,0, this);
        int x = (parent.getWidth() - image.getWidth(null)) / 2;
        int y = (parent.getHeight() - image.getHeight(null)) / 2;
        g.drawImage(image, 0, 0, this);
        for(int I=0; I < parent.dots.size()-1; I++){
            Dot dot = parent.dots.get(I);
            g.drawRect(dot.getX() - 3, dot.getY() - 3, 6, 6);
            Dot next = parent.dots.get(I+1);
            g.drawRect(next.getX() - 3, next.getY() - 3, 6, 6);
            g.drawLine(dot.getX(), dot.getY(), next.getX(), next.getY());
        }
        if(parent.dots.size() > 0) {
            Dot first = parent.dots.get(0);
            Dot last = parent.dots.get(parent.dots.size()-1);
            g.drawRect(first.getX() - 3, first.getY() - 3, 6, 6);
            g.drawLine(first.getX(), first.getY(), last.getX(), last.getY());
        }
    }
}

/**
 *
 * A class used for storing clicks made by the user, with an x,y coordinate set attached.
 *
 */

class Dot{
    private int x, y;

    public Dot(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }

}

/**
 *
 * An event handler implementing MouseListener that handles adding a new dot every time it's clicked.
 *
 */

class clickListener implements MouseListener
{

    private ImageHolder source;
    private drawPane panel;

    public clickListener(ImageHolder source, drawPane panel)
    {
        super();
        this.source = source;
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
        Point p = new Point(arg0.getX(), arg0.getY());
        panel.addDot(p.x, p.y);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) { }

    @Override
    public void mouseExited(MouseEvent arg0) { }

    @Override
    public void mousePressed(MouseEvent arg0) { }

    @Override
    public void mouseReleased(MouseEvent arg0) { }
}