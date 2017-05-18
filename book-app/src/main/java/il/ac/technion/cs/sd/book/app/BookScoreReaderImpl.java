package il.ac.technion.cs.sd.book.app;


import com.google.inject.Inject;
import com.google.inject.Provider;
import il.ac.technion.cs.sd.book.library.Storage;
import il.ac.technion.cs.sd.book.library.StorageFactory;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class BookScoreReaderImpl implements BookScoreReader {
    Storage reviewersStorage;
    Storage bookStorage;

    private static final int SECONDARY_INDEX = 1;
    private static final int SCORE_INDEX = 2;

    private static final String DELIMITER = ",";

    @Inject
    public BookScoreReaderImpl(
            StorageFactory storageFactory,
            @Named("reviewersFileName") String reviewersFileName,
            @Named("booksFileName") String booksFileName) {
        this.reviewersStorage = storageFactory.create(reviewersFileName);
        this.bookStorage = storageFactory.create(booksFileName);
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
        return getAllSecondaryForPrimaryId(reviewerId, reviewersStorage);
    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
        return getAllSecondaryAndScoresForPrimary(reviewerId, reviewersStorage);
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        return getAverageScoreForPrimaryId(reviewerId, reviewersStorage);
    }

    @Override
    public List<String> getReviewers(String bookId) {
        return getAllSecondaryForPrimaryId(bookId, bookStorage);
    }

    @Override
    public Map<String, Integer> getReviewsForBook(String bookId) {
        return getAllSecondaryAndScoresForPrimary(bookId, bookStorage);
    }

    @Override
    public OptionalDouble getAverageReviewScoreForBook(String bookId) {
        return getAverageScoreForPrimaryId(bookId, bookStorage);
    }

    private List<String> getAllSecondaryForPrimaryId(String primaryId, Storage storage) {
        return storage.getAllStringsById(primaryId)
                .stream()
                .map(s -> s.split(DELIMITER)[SECONDARY_INDEX])
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getAllSecondaryAndScoresForPrimary(String primaryId, Storage storage) {
        Map<String, Integer> reviewsForPrimaryId = new HashMap<>();
        storage.getAllStringsById(primaryId)
                .stream()
                .forEach(s -> {
                    String[] splitLineReview = s.split(DELIMITER);
                    reviewsForPrimaryId.put(
                            splitLineReview[SECONDARY_INDEX],
                            Integer.valueOf(splitLineReview[SCORE_INDEX])
                    );
                });
        return reviewsForPrimaryId;
    }

    private OptionalDouble getAverageScoreForPrimaryId(String primaryId, Storage storage) {
        List<String> reviewsByPrimary = storage.getAllStringsById(primaryId);

        if (!reviewsByPrimary.isEmpty()) {
            double sumOfReviewScores = reviewsByPrimary
                    .stream()
                    .mapToInt(s -> Integer.valueOf(s.split(DELIMITER)[SCORE_INDEX]))
                    .sum();

            return OptionalDouble.of(sumOfReviewScores / reviewsByPrimary.size());
        } else {
            return OptionalDouble.empty();
        }
    }
}
