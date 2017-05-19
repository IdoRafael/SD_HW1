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


/*    @Test
    public void ido() throws Exception {
        try{
            PrintWriter writer = new PrintWriter("C:\\Users\\Ido\\Desktop\\asdf\\huge.xml", "UTF-8");
            writer.println("<Root>\n");


            IntStream.range(0, 100)
                    .forEachOrdered(i -> {

                        writer.println(new MessageFormat("  <Reviewer Id=\"{0}\">\n").format(new Object[] {i}));

                        IntStream.range(0, 100)
                                .forEachOrdered(j -> {
                                            writer.println(new MessageFormat(
                                                    "    <Review>\n" +
                                                    "      <Id>{0}</Id>\n" +
                                                    "      <Score>{1}</Score>\n" +
                                                    "    </Review>\n").format(new Object[] {j, j}));
                                        }
                                        );

                        writer.println("  </Reviewer>\n");
                    });

            writer.println("</Root>\n");

            writer.close();
        } catch (IOException e) {
            // do something
        }

    }*/

    @Test
    public void hugeTest() throws Exception {
        BookScoreReader reader = setupAndGetReader("huge.xml");

        List<String> expected = IntStream.range(0,100).mapToObj(i -> "" + i).collect(Collectors.toList());
        expected.sort(String::compareTo);

        assertEquals(expected,reader.getReviewedBooks("5"));
        assertEquals(OptionalDouble.of(49.5), reader.getScoreAverageForReviewer("5"));
        assertEquals(OptionalDouble.of(50.0), reader.getAverageReviewScoreForBook("50"));
    }
}
