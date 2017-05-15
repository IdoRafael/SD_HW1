package il.ac.technion.cs.sd.book.app;

import java.util.ArrayList;

/**
 * Created by Yaniv on 16/05/2017.
 */
public class Reviewer {
    private final int id;
    private final ArrayList<Review> reviews;


    public Reviewer(int id, ArrayList<Review> reviews) {
        this.id = id;
        this.reviews = reviews;
    }
}
