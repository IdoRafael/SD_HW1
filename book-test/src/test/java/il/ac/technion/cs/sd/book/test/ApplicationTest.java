package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ApplicationTest extends SdHw1Test {

    private BookScoreReader setupAndGetReader(String fileName) throws Exception {
        String fileContents = getFilesContent(fileName);
        LineStorageTestImpl[] storage = {new LineStorageTestImpl(), new LineStorageTestImpl()};
        LineStorageFactory lineStorageFactory = s -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (s.equals("0")) {
                return storage[0];
            } else {
                return storage[1];
            }
        };

        Injector injector = Guice.createInjector(new BookScoreModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(LineStorageFactory.class).toInstance(lineStorageFactory);
            }
        });
        injector.getInstance(BookScoreInitializer.class).setup(fileContents);
        return injector.getInstance(BookScoreReader.class);
    }

    @Test
    public void testSimpleExampleTest() throws Exception {
        BookScoreReader reader = setupAndGetReader("small.xml");

        assertEquals(Arrays.asList("Boobar", "Foobar", "Moobar"), reader.getReviewedBooks("123"));
        assertEquals(OptionalDouble.of(6.0), reader.getScoreAverageForReviewer("123"));
        assertEquals(OptionalDouble.of(10.0), reader.getAverageReviewScoreForBook("Foobar"));
    }

    @Test
    public void shouldFindGaveReview() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertTrue(reader.gaveReview("0", "0"));
        assertTrue(reader.gaveReview("50", "0"));
        assertTrue(reader.gaveReview("99", "99"));
        assertTrue(reader.gaveReview("0", "99"));
        assertTrue(reader.gaveReview("99", "50"));
    }

    @Test
    public void shouldFailGaveReview() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertFalse(reader.gaveReview("0", "00"));
        assertFalse(reader.gaveReview("5a", "0"));
        assertFalse(reader.gaveReview("100", "5"));
        assertFalse(reader.gaveReview("a1", "z"));
        assertFalse(reader.gaveReview("9", "222"));
    }

    @Test
    public void shouldFindCorrectScore() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.of(0), reader.getScore("0", "0"));
        assertEquals(OptionalDouble.of(99), reader.getScore("99", "99"));
        assertEquals(OptionalDouble.of(0), reader.getScore("99", "0"));
        assertEquals(OptionalDouble.of(99), reader.getScore("0", "99"));
        assertEquals(OptionalDouble.of(5), reader.getScore("50", "5"));
    }

    @Test
    public void shouldFindEmptyScore() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.empty(), reader.getScore("0", "00"));
        assertEquals(OptionalDouble.empty(), reader.getScore("5a", "0"));
        assertEquals(OptionalDouble.empty(), reader.getScore("100", "5"));
        assertEquals(OptionalDouble.empty(), reader.getScore("a1", "z"));
        assertEquals(OptionalDouble.empty(), reader.getScore("9", "222"));
    }

    @Test
    public void shouldFindCorrectReviewedBooks() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        List<String> expectedReviewedBooks = IntStream.range(0,100).mapToObj(i -> "" + i).collect(Collectors.toList());
        expectedReviewedBooks.sort(String::compareTo);

        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("0"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("19"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("51"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("78"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("99"));
    }

    @Test
    public void shouldFindEmptyReviewedBooks() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        List<String> expectedReviewedBooks = new ArrayList<>();

        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("00"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("a"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("103"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("999"));
        assertEquals(expectedReviewedBooks,reader.getReviewedBooks("5b"));
    }

    @Test
    public void shouldFindCorrectAllReviewsByReviewer() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        HashMap<String, Integer> expectedMap = new HashMap<>();
        IntStream.range(0,100).forEach(i -> expectedMap.put("" + i,i));

        assertEquals(expectedMap, reader.getAllReviewsByReviewer("0"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("20"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("40"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("60"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("99"));
    }

    @Test
    public void shouldFindEmptyAllReviewsByReviewer() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        HashMap<String, Integer> expectedMap = new HashMap<>();

        assertEquals(expectedMap, reader.getAllReviewsByReviewer("000"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("01"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("1z3"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("808"));
        assertEquals(expectedMap, reader.getAllReviewsByReviewer("z"));
    }

    @Test
    public void shouldGetCorrectAverageForReviewer() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("0"));
        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("1"));
        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("59"));
        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("83"));
        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("99"));
    }

    @Test
    public void shouldGetEmptyAverageForReviewer() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.empty(), reader.getScoreAverageForReviewer("0000"));
        assertEquals(OptionalDouble.empty(), reader.getScoreAverageForReviewer("a"));
        assertEquals(OptionalDouble.empty(), reader.getScoreAverageForReviewer("557"));
        assertEquals(OptionalDouble.empty(), reader.getScoreAverageForReviewer("8999"));
        assertEquals(OptionalDouble.empty(), reader.getScoreAverageForReviewer("99999"));
    }

    @Test
    public void shouldGetCorrectReviewers() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        List<String> expectedReviewers = IntStream.range(0,100).mapToObj(i -> "" + i).collect(Collectors.toList());
        expectedReviewers.sort(String::compareTo);

        assertEquals(expectedReviewers, reader.getReviewers("0"));
        assertEquals(expectedReviewers, reader.getReviewers("33"));
        assertEquals(expectedReviewers, reader.getReviewers("55"));
        assertEquals(expectedReviewers, reader.getReviewers("66"));
        assertEquals(expectedReviewers, reader.getReviewers("99"));
    }

    @Test
    public void shouldGetEmptyReviewers() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        List<String> expectedReviewers = new ArrayList<>();

        assertEquals(expectedReviewers, reader.getReviewers("00"));
        assertEquals(expectedReviewers, reader.getReviewers("3a3"));
        assertEquals(expectedReviewers, reader.getReviewers("555"));
        assertEquals(expectedReviewers, reader.getReviewers("z"));
        assertEquals(expectedReviewers, reader.getReviewers("999"));
    }

    @Test
    public void shouldFindCorrectReviewsForBook() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        IntStream.of(0, 20, 40, 60 , 99).forEach(i -> {
            HashMap<String, Integer> expectedMap = new HashMap<>();
            IntStream.range(0,100).forEach(j -> expectedMap.put("" + j,i));
            assertEquals(expectedMap, reader.getReviewsForBook("" + i));
        });
    }

    @Test
    public void shouldFindEmptyReviewsForBook() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        HashMap<String, Integer> expectedMap = new HashMap<>();

        assertEquals(expectedMap, reader.getReviewsForBook("000"));
        assertEquals(expectedMap, reader.getReviewsForBook("01"));
        assertEquals(expectedMap, reader.getReviewsForBook("1z3"));
        assertEquals(expectedMap, reader.getReviewsForBook("808"));
        assertEquals(expectedMap, reader.getReviewsForBook("z"));
    }

    @Test
    public void shouldGetCorrectAverageForBook() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.of(0), reader.getAverageReviewScoreForBook("0"));
        assertEquals(OptionalDouble.of(1), reader.getAverageReviewScoreForBook("1"));
        assertEquals(OptionalDouble.of(59), reader.getAverageReviewScoreForBook("59"));
        assertEquals(OptionalDouble.of(83), reader.getAverageReviewScoreForBook("83"));
        assertEquals(OptionalDouble.of(99), reader.getAverageReviewScoreForBook("99"));
    }

    @Test
    public void shouldGetEmptyAverageForBook() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        assertEquals(OptionalDouble.empty(), reader.getAverageReviewScoreForBook("0000"));
        assertEquals(OptionalDouble.empty(), reader.getAverageReviewScoreForBook("a"));
        assertEquals(OptionalDouble.empty(), reader.getAverageReviewScoreForBook("557"));
        assertEquals(OptionalDouble.empty(), reader.getAverageReviewScoreForBook("8999"));
        assertEquals(OptionalDouble.empty(), reader.getAverageReviewScoreForBook("99999"));
    }
}
