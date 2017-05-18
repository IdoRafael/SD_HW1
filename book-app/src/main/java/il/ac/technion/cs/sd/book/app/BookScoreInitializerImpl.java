package il.ac.technion.cs.sd.book.app;

import com.google.inject.*;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.StorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;

import javax.inject.Named;
import java.util.SortedMap;

public class BookScoreInitializerImpl implements BookScoreInitializer {

    private StorageFactory storageFactory;

    private static final Integer REVIEWERS_FIRST_INDEX = 0;
    private static final Integer BOOKS_FIRST_INDEX = 1;

    private String reviewersFileName;
    private String booksFileName;

    @Inject
    public BookScoreInitializerImpl(
            StorageFactory storageFactory,
            @Named("reviewersFileName") String reviewersFileName,
            @Named("booksFileName") String booksFileName)
    {
        this.storageFactory = storageFactory;
        this.reviewersFileName = reviewersFileName;
        this.booksFileName = booksFileName;
    }

    @Override
    public void setup(String xmlData) {
        SortedMap<String, String>[] sortedMap;
        try {
            sortedMap = XPathXMLParser.parseXMLToSortedMap(xmlData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        storageFactory.create(reviewersFileName, sortedMap[REVIEWERS_FIRST_INDEX]);
        storageFactory.create(booksFileName, sortedMap[BOOKS_FIRST_INDEX]);
    }
}
