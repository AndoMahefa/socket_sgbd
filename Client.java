package run;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import table.Relation;
import table.Table;

public class Client {
    public static void main(String[] args) {
        Table t = new Table();
        boolean yes = true;
        while(yes) {
            try {
                System.out.println();
                System.out.print("Enter request: ");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                String result = buffer.readLine();
                if(result.compareToIgnoreCase("bye") == 0) {
                    System.out.println("BYE !");
                    break;
                }
                Socket socket = new Socket("192.168.12.110", 1000);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
                output.flush();
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Relation relation = (Relation)input.readObject();
                relation.display();

                input.close();
                output.close();
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
