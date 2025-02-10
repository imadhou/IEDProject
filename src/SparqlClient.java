import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;

import java.util.*;

public class SparqlClient {

    private String getTitleQuery(String title){
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf:<http://xmlns.com/foaf/0.1/> \n" +
                "PREFIX dbp:<http://dbpedia.org/property/> \n" +
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" +
                "PREFIX dbo:<http://dbpedia.org/ontology/> \n" +
                "SELECT ?filmName ?directorName  ?actorName ?producerName WHERE {\n" +
                "                ?film a dbo:Film ;\n" +
                "                   dbp:name \"%s\"@en ;\n" +
                "                   dbp:name ?filmName;\n" +
                "                   dbo:director ?director ;\n" +
                "                   dbo:producer ?producer;\n" +
                "                   dbo:starring ?actor.\n" +
                "                ?director rdfs:label ?directorName.\n" +
                "                ?producer rdfs:label ?producerName.\n" +
                "                ?actor rdfs:label ?actorName.\n" +
                "                FILTER (langMatches(lang(?directorName),\"en\")).\n" +
                "                FILTER (langMatches(lang(?producerName),\"en\")).\n" +
                "                FILTER (langMatches(lang(?actorName),\"en\")).\n" +
                "                 }";
        return String.format(queryString, title);
    }
    private String getActorQuery(String actor){
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf:<http://xmlns.com/foaf/0.1/> \n" +
                "PREFIX dbp:<http://dbpedia.org/property/> \n" +
                "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \n" +
                "PREFIX dbo:<http://dbpedia.org/ontology/> \n" +
                "SELECT ?film ?filmName ?directorName ?producerName WHERE {\n" +
                " ?film a dbo:Film ;\n" +
                "   rdfs:label ?filmName;\n" +
                "   dbo:director ?director ;\n" +
                "   dbo:producer ?producer;\n" +
                "   dbo:starring ?a .\n" +
                "?a foaf:name \"%s\"@en .\n" +
                " ?director rdfs:label ?directorName.\n" +
                " ?producer rdfs:label ?producerName.\n" +
                " FILTER (langMatches(lang(?filmName),\"en\")).\n" +
                " FILTER (langMatches(lang(?directorName),\"en\")).\n" +
                " FILTER (langMatches(lang(?producerName),\"en\")).\n" +
                "}";
        return String.format(queryString, actor);
    }

    public HashMap<String, Set<String>> getFilmByTitle(String title) {
        String queryString = getTitleQuery(title);
        ResultSet resultSet = executeQuery(queryString);

        Set<String> actorsSet = new HashSet<>();
        Set<String> producersSet = new HashSet<>();
        Set<String> directorsSet = new HashSet<>();

        while (resultSet.hasNext()){
            QuerySolution solution = resultSet.next();

            String actor = solution.get("actorName").toString().split("@")[0];
            actorsSet.add(actor);

            String producer = solution.get("producerName").toString().split("@")[0];
            producersSet.add(producer);

            String director = solution.get("directorName").toString().split("@")[0];
            directorsSet.add(director);
        }

        HashMap<String, Set<String>> film = new HashMap<String, Set<String>>();
        film.put("actorName", actorsSet);
        film.put("producerName", actorsSet);
        film.put("directorName", actorsSet);

        return film;
    }
    public HashMap<String, HashMap<String, Set<String>>> getFilmsByActor(String actor) {
        String queryString = getActorQuery(actor);
        ResultSet resultSet = executeQuery(queryString);

        HashMap<String, HashMap<String, Set<String>>> films = new HashMap<>();

        while (resultSet.hasNext()){
            QuerySolution solution = resultSet.next();

            String producerName = solution.get("producerName").toString().split("@")[0];
            String directorName = solution.get("directorName").toString().split("@")[0];
            String filmName = solution.get("filmName").toString().split("@")[0];

            if(films.containsKey(filmName)){

                films.get(filmName).get("producerName").add(producerName);
                films.get(filmName).get("directorName").add(directorName);

            }else {

                Set<String> producers = new HashSet<>();
                producers.add(producerName);

                Set<String> directors = new HashSet<>();
                directors.add(directorName);

                HashMap<String, Set<String>> film = new HashMap<>();
                film.put("producerName", producers);
                film.put("directorName", directors);

                films.put(filmName, film);
            }
        }

        return films;
    }

    private ResultSet executeQuery(String queryString){

        QueryExecutionHTTP qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
        ResultSet results = qexec.execSelect();
        return results;
    }

}