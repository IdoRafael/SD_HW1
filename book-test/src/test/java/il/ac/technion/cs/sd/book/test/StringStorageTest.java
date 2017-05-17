package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class StringStorageTest {

    static final int LINE_STORAGE_SIZE = 1000;
    static final int BINARY_SEARCH_ITERATIONS = (int)(Math.log(LINE_STORAGE_SIZE)/Math.log(2));
    final int AMOUNT_TO_RETURN = 100;

    private static StringStorage setupStringStorage(final LineStorage lineStorage) throws InterruptedException {

        Mockito.when(lineStorage.numberOfLines()).thenReturn(LINE_STORAGE_SIZE);

        Mockito.doAnswer(invocationOnMock -> {
            int i = (int)invocationOnMock.getArguments()[0];
            return String.join(",", "" + i/100, "" + i%100, "" + i%100);
        }).when(lineStorage).read(Mockito.anyInt());

        LineStorageFactory lineStorageFactory = s -> lineStorage;

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(LineStorageFactory.class).toInstance(lineStorageFactory);
            }
        });
        return injector.getInstance(StringStorage.class);
    }

    private void getAllStringsByIdTest(Integer index) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        List<String> resultList = stringStorage.getAllStringsById(index.toString());
        List<String> expectedList = new ArrayList<>(AMOUNT_TO_RETURN);
        IntStream.range(0, AMOUNT_TO_RETURN)
                .mapToObj(i -> String.join(",", index.toString(), "" + i, "" + i));

        assertEquals(expectedList, resultList);
        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS + AMOUNT_TO_RETURN)).read(Mockito.anyInt());
    }


    @Test
    public void testNameLolsChange() {
    }

}
