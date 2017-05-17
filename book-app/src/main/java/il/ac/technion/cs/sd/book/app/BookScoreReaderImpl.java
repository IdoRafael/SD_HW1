package il.ac.technion.cs.sd.book.app;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class BookScoreReaderImpl implements BookScoreReader {
    public BookScoreReaderImpl(){
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {

            }
        });
    }

    @Override
    public boolean gaveReview(String reviewerId, String bookId) {
        return false;
    }

    @Override
    public OptionalDouble getScore(String reviewerId, String bookId) {
        return null;
    }

    @Override
    public List<String> getReviewedBooks(String reviewerId) {
        return null;
    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
        return null;
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        return null;
    }

    @Override
    public List<String> getReviewers(String bookId) {
        return null;
    }

    @Override
    public Map<String, Integer> getReviewsForBook(String bookId) {
        return null;
    }

    @Override
    public OptionalDouble getAverageReviewScoreForBook(String bookId) {
        return null;
    }
}
