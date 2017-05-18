package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.library.XMLParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class XMLParserTest extends SdHw1Test{

    private static SortedMap getSortedMapFromXML(String fileName, boolean swapKeys) throws FileNotFoundException {
        String xml = getFilesContent("./xmlParserTest/" + fileName);
        return XMLParser.parseXMLToSortedMap(xml, "/Root/Reviewer/Review",swapKeys);
    }

    @Test
    public void shouldIgnoreDuplicatesAndGetLast() throws Exception {
        SortedMap sortedMap = getSortedMapFromXML("duplicates.xml", false);

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
        SortedMap sortedMap = getSortedMapFromXML("small.xml", false);

        assertTrue(sortedMap.containsKey("0,A"));
        assertTrue(sortedMap.containsKey("1,A"));
        assertTrue(sortedMap.containsKey("1,B"));
        assertEquals(3, sortedMap.size());
        //thus, doesn't contain reviewer with id=8
    }

    @Test
    public void shouldNotIgnoreSeparate() throws Exception {
        SortedMap sortedMap = getSortedMapFromXML("separate.xml", false);

        assertTrue(sortedMap.containsKey("1,A"));
        assertTrue(sortedMap.containsKey("1,B"));
        assertEquals(2, sortedMap.size());
    }

    @Test
    public void shouldSort() throws Exception {
        SortedMap sortedMap = getSortedMapFromXML("sort.xml", false);
        String[] expected = {"0", "1", "2"};

        //thus sorted
        assertTrue(Arrays.equals(expected, sortedMap.values().toArray()));

        assertEquals("0", sortedMap.get("0,A"));
        assertEquals("1", sortedMap.get("1,A"));
        assertEquals("2", sortedMap.get("1,B"));

        assertEquals(3, sortedMap.size());
    }

    @Test
    public void shouldSwapKeys() throws Exception {
        SortedMap sortedMap = getSortedMapFromXML("sort.xml", true);
        String[] expected = {"0", "1", "2"};

        //thus sorted
        assertTrue(Arrays.equals(expected, sortedMap.values().toArray()));

        assertEquals("0", sortedMap.get("A,0"));
        assertEquals("1", sortedMap.get("A,1"));
        assertEquals("2", sortedMap.get("B,1"));

        assertEquals(3, sortedMap.size());
    }

}
