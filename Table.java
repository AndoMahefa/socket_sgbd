package table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import table.Relation;
import variable.Nombre;
import variable.MyDate;

public class Table {
    // * fonction pour avoir la liste des tables qui existent
    public ArrayList<String> liste_tables() {
        File nom = null;
        ArrayList<String> rep = new ArrayList<String>();

        try {
            nom = new File("nom/");
            File[] fichiers = nom.listFiles();
            for (int i = 0; i < fichiers.length; i++) {
                rep.add(fichiers[i].getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return rep;
        }
    }
    // * fonction pour verifier si une chaine de caractere contient des ponctuations
    public boolean contient_ponctuation(String str) {
        char[] chaine = str.toCharArray();
        for (int i = 0; i < chaine.length; i++) {
            if(Character.isLetter(chaine[i]) == false && Character.isDigit(chaine[i]) == false) {
                return true;
            }
        }
        return false;
    }
    // * fonction pour verifier la validite du nom de la table
    public boolean nom_valide(String str) {
        if(this.contient_ponctuation(str) == true) {
            return false;
        }
        char[] chaine = str.toCharArray();
        if(Character.isDigit(chaine[0]) == true) {
            return false;
        }
        return true;
    }
    // * fonction pour savoir si la table existe deja
    public boolean nom_disponible(String str) {
        if(nom_valide(str) == false) {
            return false;
        }
        ArrayList<String> liste_table = this.liste_tables();
        for (int i = 0; i < liste_table.size(); i++) {
            if(str.compareToIgnoreCase(liste_table.get(i)) == 0) {
                return false;
            }
        }
        return true;
    }
    // * fonction pour savoir si le type de la variable est valide
    public boolean type_valide(String str) {
        if(str.compareToIgnoreCase("string") == 0) {
            return true;
        } 
        if(str.compareToIgnoreCase("number") == 0) {
            return true;
        }
        if(str.compareToIgnoreCase("date") == 0) {
            return true;
        }
        return false;
    }
    // * fonction ppur creer la table
    public String creer_table(String requete) throws Exception {
        String[] mots = requete.split(" ");

        // ? verification si la requete est generate
        String generate = mots[0];
        if(generate.compareToIgnoreCase("generate") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-0001. To create a table, use 'GENERATE'.");
        }

        // ? si la requete est generate, continuons
        String chart = mots[1];
        if(chart.compareToIgnoreCase("chart") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-2267. to create a table, use 'CHART'.");
        }

        // ? verification si la table existe deja
        String nom_de_table = mots[2];
        if(this.nom_disponible(nom_de_table) == false) {
            throw new Exception("SYNTAX ERROR: BDD-9762. The table '" + nom_de_table + "' already exists.");
        }

        // ? verification des colonnes
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-0036. You must insert column and types.");
        }
        if(mots.length > 4) {
            throw new Exception("SYNTAX ERROR: BDD-1203. Too many arguments.");
        }
        String attributs = mots[3];
        String[] attributs_types = attributs.split("_");
        for (int i = 0; i < attributs_types.length; i++) {
            // ! si la colonne n'est pas typee
            if(attributs_types[i].split("/").length < 2) {
                throw new Exception("SYNTAX ERROR: BDD-5023. No column type for '" + attributs_types[i] + "'.");
            }
            // ! si le nom de la colonne est invalide
            if(this.nom_valide(attributs_types[i].split("/")[0]) == false) {
                throw new Exception("SYNTAX ERROR: BDD-5202. Column name invalid '" + attributs_types[i].split("/")[0] + "'.");
            }
            // ! si le type de la colonne est invalide
            if(this.type_valide(attributs_types[i].split("/")[1]) == false) {
                throw new Exception("SYNTAX ERROR: BDD-5492. Column type invalid '" + attributs_types[i] + "'.");
            }
        }

        File fichier = new File("nom/" + mots[2].toLowerCase());
        File donnee = new File("donnee/" + mots[2].toLowerCase());
        fichier.createNewFile();
        donnee.createNewFile();
        FileWriter ecrivain = new FileWriter(fichier);
        ecrivain.write(mots[3]);
        ecrivain.close();

        return("Chart '" + nom_de_table + "' create successfully.");
    }


    public String infos_table(String nom_de_table) throws Exception {
        if(this.nom_disponible(nom_de_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_de_table + "' does not exist.");
        }
        File fichier = new File("nom/" + nom_de_table);
        FileReader fr = new FileReader(fichier);
        BufferedReader br = new BufferedReader(fr);
        String attribut = br.readLine();
        return attribut;
    }
    public String decrire_table(String requete) throws Exception {
        String[] mots = requete.split(" ");
        String give = mots[0];
        if(give.compareToIgnoreCase("give") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1503. Use 'GIVE',");
        }
        String info = mots[1];
        if(info.compareToIgnoreCase("infos") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1450. Use 'INFOS',");
        }
        String about = mots[2]; 
        if(about.compareToIgnoreCase("about") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-9450. Use 'ABOUT',");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-5450. Please give table's name.");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-2400. Too many arguments to describe a table.");
        }
        String nom_de_table = mots[3];
        String infos = null;
        try {
            infos = this.infos_table(nom_de_table);
        } catch(Exception e) {
            throw e;
        }
        String rep = "";
        String[] colonnes = infos.split("_");
        String[] valeur = null;
        rep += ("The table '" + nom_de_table.toUpperCase() + "' has " + colonnes.length + " columns.\n");
        rep += ("Column name\t\t\t\tType\n");
        rep += ("---------------------------------------------------------\n");
        for (int i = 0; i < colonnes.length; i++) {
            valeur = colonnes[i].split("/");
            rep += (valeur[0] + "\t\t\t\t|\t" + valeur[1] + "\n");
        }
        return rep;
    }


    public boolean chaine_de_nombre(String str) {
        char[] chaine = str.toCharArray();
        for (int i = 0; i < chaine.length; i++) {
            if(Character.isDigit(chaine[i]) == false) {
                return false;
            }
        }
        return true;
    }
    public boolean chaine_date(String str) {
        String[] date = str.split("-");
        if(date.length != 3) {
            return false;
        }
        for (int i = 0; i < date.length; i++) {
            if(this.chaine_de_nombre(date[i]) == false) {
                return false;
            }
        }
        return true;
    }
    public void match_valeur_colonne(String valeur, String type) throws Exception {
        if(type.compareToIgnoreCase("string") == 0) {
            if(this.contient_ponctuation(valeur) == true) {
                throw new Exception("SYNTAX ERROR: BDD-0365. '" + valeur + "' is not a string");
            }
        } else if(type.compareToIgnoreCase("number") == 0) {
            if(this.chaine_de_nombre(valeur) == false) {
                throw new Exception("SYNTAX ERROR: BDD-0366. '" + valeur + "' is not a number");
            }
        } else if(type.compareToIgnoreCase("date") == 0) {
            if(this.chaine_date(valeur) == false) {
                throw new Exception("SYNTAX ERROR: BDD-0367. '" + valeur + "' is not a date");
            }
        }
    }
    public String inserer_object(String requete) throws Exception {
        String[] mots = requete.split(" ");
        String introduce = mots[0];
        if(introduce.compareToIgnoreCase("introduce") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1123. Use 'INTRODUCE'.");
        }
        String to = mots[1];
        if(to.compareToIgnoreCase("to") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1124. Use 'TO'.");
        }
        String nom_table = mots[2];
        if(this.nom_disponible(nom_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_table + "' does no exist.");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-9560. Please give the values to insert.");
        }
        if(mots.length > 4) {
            throw new Exception("SYNTAX ERROR: BDD-9561. Too many arguments in order to insert.");
        }

        String infos = null;
        try {
            infos = this.infos_table(nom_table);
        } catch(Exception e) {
            throw e;
        }
        String value = mots[3];
        String[] values = value.split("_");
        String[] column = infos.split("_");
        String[] type = new String[column.length];
        if(column.length < values.length) {
            throw new Exception("SYNTAX ERROR: BDD-9562. Too many arguments.");
        } else if(column.length > values.length) {
            throw new Exception("SYNTAX ERROR: BDD-9563. Less number of arguments.");
        }
        for (int i = 0; i < type.length; i++) {
            type[i] = column[i].split("/")[1];
            try {
                this.match_valeur_colonne(values[i], type[i]);
            } catch(Exception e) {
                throw e;
            }
        }
        
        File fichier = new File("donnee/" + nom_table.toLowerCase());
        FileWriter writer = new FileWriter(fichier, true);
        writer.write(value + ";");
        writer.close();
        return("1 row added successfully");
    }


    public String lire_donnee(String nom_de_table) throws Exception {
        nom_de_table = nom_de_table.toLowerCase();
        File fichier = new File("donnee/" + nom_de_table);
        FileReader reader = new FileReader(fichier);
        BufferedReader br = new BufferedReader(reader);

        return br.readLine();
    }
    public int ind_colonne_cible(String nom_table, String nom_colonne) throws Exception {
        nom_colonne = nom_colonne.toLowerCase();
        int rep = -1;
        try {
            String infos = this.infos_table(nom_table);
            String[] colonne = infos.split("_");
            for (int i = 0; i < colonne.length; i++) {
                if(nom_colonne.compareToIgnoreCase(colonne[i].split("/")[0]) == 0) {
                    rep = i;
                }
            }
        } catch(Exception e) {}
        if(rep == -1) {
            throw new Exception("SYNTAX ERROR: BDD-1500. The column '" + nom_colonne + "' does not exist.");
        }
        return rep;
    }
    public String type_cible(String nom_table, int indice) {
        String rep = null;
        try {
            String infos = this.infos_table(nom_table);
            String[] colonne = infos.split("_");
            rep = colonne[indice].split("/")[1];
        } catch(Exception e) {}
        return rep;
    }
    public Object conversion_object(String valeur, String type) {
        Object rep = null;
        if(type.compareToIgnoreCase("string") == 0) {
            rep = valeur;
        } else if(type.compareToIgnoreCase("number") == 0) {
            rep = new Nombre(valeur);
        } else if(type.compareToIgnoreCase("date") == 0) {
            rep = new MyDate(valeur);
        }
        return rep;
    }
    public Relation selectionner(String requete) throws Exception {
        Relation rep = new Relation();
        String[] mots = requete.split(" ");

        String take = mots[0];
        if(take.compareToIgnoreCase("take") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1000. Use 'TAKE' to select values.");
        }
        String all = mots[1];
        if(all.compareToIgnoreCase("all") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1001. Use 'ALL' to select values.");
        }
        
        if(mots.length < 3) {
            throw new Exception("SYNTAX ERROR: BDD-1002. Please give table's name.");
        }
        String nom_table = mots[2];
        if(this.nom_disponible(nom_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_table + "' does no exist.");
        }
        rep.setTable(nom_table);

        String infos = null;
        try {
            infos = this.infos_table(nom_table);
        } catch(Exception e) {
            throw e;
        }
        String[] colonne = infos.split("_");
        String[] type = new String[colonne.length];
        String[] nom = new String[colonne.length];
        for (int i = 0; i < nom.length; i++) {
            nom[i] = colonne[i].split("/")[0];
            type[i] = colonne[i].split("/")[1];
        }
        rep.setColonnes(nom);

        String donnee = this.lire_donnee(nom_table);
        String[] objets = donnee.split(";");
        Object[][] object = new Object[objets.length][colonne.length];
        for (int i = 0; i < object.length; i++) {
            for (int j = 0; j < colonne.length; j++) {
                object[i][j] = this.conversion_object(donnee.split(";")[i].split("_")[j], type[j]);
            }
        }
        
        String condition = null;
        if(mots.length > 3) {
            condition = mots[3];
            String[] temp_cond = condition.split("_");
            for (int x = 0; x < temp_cond.length; x++) {            
                ArrayList<Object[]> array = new ArrayList<Object[]>();
                String col = temp_cond[x].split(":")[0];
                String value = temp_cond[x].split(":")[1]; 
                int indice = this.ind_colonne_cible(nom_table, col);
                String t = this.type_cible(nom_table, indice);
                this.match_valeur_colonne(value, t);
                Object objet = null;
                if(t.compareToIgnoreCase("string") == 0) {objet = value;}
                if(t.compareToIgnoreCase("number") == 0) {objet = new Nombre(value);};
                if(t.compareToIgnoreCase("date") == 0)   {objet = new MyDate(value);};
                
                for (int i = 0; i < object.length; i++) {
                    if(t.compareToIgnoreCase("string") == 0) {
                        if(String.valueOf(object[i][indice]).compareTo(String.valueOf(objet)) == 0) {
                            array.add(object[i]);
                        }
                    }
                    if((t.compareToIgnoreCase("number") == 0)) {
                        Nombre n1 = new Nombre(String.valueOf(object[i][indice]));
                        Nombre n2 = new Nombre(String.valueOf(objet));
                        if(n1.nombre_egaux(n2)) {
                            array.add(object[i]);
                        }
                    }
                    if((t.compareToIgnoreCase("date") == 0)) {
                        MyDate d1 = new MyDate(String.valueOf(object[i][indice]));
                        MyDate d2 = new MyDate(String.valueOf(objet));
                        if(d1.date_egales(d2)) {
                            array.add(object[i]);
                        }
                    }
                }
                object = new Object[array.size()][colonne.length];
                for (int i = 0; i < object.length; i++) {
                    for (int j = 0; j < object[i].length; j++) {
                        object[i][j] = array.get(i)[j];
                    }
                }
            }
        }
        rep.setValeur(object);
        return rep;
    }


    public ArrayList<String> colonnes_communes(String r1, String r2) {
        ArrayList<String> list = new ArrayList<String>();
        String info1 = null;
        String info2 = null;

        try {
            info1 = this.infos_table(r1);
            info2 = this.infos_table(r2);
        } catch(Exception e) {}

        String[] attributs1 = info1.split("_");
        String[] attributs2 = info2.split("_");

        for (int i = 0; i < attributs1.length; i++) {
            for (int j = 0; j < attributs2.length; j++) {
                if(attributs1[i].compareTo(attributs2[j]) == 0) 
                    list.add(attributs1[i]);
            }
        }

        return list;
    }
    public int[] les_colonnes_cibles(String nom_de_table, ArrayList<String> nom_colonnes) {
        String infos = null;
        try {
            infos = this.infos_table(nom_de_table);
        } catch(Exception e) {}

        int[] rep = new int[nom_colonnes.size()];
        String[] col = infos.split("_");
        int ajout = 0;
        for (int i = 0; i < col.length; i++) {
            for (int j = 0; j < nom_colonnes.size(); j++) {
                if(col[i].compareTo(nom_colonnes.get(j)) == 0) {
                    rep[ajout] = i;
                    ajout ++;
                }
            }
        }
        return rep;
    }
    public Relation difference(String requete) throws Exception {
        Relation rep = new Relation();
        String[] mots = requete.split(" ");
        String give = mots[0];
        if(give.compareToIgnoreCase("give") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-3890. Use 'GIVE'.");
        }
        String nom_grand_table = mots[1];
        if(this.nom_disponible(nom_grand_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_grand_table + "' does not exist.");
        }
        String less = mots[2];
        if(less.compareToIgnoreCase("less") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-3891. Use 'LESS'.");
        }
        String nom_petit_table = mots[3];
        if(this.nom_disponible(nom_petit_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_petit_table + "' does not exist.");
        }

        ArrayList<String> commun = this.colonnes_communes(nom_grand_table, nom_petit_table);
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        Relation grande_relation = this.selectionner("take all " + nom_grand_table);
        Relation petite_relation = this.selectionner("take all " + nom_petit_table);
        int[] ind_1 = this.les_colonnes_cibles(nom_grand_table, commun);
        int[] ind_2 = this.les_colonnes_cibles(nom_petit_table, commun);

        for (int i = 0; i < grande_relation.getValeur().length; i++) {
            int[] count = new int[petite_relation.getValeur().length];
            for (int j = 0; j < petite_relation.getValeur().length; j++) {
                count[j] = 0;
                for (int k = 0; k < ind_2.length; k++) {
                    String value1 = String.valueOf(grande_relation.getValeur()[i][ind_1[k]]);
                    String value2 = String.valueOf(petite_relation.getValeur()[j][ind_2[k]]);
                    if(value1.compareTo(value2) == 0) {count[j] ++;}
                }
            }
            int n = 0;
            for (int j = 0; j < count.length; j++) {
                if(count[i] == ind_1.length) {n ++;}
            }
            if(n == 0) 
                list.add(grande_relation.getValeur()[i]);
        }
        String table = nom_grand_table.toUpperCase() + " less " + nom_petit_table.toUpperCase();
        rep.setTable(table);

        Object[][] valeur = new Object[list.size()][];
        for (int i = 0; i < valeur.length; i++) {
            valeur[i] = list.get(i);
        }
        rep.setValeur(valeur);

        String infos = this.infos_table(nom_grand_table);
        String[] attr = infos.split("_");
        String[] nom_attr = new String[attr.length];
        for (int i = 0; i < nom_attr.length; i++) {
            nom_attr[i] = attr[i].split("/")[0];
        }
        rep.setColonnes(nom_attr);

        return rep;
    }



    public boolean contient_colonne(String nom_de_table, String nom_de_colonne) {
        String infos = null;
        try {
            infos = this.infos_table(nom_de_table);
        } catch(Exception e) {}
        String[] details = infos.split("_");
        for (int i = 0; i < details.length; i++) {
            if(details[i].split("/")[0].compareToIgnoreCase(nom_de_colonne) == 0) 
                return true;
        }
        return false;
    }
    public String type_colonne(String nom_de_table, String nom_colonne) throws Exception {
        String infos = null;
        try {
            infos = this.infos_table(nom_de_table);
        } catch(Exception e) {}
        if(this.contient_colonne(nom_de_table, nom_colonne) == false) {
            throw new Exception("SYNTAX ERROR: BDD-2265. The table '" + nom_de_table + "' does not have column '" + nom_colonne + "'.");
        }
        String rep = null;
        String[] details = infos.split("_");
        for (int i = 0; i < details.length; i++) {
            if(details[i].split("/")[0].compareToIgnoreCase(nom_colonne) == 0) 
                rep = details[i].split("/")[1];
        }
        return rep;
    }
    public Object[] concatObjectArray(Object[] obj_1, Object[] obj_2) {
        Object[] rep = new Object[obj_1.length + obj_2.length];
        for (int i = 0; i < obj_1.length; i++) {
            rep[i] = obj_1[i];
        }
        int index = 0;
        for (int i = obj_1.length; i < rep.length; i++) {
            rep[i] = obj_2[index];
            index ++;
        }
        return rep;
    }
    public String[] concatStringArray(String[] obj_1, String[] obj_2) {
        String[] rep = new String[obj_1.length + obj_2.length];
        for (int i = 0; i < obj_1.length; i++) {
            rep[i] = obj_1[i];
        }
        int index = 0;
        for (int i = obj_1.length; i < rep.length; i++) {
            rep[i] = obj_2[index];
            index ++;
        }
        return rep;
    }
    public Relation jointure(String requete) throws Exception {
        Relation reponse = new Relation();

        String[] mots = requete.split(" ");
        String connect = mots[0];
        if(connect.compareToIgnoreCase("Connect") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-4000. Use 'CONNECT'.");
        }
        String relation_1 = mots[1];
        if(this.nom_disponible(relation_1) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_1 + "' does not exist.");
        }
        String with = mots[2];
        if(with.compareToIgnoreCase("with") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-4000. Use 'WITH'.");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-2000. Please give the table to connect with '" + relation_1 + "'.");
        }
        String relation_2 = mots[3];
        if(this.nom_disponible(relation_2) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_2 + "' does not exist.");
        }
        if(mots.length < 5) {
            throw new Exception("SYNTAX ERROR: BDD-4200. Use 'WHERE'.");
        }
        String where = mots[4];
        if(where.compareToIgnoreCase("where") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-4200. Use 'WHERE'.");
        } if(mots.length < 6) {
            throw new Exception("SYNTAX ERROR: BDD-1000. Please condition of the connection.");
        }
        String condition = mots[5];
        String[] champs = condition.split("_");
        String type_1 = null, type_2 = null;
        try {
            type_1 = this.type_colonne(relation_1, champs[0]);
            type_2 = this.type_colonne(relation_2, champs[1]);
        } catch(Exception e) {
            throw e;
        }
        if(type_1.compareToIgnoreCase(type_2) != 0) {
            throw new Exception("SYNTAX ERROR: BDD_5020. Type " + type_1 + " is not compatible with type " + type_2 + ".");
        }

        reponse.setTable("Connection between " + relation_1 + " and " + relation_2 + ".");

        Relation table_1 = this.selectionner("take all " + relation_1);
        Relation table_2 = this.selectionner("take all " + relation_2);
        String[] colonne_reponse = this.concatStringArray(table_1.getColonnes(), table_2.getColonnes());
        reponse.setColonnes(colonne_reponse);

        int indice_1 = this.ind_colonne_cible(relation_1, champs[0]);
        int indice_2 = this.ind_colonne_cible(relation_2, champs[1]);
        ArrayList<Object[]> objects = new ArrayList<Object[]>();
        for (int i = 0; i < table_1.getValeur().length; i++) {
            for (int j = 0; j < table_2.getValeur().length; j++) {
                if(type_1.compareToIgnoreCase("string") == 0) {
                    String valeur_1 = String.valueOf(table_1.getValeur()[i][indice_1]);
                    String valeur_2 = String.valueOf(table_2.getValeur()[j][indice_2]);
                    if(valeur_1.compareTo(valeur_2) == 0) 
                        objects.add(this.concatObjectArray(table_1.getValeur()[i], table_2.getValeur()[j]));
                } else if(type_1.compareToIgnoreCase("number") == 0) {
                    Nombre valeur_1 = (Nombre)(table_1.getValeur()[i][indice_1]);
                    Nombre valeur_2 = (Nombre)(table_2.getValeur()[j][indice_2]);
                    if(valeur_1.nombre_egaux(valeur_2) == true) 
                        objects.add(this.concatObjectArray(table_1.getValeur()[i], table_2.getValeur()[j]));
                } else if(type_1.compareToIgnoreCase("date") == 0) {
                    MyDate valeur_1 = (MyDate)(table_1.getValeur()[i][indice_1]);
                    MyDate valeur_2 = (MyDate)(table_2.getValeur()[j][indice_2]);
                    if(valeur_1.date_egales(valeur_2) == true) 
                        objects.add(this.concatObjectArray(table_1.getValeur()[i], table_2.getValeur()[j]));
                }
            }
        }
        Object[][] valeur = new Object[objects.size()][];
        for (int i = 0; i < valeur.length; i++) {
            valeur[i] = objects.get(i);
        }
        reponse.setValeur(valeur);

        return reponse;
    }



    public Relation produit_cartesien(String requete) throws Exception {
        Relation rep = new Relation();
        String[] mots = requete.split(" ");

        String give = mots[0];
        if(give.compareToIgnoreCase("give") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-3890. Use 'GIVE'.");
        }
        String product = mots[1];
        if(product.compareToIgnoreCase("product") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1966. Use 'PRODUCT'.");
        }
        String between = mots[2];
        if(between.compareToIgnoreCase("between") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1960. Use 'BETWEEN'.");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-1990. Give the tables you want to have product.");
        }
        String relation_1 = mots[3];
        if(this.nom_disponible(relation_1) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_1 + "' does not exist.");
        }
        if(mots.length < 5) {
            throw new Exception("SYNTAX ERROR: BDD-1989. Use 'AND' and give the second name of the table.");
        }
        String and = mots[4];
        if(and.compareToIgnoreCase("and") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-1958. USE 'AND'.");
        }
        if(mots.length < 6) {
            throw new Exception("SYNTAX ERROR: BDD-1010. Give the second name of the table.");
        }
        String relation_2 = mots[5];
        if(this.nom_disponible(relation_2) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_2 + "' does not exist.");
        }

        String titre = "PRODUCT BETWEEN " + relation_1.toUpperCase() + " AND " + relation_2.toUpperCase() + ".";
        rep.setTable(titre);

        Relation table_1 = this.selectionner("take all " + relation_1);
        Relation table_2 = this.selectionner("take all " + relation_2);
        String[] colonne = this.concatStringArray(table_1.getColonnes(), table_2.getColonnes());
        rep.setColonnes(colonne);

        ArrayList<Object[]> list = new ArrayList<Object[]>();
        for (int i = 0; i < table_1.getValeur().length; i++) {
            for (int j = 0; j < table_2.getValeur().length; j++) 
                list.add(this.concatObjectArray(table_1.getValeur()[i], table_2.getValeur()[j]));
        }
        Object[][] valeur = new Object[list.size()][];
        for (int i = 0; i < valeur.length; i++) {
            valeur[i] = list.get(i);
        }
        rep.setValeur(valeur);

        return rep;
    }



    public Relation projection(String requete) throws Exception {
        Relation rep = new Relation();
        String[] mots = requete.split(" ");

        String project = mots[0];
        if(project.compareToIgnoreCase("project") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-0202. Use 'PROJECT'.");
        }
        String nom_table = mots[1];
        if(this.nom_disponible(nom_table) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + nom_table + "' does not exist.");
        }
        if(mots.length < 3) {
            throw new Exception("SYNTAX ERROR: BDD-0203. Please give the columns.");
        }
        String condition = mots[2];
        String[] colonnes = condition.split("_");
        int[] index = new int[colonnes.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = this.ind_colonne_cible(nom_table, colonnes[i]);
        }

        Relation select = this.selectionner("take all " + nom_table);
        Object[][] obj = new Object[select.getValeur().length][index.length];
        for (int i = 0; i < select.getValeur().length; i++) {
            int ind = 0;
            for (int j = 0; j < obj[i].length; j++) {
                obj[i][j] = select.getValeur()[i][index[ind]];
                ind ++;
            }
            ind = 0;
        }

        String titre = "Projection of " + nom_table + " on ";
        for (int i = 0; i < colonnes.length; i++) {
            titre += colonnes[i];
            if(i != colonnes.length - 1)        titre += " and ";
        }
        titre += ".";
        rep.setTable(titre);
        rep.setColonnes(colonnes);
        rep.setValeur(obj);
        return rep;
    }


    public int caractere_diff(String s1, String s2) {
        int count = 0;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        if(c1.length != c2.length) {
            return 20;
        }
        for (int i = 0; i < c2.length; i++) {
            if(c1[i] != c2[i])
                count ++;
        }
        return count;
    }
    public String valeur_objet(Object[] object) {
        String rep = "";
        for (int i = 0; i < object.length; i++) {
            rep += String.valueOf(object[i]);
            if(i != object.length - 1) 
                rep += "_";
        }
        return rep;
    }
    public Relation division(String requete) throws Exception {
        Relation reponse = new Relation();
        String[] mots = requete.split(" ");
        String relation_1 = mots[1];
        if(this.nom_disponible(relation_1) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_1 + "' does not exist.");
        }
        if(mots.length < 3) {
            throw new Exception("SYNTAX ERROR: BDD-1236. Use 'WITH' and give the second table.");
        }
        String with = mots[2];
        if(with.compareToIgnoreCase("with") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-2022. Use 'WITH");
        }
        if(mots.length < 4) {
            throw new Exception("SYNTAX ERROR: BDD-4569. Give the second table.");
        }
        String relation_2 = mots[3];
        if(this.nom_disponible(relation_2) == true) {
            throw new Exception("SYNTAX ERROR: BDD-0102. The table '" + relation_2 + "' does not exist.");
        }

        if(this.infos_table(relation_2).split("_").length > 1) {
            throw new Exception("SYNTAX ERROR: BDD-1120. The table " + relation_2 + " has " + this.infos_table(relation_2).split("_").length + " columns.");
        }
        if(this.infos_table(relation_2).split("/")[1].compareToIgnoreCase("number") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-7895. The column type must be number in " + relation_2);
        }
        if(this.type_cible(relation_1, this.ind_colonne_cible(relation_1, this.infos_table(relation_2).split("/")[0])).compareToIgnoreCase("number") != 0) {
            throw new Exception("SYNTAX ERROR: BDD-7895. The column type must be number in " + relation_1);
        }
        int index = this.ind_colonne_cible(relation_1, this.infos_table(relation_2).split("/")[0]);

        Relation grand = this.selectionner("take all " + relation_1);
        Relation petit = this.selectionner("take all " + relation_2);
        ArrayList<String> listString = new ArrayList<String>();
        for (int i = 0; i < grand.getValeur().length; i++) {
            for (int j = 0; j < petit.getValeur().length; j++) {
                Nombre nb1 = (Nombre)grand.getValeur()[i][index];
                Nombre nb2 = (Nombre)petit.getValeur()[j][0];
                if(nb1.nombre_egaux(nb2)) {
                    listString.add(this.valeur_objet(grand.getValeur()[i]));
                }
            }
        }

        int[] count = new int[listString.size()];
        ArrayList<String> rep = new ArrayList<String>();
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
            for (int j = 0; j < count.length; j++) {
                if(this.caractere_diff(listString.get(i), listString.get(j)) <= 1) 
                    count[i] ++;
            }
            if(count[i] == petit.getValeur().length) 
                rep.add(listString.get(i));
        }

        Relation rel = this.selectionner("take all " + relation_1);
        reponse.setTable(relation_1 + " divided by " + relation_2);
        reponse.setColonnes(rel.getColonnes());
        Object[][] valeur = new Object[rep.size()][rel.getValeur()[0].length];
        for (int i = 0; i < valeur.length; i++) {
            for (int j = 0; j < valeur[i].length; j++) {
                valeur[i][j] = rep.get(i).split("_")[j]; 
            }
        }
        reponse.setValeur(valeur);

        return reponse;
    }
}