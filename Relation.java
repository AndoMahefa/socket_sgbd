package table;
import java.io.Serializable;

public class Relation implements Serializable {
    private String table;
    private String[] colonnes;
    private Object[][] valeur;
    public Relation() {
    }
    public Relation(String table, String[] colonnes, Object[][] valeur) {
        this.table = table;
        this.colonnes = colonnes;
        this.valeur = valeur;
    }
    public Relation(String hhaha) {
        this.table = hhaha;
        this.colonnes = null;
        this.valeur = null;
    }
    public Relation(Exception e) {
        this.table = e.getMessage();
        this.colonnes = null;
        this.valeur = null;
    }
    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }
    public String[] getColonnes() {
        return colonnes;
    }
    public void setColonnes(String[] colonnes) {
        this.colonnes = colonnes;
    }
    public Object[][] getValeur() {
        return valeur;
    }
    public void setValeur(Object[][] valeur) {
        this.valeur = valeur;
    }

    public void display() {
        if(this.colonnes == null && this.valeur == null) {
            System.out.println(this.table);
        } else {
            if(this.valeur.length <= 1) {System.out.println(this.valeur.length + " row selected");}
            else                        {System.out.println(this.valeur.length + " rows selected");}
            System.out.println("table: " + this.getTable().toUpperCase());
            String cols = "";
            String tiret = "";
            for (int i = 0; i < colonnes.length; i++) {
                cols += this.getColonnes()[i].toUpperCase() + "\t\t";
                tiret += "----------------";
            }
            System.out.println(cols);
            System.out.println(tiret);
            String values = "";
            for (int i = 0; i < valeur.length; i++) {
                for (int j = 0; j < valeur[i].length; j++) {
                    values += String.valueOf(valeur[i][j]) + "\t\t";
                }
                System.out.println(values);
                values = "";
            }
        }
    }
}
