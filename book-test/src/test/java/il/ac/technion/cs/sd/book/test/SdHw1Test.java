package il.ac.technion.cs.sd.book.test;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;


public class SdHw1Test {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}
