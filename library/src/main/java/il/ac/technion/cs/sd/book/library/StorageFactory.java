package il.ac.technion.cs.sd.book.library;

import java.util.SortedMap;

public interface StorageFactory {
    Storage create(String fileName);
    Storage create(String fileName, SortedMap<String, String> sortedMap);
}
