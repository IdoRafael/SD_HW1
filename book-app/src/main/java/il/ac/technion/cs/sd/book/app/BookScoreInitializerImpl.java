package il.ac.technion.cs.sd.book.app;

import com.google.inject.*;
import il.ac.technion.cs.sd.book.library.StorageFactory;

import javax.inject.Named;

public class BookScoreInitializerImpl implements BookScoreInitializer {

    private StorageFactory storageFactory;
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
        String query = "/Root/Reviewer/Review";
        storageFactory.create(reviewersFileName, xmlData, query, false);
        storageFactory.create(booksFileName, xmlData, query, true);
    }
}
