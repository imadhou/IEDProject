import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestClient {

    private static final String REST_URI
            = "http://www.omdbapi.com/?t=%s&apikey=7de4e25b&r=xml";

    public String getDescriptionByTitle(String title) {
            //Create connection
        NodeList nodeList = XPath(String.format(REST_URI, title), "/root/movie/@plot", XPathConstants.NODESET);
        return nodeList.item(0).getNodeValue();
    }


    private static NodeList XPath(String uri, String requete, QName typeRetour){
        //Le dernier paramètre indique le type de résultat souhaité
        //XPathConstants.STRING: chaîne de caractères (String)
        //XPathConstants.NODESET: ensemble de noeuds DOM (NodeList)
        //XPathConstants.NODE: noeud DOM (Node) - le premier de la liste
        //XPathConstants.BOOLEAN: booléen (Boolean) - vrai si la liste n'est pas vide
        //XPathConstants.NUMBER: numérique (Double) - le contenu du noeud sélectionné transformé en Double

        try{
            //Transformation en document DOM du contenu XML
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            DocumentBuilder parseur = fabrique.newDocumentBuilder();
            Document document = parseur.parse(uri);

            //création de l'objet XPath
            XPathFactory xfabrique = XPathFactory.newInstance();
            XPath xpath = xfabrique.newXPath();

            //évaluation de l'expression XPath
            XPathExpression exp = xpath.compile(requete);
            NodeList nodeList = (NodeList) exp.evaluate(document, typeRetour);
            return nodeList;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
