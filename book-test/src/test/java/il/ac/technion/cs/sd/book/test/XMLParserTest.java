package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.library.XMLParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedMap;


public class XMLParserTest extends SdHw1Test{

  private static String getXMLStringFromFile(String fileName) throws FileNotFoundException {
    return new Scanner(new File(XMLParserTest.class.getResource(fileName).getFile())).useDelimiter("\\Z").next();
  }

  @Test
  public void testSimple2() throws Exception {
    System.out.println("XPATH says:");
    String xml = getXMLStringFromFile("xmlParserTest.xml");
    SortedMap sortedMaps = XMLParser.parseXMLToSortedMap(xml, "/Root/Reviewer/Review",true);
    System.out.println(sortedMaps);
  }
}
