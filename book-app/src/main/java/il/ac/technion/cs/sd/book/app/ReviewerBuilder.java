package il.ac.technion.cs.sd.book.app;

import java.util.ArrayList;

/**
 * Created by Yaniv on 16/05/2017.
 */
public class ReviewerBuilder {
    private int id;
    private ArrayList<Review> reviews;

    public ReviewerBuilder setId(int id){
        this.id = id;
        return this;
    }

    public ReviewerBuilder addReview(Review review){
        reviews.add(review);
        return this;
    }

    public Reviewer build(){
        return new Reviewer(id, reviews);
    }
}
