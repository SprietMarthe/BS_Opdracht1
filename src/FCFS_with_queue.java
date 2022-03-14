//import jdk.internal.icu.text.UnicodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FCFS_with_queue {

    public static void main(String argv[]) {
        int aantalProcessen = 0;
        Queue<Integer> readyQueue = null;
        List<Double> arrivaltimes = null;
        List<Double> servicetimes = null;

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
            File file = new File("C:\\Users\\bekem\\OneDrive - KU Leuven\\KULeuven\\3eBach\\2e_semester\\Besturingsystemen2\\processen\\voorbeeld.xml");
//an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("process");
// nodeList is not iterable, so we are using for loop
            readyQueue = new LinkedList<>();
            arrivaltimes = new ArrayList<Double>();
            servicetimes = new ArrayList<Double>();
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    readyQueue.add(Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent()));
                    arrivaltimes.add(Double.parseDouble(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent()));
                    servicetimes.add(Double.parseDouble(eElement.getElementsByTagName("servicetime").item(0).getTextContent()));
                    aantalProcessen++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

// process parameters berekend
        int aantalJiffys = 0;
        while (readyQueue.size() > 0) {
            int process = readyQueue.remove();
            starttijd.add(process-1, (double) aantalJiffys);
            eindtijd.add(process-1, aantalJiffys+servicetimes.get(process-1));
            wachttijd.add(process-1, aantalJiffys-arrivaltimes.get(process-1));
            omlooptijd.add(process-1, wachttijd.get(process-1) + servicetimes.get(process-1));
            genormaliseerdeOmlooptijd.add(process-1, omlooptijd.get(process-1)/servicetimes.get(process-1));

            aantalJiffys += servicetimes.get(process-1);
        }


// process parameters uitgeprint
        System.out.println("PROCESS PARAMETERS");
        for (int i=0; i<aantalProcessen; i++) {
            System.out.println("Process " + (i+1) + ":");
            System.out.println("arrival: " + arrivaltimes.get(i));
            System.out.println("service: " + servicetimes.get(i));
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