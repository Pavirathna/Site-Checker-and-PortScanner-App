package com.auxolabs.scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame ( " Site Checker and Port Scanner " );
        JPanel panel = new JPanel ();
        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        JButton button1 = new JButton ( "Site Checker" );
        JButton button2 = new JButton ( "Port Scanner" );
        button1.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SiteChecker ();
            }
        } );
        button2.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PortScanner ();
            }
        } );

        panel.add ( button1 );
        panel.add ( button2 );
        frame .setLayout ( new FlowLayout () );
        frame .setSize ( 600, 500 );
        frame .setLocationRelativeTo ( null );
        frame.add ( panel );
        frame .setResizable ( true );
        frame .setVisible ( true );

    }
}
