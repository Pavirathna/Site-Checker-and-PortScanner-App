package model2;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Site_Checker {

        public static void main(String[] args) {
            Scanner sc=new Scanner (System.in);

            try {
               System.out.println ("Enter : ");
               String site=sc.next ();
                URL obj = new URL(site);
              //  URL obj = new URL("http://mkyong.com");
                URLConnection conn = obj.openConnection();
                Map<String, List<String>> map = conn.getHeaderFields();

                System.out.println("Printing Response Header...\n");

                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    System.out.println("Key : " + entry.getKey()
                            + " ,Value : " + entry.getValue());
                }

                System.out.println("\nGet Response Header By Key ...\n");
                String server = conn.getHeaderField("Server");

                if (server == null) {
                    System.out.println("Key 'Server' is not found!");
                } else {
                    System.out.println("Server - " + server);
                }

                System.out.println("\n Done");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

