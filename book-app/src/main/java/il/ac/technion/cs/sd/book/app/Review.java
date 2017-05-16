package il.ac.technion.cs.sd.book.app;

public class Review {
    private final String reviewerId;
    private final String bookId;
    private final String bookScore;

    public Review(String reviewerId, String bookId, String bookScore) {
        this.reviewerId = reviewerId;
        this.bookId = bookId;
        this.bookScore = bookScore;
    }

    @Override
    public String toString() {
        return "[Reviewer: " + reviewerId + "; Book: " + bookId + "; Score: " + bookScore + "]";
    }
}


