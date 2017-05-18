package il.ac.technion.cs.sd.book.app;

import com.google.inject.*;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;

import java.util.SortedMap;

public class BookScoreInitializerImpl implements BookScoreInitializer {

    private LineStorageFactory lineStorageFactory;

    private static final Integer REVIEWERS_FIRST_INDEX = 0;
    private static final Integer BOOKS_FIRST_INDEX = 1;

    public static final String REVIEWERS_FIRST_FILENAME = REVIEWERS_FIRST_INDEX.toString();
    public static final String BOOKS_FIRST_FILENAME = BOOKS_FIRST_INDEX.toString();

    @Inject
    public BookScoreInitializerImpl(LineStorageFactory lineStorageFactory) {
        this.lineStorageFactory = lineStorageFactory;
    }

    @Override
    public void setup(String xmlData) {
        SortedMap<String, String>[] sortedMap;
        try {
            sortedMap = XPathXMLParser.parseXMLToSortedMap(xmlData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Injector reviewersInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(LineStorageFactory.class).toInstance(lineStorageFactory);
            }

            @Provides
            String provideFileName() {
                return REVIEWERS_FIRST_FILENAME;
            }

            @Provides
            SortedMap<String,String> provideSortedMap() {
                return sortedMap[REVIEWERS_FIRST_INDEX];
            }
        });
        reviewersInjector.getInstance(StringStorage.class);

        Injector booksInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(LineStorageFactory.class).toInstance(lineStorageFactory);
            }

            @Provides
            String provideFileName() {
                return BOOKS_FIRST_FILENAME;
            }

            @Provides
            SortedMap<String,String> provideSortedMap() {
                return sortedMap[BOOKS_FIRST_INDEX];
            }
        });
        booksInjector.getInstance(StringStorage.class);
    }
}
