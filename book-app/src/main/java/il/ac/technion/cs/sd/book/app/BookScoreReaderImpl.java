package il.ac.technion.cs.sd.book.app;


import il.ac.technion.cs.sd.book.library.Storage;

import java.util.*;
import java.util.stream.Collectors;

public class BookScoreReaderImpl implements BookScoreReader {
    Storage reviewersStorage;
    Storage bookStorage;

    private static final int REVIEWER_INDEX = 0;
    private static final int BOOK_INDEX = 1;
    private static final int SCORE_INDEX = 2;

    private static final String DELIMITER = ",";

    public BookScoreReaderImpl(){

    }

    @Override
    public boolean gaveReview(String reviewerId, String bookId) {
        return reviewersStorage.exists(reviewerId, bookId);
    }

    @Override
    public OptionalDouble getScore(String reviewerId, String bookId) {
        Optional<String> resultString = reviewersStorage.getStringByIds(reviewerId, bookId);
        if (resultString.isPresent()) {
            return OptionalDouble.of(Integer.valueOf(resultString.get().split(DELIMITER)[SCORE_INDEX]));
        } else {
            return OptionalDouble.empty();
        }
    }

    @Override
    public List<String> getReviewedBooks(String reviewerId) {
        return reviewersStorage.getAllStringsById(reviewerId)
                .stream()
                .map(s -> s.split(DELIMITER)[BOOK_INDEX])
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
        Map<String, Integer> reviewsByReviewer = new HashMap<>();
        reviewersStorage.getAllStringsById(reviewerId)
                .stream()
                .forEach(s -> {
                    String[] splitLineReview = s.split(DELIMITER);
                    reviewsByReviewer.put(
                            splitLineReview[BOOK_INDEX],
                            Integer.valueOf(splitLineReview[SCORE_INDEX])
                    );
                });
        return reviewsByReviewer;
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        List<String> reviewsByReviewer = reviewersStorage.getAllStringsById(reviewerId);

        if (!reviewsByReviewer.isEmpty()) {
            double sumOfReviewScores = reviewsByReviewer
                    .stream()
                    .mapToInt(s -> Integer.valueOf(s.split(DELIMITER)[SCORE_INDEX]))
                    .sum();

            return OptionalDouble.of(sumOfReviewScores / reviewsByReviewer.size());
        } else {
            return OptionalDouble.empty();
        }
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
