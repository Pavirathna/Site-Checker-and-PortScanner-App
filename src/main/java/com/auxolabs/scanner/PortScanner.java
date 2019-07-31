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


    private static final long serialVersionUID = 2884600754343147821L;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 375;


    private boolean displayAll = false;


    private JTextField ipAddress, lowerPort, higherPort;
    private JTextArea output;
    private JScrollPane outputScroller;
    private JCheckBox toggleDisplayAll;
    private JButton scanPorts;
    private JPanel settingsPanel, outputPanel;
    private JFrame frame;


    public PortScanner() {
        super ( "Port Scanner" );

        initComponents ();

        super.setLayout ( new FlowLayout () );
        super.setSize ( 600, 700 );
        super.setLocationRelativeTo ( null );
        super.setResizable ( false );
        super.setVisible ( true );
        super.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

    }


    public static void main(String[] args) {
        @SuppressWarnings("unused")
        PortScanner psg = new PortScanner ();
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


        this.scanPorts = new JButton ( "Scan" );
        this.scanPorts.addActionListener ( this );


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

        //Scan ports in range
        for (int current = start; current <= end; current++) {
            try {
                Socket s = new Socket ();
                s.connect ( new InetSocketAddress ( ipAddress, current ), timeout );
                s.close ();

                this.output.append ( "Open port: " + current + System.lineSeparator () );
                JOptionPane.showMessageDialog ( frame,current+" port  is open" );
            } catch (IOException ioe) {
                if (this.displayAll) {
                    this.output.append ( "Closed port: " + current + System.lineSeparator () );
                }
            }
        }
    }

//    public boolean isValid(int start,String end,String  ipAddress,)
//    {
//        Pattern patternS = Pattern.compile(	"^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
//                "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
//                "([).!';/?:,][[:blank:]])?$");
//        Matcher ms = patternS.matcher(stringSite);
//        boolean match2 = ms.matches();
//        Pattern patternM = Pattern.compile ( "^(.+)@(.+)$" );
//        Matcher m = patternM.matcher ( mailId );
//        boolean match = m.matches ();
//        boolean match3 =Pattern.matches ( "^[start-65535]",end);
//        boolean result=false;
//        if(match&&match2&&match3)
//        {
//            result=true;
//        }
//        return result;
//
//    }
} 