package il.ac.technion.cs.sd.book.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.SortedMap;
import java.util.TreeMap;

public class XMLParser {

    public static SortedMap<String,Review>[] parseXMLToSortedMap(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new Exception("OMGS LOL WTF IS THIS EXCEPTION TROLOLOL");
        }
        InputSource is = new InputSource(new StringReader(xml));
        Document document = null;
        try {
            document = builder.parse(is);
        } catch (Exception e) {
            throw new Exception("OMGS LOL WTF IS THIS EXCEPTION TROLOLOL");
        }

        NodeList reviewerList = document.getDocumentElement().getChildNodes();

        SortedMap<String, Review> sortedReviewMapByReviewer = new TreeMap<>();
        SortedMap<String, Review> sortedReviewMapByBook = new TreeMap<>();

        for (int i = 0; i < reviewerList.getLength(); ++i) {
            Node reviewerNode = reviewerList.item(i);
            if (reviewerNode.getNodeType() == Node.ELEMENT_NODE) {
                String reviewerId = reviewerNode.getAttributes().getNamedItem("Id").getTextContent();
                NodeList reviewList = reviewerNode.getChildNodes();
                for (int j = 0; j < reviewList.getLength(); ++j) {
                    Node reviewNode = reviewList.item(j);
                    if (reviewNode.getNodeType() == Node.ELEMENT_NODE) {
                        String bookId = reviewNode.getChildNodes().item(1).getTextContent();
                        String bookScore = reviewNode.getChildNodes().item(3).getTextContent();
                        sortedReviewMapByReviewer.put(reviewerId + "," +  bookId, new Review(reviewerId, bookId, bookScore));
                        sortedReviewMapByBook.put(bookId + "," + reviewerId, new Review(reviewerId, bookId, bookScore));
                    }
                }
            }
        }

        return new SortedMap[] {sortedReviewMapByReviewer, sortedReviewMapByBook};
    }
}
