import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
public class ReadXMLFile
{
    public static void main(String argv[])
    {
        try
        {
//creating a constructor of file class and parsing an XML file  
            File file = new File("C:\\Users\\MartheSpriet\\OneDrive - KU Leuven\\Documents\\Universiteit\\B3\\Tweede semester\\Besturingssystemen2\\XML\\processen10000.xml");
//an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("process");
// nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                System.out.println("\nProcess Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    System.out.println("pid: "+ eElement.getElementsByTagName("pid").item(0).getTextContent());
                    System.out.println("arrival time: "+ eElement.getElementsByTagName("arrivaltime").item(0).getTextContent());
                    System.out.println("servicetime: "+ eElement.getElementsByTagName("servicetime").item(0).getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
} 