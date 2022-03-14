import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
public class ReadXMLFile {
    public static void readingXMLFile(int aantalProcessen, int[] pid, int[] arrivaltime, int[] servicetime){
        int p = 0;
        File file = null;
        try {
//creating a constructor of file class and parsing an XML file
            switch (aantalProcessen) {
                case 5 -> file = new File("voorbeeld.xml");
                case 10000 -> file = new File("processen10000.xml");
                case 20000 -> file = new File("processen20000.xml");
                case 50000 -> file = new File("processen50000.xml");
                default -> {
                }
            }
//an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("process");
// nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    p++;
                    pid[p-1] = Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent());
                    arrivaltime[p-1] = Integer.parseInt(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent());
                    servicetime[p-1] = Integer.parseInt(eElement.getElementsByTagName("servicetime").item(0).getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}