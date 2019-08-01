package com.auxolabs.scanner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


class PortScanner extends JFrame implements ActionListener, ChangeListener {


    private boolean displayAll = false;
    private JTextField ipAddress, lowerPort, higherPort;
    private JTextArea output;
    private JScrollPane outputScroller;
    private JCheckBox toggleDisplayAll;
    private JButton scanPorts,cancel;
    private JPanel settingsPanel, outputPanel;
    private JFrame frame;


    public PortScanner() {
        super ( "Port Scanner" );

        initComponents ();

        super.setLayout ( new FlowLayout () );
        super.setSize ( 600, 500 );
        super.setLocationRelativeTo ( null );
        super.setResizable ( false );
        super.setVisible ( true );
    }


    private final void initComponents() {


        this.ipAddress = new JTextField ( 12 );
        this.lowerPort = new JTextField ( 5 );
        this.higherPort = new JTextField ( 5 );


        this.output = new JTextArea ( 10, 20 );
        this.output.setEditable ( false );
        this.output.setLineWrap ( true );
        this.outputScroller = new JScrollPane ( this.output );


        this.toggleDisplayAll = new JCheckBox ( "Display all results (open & closed)" );
        this.toggleDisplayAll.addChangeListener ( this );



        this.scanPorts = new JButton ( " SCAN" );
        this.scanPorts.addActionListener ( this );

        this.cancel = new JButton ( " CANCEL" );
        this.cancel.addActionListener ( this );



        this.settingsPanel = new JPanel ( new FlowLayout () );
        this.settingsPanel.setBorder ( BorderFactory.createTitledBorder ( "Scan information" ) );

        this.settingsPanel.setPreferredSize ( new Dimension ( 230, 135 ) );
        this.settingsPanel.add ( new JLabel ( "IP Address: " ) );
        this.settingsPanel.add ( this.ipAddress );
        this.settingsPanel.add ( new JLabel ( "Port range: " ) );
        this.settingsPanel.add ( this.lowerPort );
        this.settingsPanel.add ( new JLabel ( "-" ) );
        this.settingsPanel.add ( this.higherPort );
        this.settingsPanel.add ( this.toggleDisplayAll );
        this.settingsPanel.add ( this.scanPorts );
        cancel.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ce) {
                dispose ();
                new PortScanner ();
            }
        } );
        this.settingsPanel.add ( this.cancel );
        this.outputPanel = new JPanel ( new FlowLayout () );
        this.outputPanel.setBorder ( BorderFactory.createTitledBorder ( "Results: " ) );
        this.outputPanel.add ( outputScroller );


        super.add ( this.settingsPanel );
        super.add ( this.outputPanel );

    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource () == this.scanPorts) {

            this.output.setText ( "Starting scan..." + System.lineSeparator () );
            scan ( this.ipAddress.getText (), this.lowerPort.getText (), this.higherPort.getText (), 200 );
            this.output.append ( "Scan finished." );
        }
    }

    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource () == toggleDisplayAll) {
            this.displayAll = this.toggleDisplayAll.isSelected ();
        }

    }

    private final void scan(String ipAddress, String lowPort, String highPort, int timeout) {
        int start, end;

        try {
            start = Integer.parseInt ( lowPort );
            end = Integer.parseInt ( highPort );

            if (end <= start) {
                this.output.append ( "The second port must be higher than the first port" + System.lineSeparator () );
                return;
            }
        } catch (NumberFormatException nfe) {
            this.output.append ( "Please enter valid port numbers." + System.lineSeparator () );
            return;
        }


        for (int current = start; current <= end; current++) {
            try {
                Socket s = new Socket ();
                s.connect ( new InetSocketAddress ( ipAddress, current ), timeout );
                s.close ();

                this.output.append ( "Open port: " + current + System.lineSeparator () );
                JOptionPane.showMessageDialog ( frame, current + " port  is open" );
            } catch (IOException ioe) {
                if (this.displayAll) {
                    this.output.append ( "Closed port: " + current + System.lineSeparator () );
                }
            }
        }
    }
}