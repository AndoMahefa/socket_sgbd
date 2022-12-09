package run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import table.Relation;
import table.Table;
public class Main {

    public static void action(String result, Table t) {
        if(result.split(" ")[0].compareToIgnoreCase("introduce") == 0) {
            try {
                t.inserer_object(result);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("generate") == 0) {
            try {
                t.creer_table(result);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("give") == 0) {
            if(result.split(" ")[1].compareToIgnoreCase("infos") == 0) {
                try {
                    t.decrire_table(result);
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if(result.split(" ")[1].compareToIgnoreCase("product") == 0) {
                try {
                    Relation r = t.produit_cartesien(result);
                    r.display();
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    Relation r = t.difference(result);
                    r.display();
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("take") == 0) {
            try {
                Relation r = t.selectionner(result);
                r.display();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("connect") == 0) {
            try {
                Relation r = t.jointure(result);
                r.display();
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("project") == 0) {
            try {
                Relation r = t.projection(result);
                r.display();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else if(result.split(" ")[0].compareToIgnoreCase("divide") == 0) {
            try {
                Relation r = t.division(result);
                r.display();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(result.split(" ")[0] + " is not a command.");
        }
    }
    public static void main(String[] naruto) {
        
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
                    yes = false;
                } else {
                    action(result, t);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}