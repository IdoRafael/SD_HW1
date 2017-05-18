package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.library.XMLParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class XMLParserTest extends SdHw1Test{

    private static SortedMap getSortedMapFromXML(String fileName) throws FileNotFoundException {
        String xml = getFilesContent("./xmlParserTest/" + fileName);
        return XMLParser.parseXMLToSortedMap(xml, "/Root/Reviewer/Review",false);
    }

    @Test
    public void shouldIgnoreDuplicatesAndGetLast() throws Exception {
        SortedMap sortedMap = getSortedMapFromXML("duplicates.xml");

        String reviewerAndBookId = String.join(",","1", "A");
        String score = "3";

        assertEquals(
                score,
                sortedMap.get(reviewerAndBookId)
        );
        assertEquals(1, sortedMap.size());
    }

    @Test
    public void shouldIgnoreEmptyReviewers() throws Exception {

    }

    @Test
    public void shouldSwapKeys() throws Exception {

    }

    @Test
    public void shouldSort() throws Exception {

    }


}
