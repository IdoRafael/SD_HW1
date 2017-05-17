package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringStorageTest {

    static final int LINE_STORAGE_SIZE = 100;

    //log2(size)+1 iterations + 1 for 2nd compare (to check if "equals")
    static final int BINARY_SEARCH_ITERATIONS = (int)(Math.log(LINE_STORAGE_SIZE)/Math.log(2)) + 2;
    final int AMOUNT_TO_RETURN = 10;

    private static StringStorage setupStringStorage(final LineStorage lineStorage) throws InterruptedException {

        Mockito.when(lineStorage.numberOfLines()).thenReturn(LINE_STORAGE_SIZE);

        Mockito.doAnswer(invocationOnMock -> {
            int i = (int)invocationOnMock.getArguments()[0];
            return String.join(",", "" + i/10, "" + i%10, "" + i%10);
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

    private void existTest(String id0, String id1, boolean exists) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        assertEquals(exists, stringStorage.exists(id0, id1));

        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS)).read(Mockito.anyInt());
    }

    private void getSingleStringsByIdTest(String id0, String id1) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        Optional<String> result = stringStorage.getStringByIds(id0, id1);
        String[] stringResults = result.get().split(",");

        assertTrue(result.isPresent());
        assertEquals(id0, stringResults[0]);
        assertEquals(id1, stringResults[1]);

        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS)).read(Mockito.anyInt());
    }

    private void getAllStringsByIdTest(String id) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        List<String> resultList = stringStorage.getAllStringsById(id);
        List<String> expectedList = IntStream.range(0, AMOUNT_TO_RETURN)
                .mapToObj(i -> String.join(",", id.toString(), "" + i, "" + i))
                .collect(Collectors.toList());

        assertEquals(expectedList, resultList);
        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS + AMOUNT_TO_RETURN)).read(Mockito.anyInt());
    }

    private void missSingleStringsByIdTest(String id0, String id1) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        Optional<String> result = stringStorage.getStringByIds(id0, id1);

        assertFalse(result.isPresent());

        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS)).read(Mockito.anyInt());
    }

    private void missAllStringsByIdTest(String id) throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        List<String> resultList = stringStorage.getAllStringsById(id);

        assertTrue(resultList.isEmpty());
        Mockito.verify(lineStorage, Mockito.atMost(BINARY_SEARCH_ITERATIONS + AMOUNT_TO_RETURN)).read(Mockito.anyInt());
    }

    @Test
    public void shouldAppendRightAmountOfLines() throws InterruptedException {
        LineStorage lineStorage = Mockito.mock(LineStorage.class);
        StringStorage stringStorage = setupStringStorage(lineStorage);

        SortedMap<String, String> sortedMap = new TreeMap<>();
        IntStream.range(0, LINE_STORAGE_SIZE)
                .forEach(i -> sortedMap.put("" + i, ""));

        stringStorage.appendLines(sortedMap);

        Mockito.verify(lineStorage, Mockito.times(LINE_STORAGE_SIZE)).appendLine(Mockito.anyString());
    }

    @Test
    public void shouldExistInStart() throws InterruptedException {
        existTest("0", "0", true);
    }

    @Test
    public void shouldExistInMiddle() throws InterruptedException {
        existTest("5" , "5", true);
    }

    @Test
    public void shouldExistInEnd() throws InterruptedException {
        existTest("9", "9", true);
    }

    @Test
    public void shouldntExistInStart() throws InterruptedException {
        existTest("", "0", false);
    }

    @Test
    public void shouldntExistInMiddle() throws InterruptedException {
        existTest("50" , "5", false);
    }

    @Test
    public void shouldntExistInEnd() throws InterruptedException {
        existTest("9", "999", false);
    }

    @Test
    public void shouldFindSingleInStart() throws InterruptedException {
        getSingleStringsByIdTest("0", "0");
    }

    @Test
    public void shouldFindSingleInMiddle() throws InterruptedException {
        getSingleStringsByIdTest("5" , "5");
    }

    @Test
    public void shouldFindSingleInEnd() throws InterruptedException {
        getSingleStringsByIdTest("9", "9");
    }

    @Test
    public void shouldMissSingleInStart() throws InterruptedException {
        missSingleStringsByIdTest("", "0");
    }

    @Test
    public void shouldMissSingleInMiddle() throws InterruptedException {
        missSingleStringsByIdTest("50" , "5");
    }

    @Test
    public void shouldMissSingleInEnd() throws InterruptedException {
        missSingleStringsByIdTest("9", "999");
    }

    @Test
    public void shouldFindGroupInStart() throws InterruptedException {
        getAllStringsByIdTest("0");
    }

    @Test
    public void shouldFindGroupInMiddle() throws InterruptedException {
        getAllStringsByIdTest("5");
    }

    @Test
    public void shouldFindGroupInEnd() throws InterruptedException {
        getAllStringsByIdTest("9");
    }

    @Test
    public void shouldMissGroupInStart() throws InterruptedException {
        missAllStringsByIdTest("");
    }

    @Test
    public void shouldMissGroupInMiddle() throws InterruptedException {
        missAllStringsByIdTest("50");
    }

    @Test
    public void shouldMissGroupInEnd() throws InterruptedException {
        missAllStringsByIdTest("999");
    }
}
