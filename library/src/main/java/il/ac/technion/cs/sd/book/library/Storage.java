package il.ac.technion.cs.sd.book.library;


import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

public interface Storage {
    void appendLines(SortedMap<String,String> sortedMap);

    boolean exists(String id0, String id1);

    Optional<String> getStringByIds(String id0, String id1);

    List<String> getAllStringsById(String id);
}
