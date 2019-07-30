package com.auxolabs.scanner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame ( " Site Checker and Port Scanner " );
        JPanel panel = new JPanel (  );


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


        button1.setBounds ( 7,4,4,4 );
        panel.add ( button1 );
        panel.add ( button2 );
        frame.setSize ( 400,400 );
        frame.add ( panel );
        frame.setVisible ( true );
        frame.setLayout ( null );
    }
}
