package il.ac.technion.cs.sd.book.app;


import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class BookScoreReaderImpl implements BookScoreReader {


    public BookScoreReaderImpl(){

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
