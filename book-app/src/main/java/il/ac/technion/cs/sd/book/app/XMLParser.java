package il.ac.technion.cs.sd.book.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.Map;

public class XMLParser {
    public static Map<String,String> parseXMLToMap(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = builder.parse(is);

        NodeList reviewerList = document.getFirstChild().getChildNodes();

        for (int i = 0; i < reviewerList.getLength(); ++i) {
            Node reviewerNode = reviewerList.item(i);
            if (reviewerNode.getNodeType() == Node.ELEMENT_NODE) {
                String reviewerId = reviewerNode.getAttributes().getNamedItem("Id").getTextContent();
                NodeList reviewList = reviewerNode.getChildNodes();
                for (int j = 0; j < reviewList.getLength(); ++j) {
                    Node reviewNode = reviewList.item(j);
                    if (reviewNode.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(reviewerId
                                + " " + reviewNode.getChildNodes().item(1).getTextContent()
                                + " " + reviewNode.getChildNodes().item(3).getTextContent());
                    }
                }
            }
        }
    }
}
