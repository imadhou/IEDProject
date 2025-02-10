import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class Mediator {


    public static void main(String args[]) throws Exception {

        RestClient restClient = new RestClient();
        JdbcClient jdbcClient = new JdbcClient();
        SparqlClient sparqlClient = new SparqlClient();

        System.out.println("Pour une requête sur un titre de film tapez 1:");
        System.out.println("Pour une requête sur un acteur tapez 2:");
        Scanner s_name = new Scanner(System.in);

        int choix = s_name.nextInt();

        if (choix == 1) {
            System.out.println("Filme : ");
            s_name = new Scanner(System.in);

            String film = s_name.nextLine().trim();

            ArrayList<HashMap<String, String>> films =  jdbcClient.getFilmsByTitle(film);

            for (HashMap<String, String> f: films){
                System.out.println("-------------------------------------------------");
                System.out.println("Titre: " + f.get("Titre"));
                String description = restClient.getDescriptionByTitle(f.get("Titre"));
                HashMap<String, Set<String>> sparql = sparqlClient.getFilmByTitle(f.get("Titre"));
                System.out.println("Date de sortie :" + f.get("Date"));
                System.out.println("Genre :" + f.get("Genre"));
                System.out.println("Distributeur :" + f.get("Theatrical_Distributor"));
                System.out.println("Budget :" + f.get("ProductionBudget"));
                System.out.println("Revenus USA :" + f.get("DomesticGross"));
                System.out.println("Revenus :" + f.get("WorldwideGross"));
                System.out.println("Réalisateurs :" + sparql.get("directorName"));
                System.out.println("Résumé :" + description);
                System.out.println("Acteurs :" + sparql.get("actorName"));
                System.out.println("-------------------------------------------------");

            }

        } else {
            s_name = new Scanner(System.in);
            System.out.println("Acteur : ");

            String actor = s_name.nextLine().trim();
            HashMap<String, HashMap<String, Set<String>>> sparqlresult = sparqlClient.getFilmsByActor(actor);
            for(String title: sparqlresult.keySet()){
                ArrayList<HashMap<String, String>> films =  jdbcClient.getFilmsByTitle(title);
                if(films.size() > 0){
                    HashMap<String, String> f =  jdbcClient.getFilmsByTitle(title).get(0);
                    System.out.println("-------------------------------------------------");
                    System.out.println("Titre: " + f.get("Titre"));
                    System.out.println("Date de sortie :" + f.get("Date"));
                    System.out.println("Genre :" + f.get("Genre"));
                    System.out.println("Distributeur :" + f.get("Theatrical_Distributor"));
                    System.out.println("Réalisateurs :" + sparqlresult.get(title).get("directorName"));
                    System.out.println("Acteurs :" + sparqlresult.get(title).get("directorName"));
                    System.out.println("-------------------------------------------------");
                }

            }

        }

    }
}
