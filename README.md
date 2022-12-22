# socket_sgbd
avant d'utiliser, creer les dossiers nom et donnee
tsy azo asiana diso ilay anarana dossier

ny fonctionnalites ao anatiny zany dia:
  select                    take all nom_table (condition)
                            ex: take all hello nom:jean
                            
  project                   project nom_table nom_colonne
                            ex: project hello nom
  
  insert                    introduce to nom_table col1_col2_col3...
                            ex: introduce to hello jean_56
                            
  create table              generate chart nom_table nomcol1/type_nomcol2/type_nomcol3/type...  
                            ex: generate chart hello nom/string_age/number
                            
  join                      connect table_1 with table_2 where col1_col2
                            ex: connect hello with bonjour where nom_prenom
                            
  describe                  give more infos about nom_table
                            ex: give more infos about hello
