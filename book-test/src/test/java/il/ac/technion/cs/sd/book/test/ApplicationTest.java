package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.Storage;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.*;

import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;


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
    public void testSimple() throws Exception {
        BookScoreReader reader = setupAndGetReader("small.xml");
        assertEquals(Arrays.asList("Boobar", "Foobar", "Moobar"), reader.getReviewedBooks("123"));
        assertEquals(OptionalDouble.of(6.0), reader.getScoreAverageForReviewer("123"));
        assertEquals(OptionalDouble.of(10.0), reader.getAverageReviewScoreForBook("Foobar"));
    }
}
