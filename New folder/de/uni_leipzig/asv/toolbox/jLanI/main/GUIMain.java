/*
 *
 */
package de.uni_leipzig.asv.toolbox.jLanI.main;

import javax.swing.JFrame;

import de.uni_leipzig.asv.toolbox.jLanI.gui.MainPane;
/**
 * @author Alexandr Uciteli
 * @version 1.0
 */
public class GUIMain
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame("JLanI");
		f.pack();
        MainPane mp = new MainPane();
        f.getContentPane().add(mp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(100,100,800,600);
		f.setResizable(false);
        f.setVisible(true);
    }
}
