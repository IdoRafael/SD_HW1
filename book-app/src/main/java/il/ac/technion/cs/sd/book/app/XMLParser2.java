package il.ac.technion.cs.sd.book.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class XMLParser2 {

    public static SortedMap<String,Review>[] parseXMLToSortedMap(String xml) throws Exception {

        Comparator<String> csvStringComparator = Comparator
                .comparing((String s) -> s.split(",")[0])
                .thenComparing((String s)-> s.split(",")[1]);

        SortedMap<String, Review> sortedReviewMapByReviewer = new TreeMap<>(csvStringComparator);
        SortedMap<String, Review> sortedReviewMapByBook = new TreeMap<>(csvStringComparator);


        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document document = builder.parse(is);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("/Root/Reviewer/Review");

            NodeList reviewList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < reviewList.getLength(); ++i) {
                Node reviewNode = reviewList.item(i);
                if (reviewNode.getNodeType() == Node.ELEMENT_NODE) {
                    String reviewerId = reviewNode.getParentNode().getAttributes().getNamedItem("Id").getTextContent();
                    String bookId = reviewNode.getChildNodes().item(1).getTextContent();
                    String bookScore = reviewNode.getChildNodes().item(3).getTextContent();
                    Review newReview = new Review(reviewerId, bookId, bookScore);

                    sortedReviewMapByReviewer.put(
                            String.join(",", reviewerId, bookId),
                            newReview
                    );
                    sortedReviewMapByBook.put(
                            String.join(",", bookId, reviewerId),
                            newReview
                    );
                }
        }

        } catch (Exception e) {
            throw new Exception("OMGS LOL WTF IS THIS EXCEPTION TROLOLOL");
        }

        return new SortedMap[] {sortedReviewMapByReviewer, sortedReviewMapByBook};
    }
}
