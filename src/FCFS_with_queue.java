//import jdk.internal.icu.text.UnicodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FCFS_with_queue {

    public static void main(String[] argv) {
        int aantalProcessen = 0;
        Queue<Integer> readyQueue = null;
        List<Double> aankomsttijd = null;
        List<Double> bedieningstijd = null;

        List<Double> starttijd = new ArrayList<>();
        List<Double> eindtijd = new ArrayList<>();
        List<Double> wachttijd = new ArrayList<>();
        List<Double> omlooptijd = new ArrayList<>();
        List<Double> genormaliseerdeOmlooptijd = new ArrayList<>();

        double gemiddeldeOmlooptijd;
        double gemiddeldeGenormaliseerdeOmlooptijd;
        double gemiddeldeWachttijd;

        try {
//creating a constructor of file class and parsing an XML file
            File file = new File("voorbeeld.xml");
//an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("process");
// nodeList is not iterable, so we are using for loop
            readyQueue = new LinkedList<>();
            aankomsttijd = new ArrayList<>();
            bedieningstijd = new ArrayList<>();
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    readyQueue.add(Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent()));
                    aankomsttijd.add(Double.parseDouble(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent()));
                    bedieningstijd.add(Double.parseDouble(eElement.getElementsByTagName("servicetime").item(0).getTextContent()));
                    aantalProcessen++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

// proces parameters berekend
        int aantalJiffys = 0;
        for (int i=0; i<aantalProcessen; i++) {
            int proces = readyQueue.remove();
            starttijd.add(proces-1, (double) aantalJiffys);
            eindtijd.add(proces-1, aantalJiffys+bedieningstijd.get(proces-1));
            wachttijd.add(proces-1, aantalJiffys-aankomsttijd.get(proces-1));
            omlooptijd.add(proces-1, wachttijd.get(proces-1) + bedieningstijd.get(proces-1));
            genormaliseerdeOmlooptijd.add(proces-1, omlooptijd.get(proces-1)/bedieningstijd.get(proces-1));

            aantalJiffys += bedieningstijd.get(proces-1);
        }


// proces parameters uitgeprint
        System.out.println("PROCES PARAMETERS");
        for (int i=0; i<aantalProcessen; i++) {
            System.out.println("Proces " + (i+1) + ":");
            System.out.println("arrival: " + aankomsttijd.get(i));
            System.out.println("service: " + bedieningstijd.get(i));
            System.out.println("start: " + starttijd.get(i));
            System.out.println("eind: " + eindtijd.get(i));
            System.out.println("wachttijd: " + wachttijd.get(i));
            System.out.println("omlooptijd: " + omlooptijd.get(i));
            System.out.println("genormaliseerde omlooptijd: " + genormaliseerdeOmlooptijd.get(i));

            System.out.println();
        }

// globale parameters berekend
        int somOmlooptijd = 0;
        int somGenormaliseerdeOmlooptijd = 0;
        int somWachttijd = 0;
        for (int i=0; i<aantalProcessen; i++) {
            somOmlooptijd += omlooptijd.get(i);
            somGenormaliseerdeOmlooptijd += genormaliseerdeOmlooptijd.get(i);
            somWachttijd += wachttijd.get(i);
        }
        gemiddeldeOmlooptijd = somOmlooptijd/(double)aantalProcessen;
        gemiddeldeGenormaliseerdeOmlooptijd = somGenormaliseerdeOmlooptijd/(double)aantalProcessen;
        gemiddeldeWachttijd = somWachttijd/(double)aantalProcessen;


// globale parameters uitgeprint
        System.out.println("GLOBALE PARAMETERS");
        System.out.println("gemiddelde omlooptijd: " + gemiddeldeOmlooptijd);
        System.out.println("gemiddelde genormaliseerde omlooptijd: " + gemiddeldeGenormaliseerdeOmlooptijd);
        System.out.println("gemiddelde wachttijd: " + gemiddeldeWachttijd);

    }
}
