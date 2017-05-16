package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.app.XMLParser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedMap;


public class XMLParserTest {

  @Rule public Timeout globalTimeout = Timeout.seconds(30);

  private static String getXMLStringFromFile(String fileName) throws FileNotFoundException {
    return new Scanner(new File(XMLParserTest.class.getResource(fileName).getFile())).useDelimiter("\\Z").next();
  }

  @Test
  public void testSimple() throws Exception {
    String xml = getXMLStringFromFile("xmlParserTest.xml");
    SortedMap[] sortedMaps = XMLParser.parseXMLToSortedMap(xml);
    System.out.println(sortedMaps[0]);
    System.out.println(sortedMaps[1]);
  }
}
