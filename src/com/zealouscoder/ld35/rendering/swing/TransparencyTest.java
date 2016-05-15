package com.zealouscoder.ld35.rendering.swing;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TransparencyTest {


    public static void main(String[] args) throws IOException {
        Image img = ImageIO.read(Paths.get("resources/puppy.png").toFile());
        JFrame frame = new JFrame("Transparency Test");
        JPanel pane = new JPanel() {
            {
                setPreferredSize(new Dimension(800,600));
            }
            
            public void paintComponent(Graphics init) {
                Graphics2D g = (Graphics2D) init.create();
                g.setColor(new Color(0,0,0,255));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(Color.yellow);
                g.fillRect(0, 0, 100, 100);
                g.scale(10, 10);
                g.drawImage(img, 0, 0, 8, 8, null);
                g.dispose();
            }
        };
        frame.setLayout(new BorderLayout());
        frame.add(pane);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

}
