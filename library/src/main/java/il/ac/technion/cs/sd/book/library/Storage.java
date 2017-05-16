package il.ac.technion.cs.sd.book.library;


import java.util.Optional;

public interface Storage {
    Optional<String> getStringByIds(String id0, String id1);
}
