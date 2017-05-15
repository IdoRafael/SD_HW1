package il.ac.technion.cs.sd.book.app;

public class ReviewBuilder {
    private String bookName;
    private int bookScore;

    public ReviewBuilder setBookName (String bookName) {
        this.bookName = bookName;
        return this;
    }

    public ReviewBuilder setbookScore (int bookScore) {
        this.bookScore = bookScore;
        return this;
    }

    public Review build(){
        return new Review(bookName, bookScore);
    }
}
