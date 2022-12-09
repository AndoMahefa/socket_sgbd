package run;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import table.*;

public class Server {
    public static Relation action(String result, Table t) {
        if(result.split(" ")[0].compareToIgnoreCase("introduce") == 0) {
            try {
                return(new Relation(t.inserer_object(result)));
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("generate") == 0) {
            try {
                return(new Relation(t.creer_table(result)));
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("give") == 0) {
            if(result.split(" ")[1].compareToIgnoreCase("infos") == 0) {
                try {
                    return(new Relation(t.decrire_table(result)));
                } catch(Exception e) {
                    Relation re = new Relation(e);
                    return re;
                }
            } else if(result.split(" ")[1].compareToIgnoreCase("product") == 0) {
                try {
                    Relation r = t.produit_cartesien(result);
                    return r;
                } catch(Exception e) {
                    Relation re = new Relation(e);
                    return re;
                }
            } else {
                try {
                    Relation r = t.difference(result);
                    return r;
                } catch(Exception e) {
                    Relation re = new Relation(e);
                    return re;
                }
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("take") == 0) {
            try {
                Relation r = t.selectionner(result);
                return r;
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("connect") == 0) {
            try {
                Relation r = t.jointure(result);
                return r;
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("project") == 0) {
            try {
                Relation r = t.projection(result);
                return r;
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("divide") == 0) {
            try {
                Relation r = t.division(result);
                return r;
            } catch(Exception e) {
                Relation re = new Relation(e);
                return re;
            }
        }
        return(new Relation(result.split(" ")[0] + " is not a command."));
        
    }

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(1000);
        } catch(Exception e) {
            System.out.println(e);
        }
        Table table = new Table();
        while(true) {
            try {
                Socket socket = server.accept();
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                String result = (String)input.readObject();
                if(result.compareToIgnoreCase("bye") == 0) {
                    break;
                }
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(action(result, table));
                output.flush();

                input.close();;
                output.close();
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
