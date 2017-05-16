package il.ac.technion.cs.sd.book.library;

import il.ac.technion.cs.sd.book.ext.LineStorage;

import javax.inject.Inject;
import java.util.*;

public class StringStorage extends AbstractList<String> implements RandomAccess, Storage {
    private LineStorage lineStorage;

    @Inject
    public StringStorage(LineStorage lineStorage) {
        this.lineStorage = lineStorage;
    }

    @Override
    public String get(int index) {
        try {
            return lineStorage.read(index);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            return lineStorage.numberOfLines();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> getStringByIds(String id0, String id1) {
        int keyFound = findIndexByTwoKeys(String.join(",", id0, id1));
        if (keyFound >= 0) {
            return Optional.of(get(keyFound));
        } else {
            return Optional.empty();
        }
    }

    private int findIndexByTwoKeys(String keys) {
        int keyFound;

        try {
            keyFound = Collections.binarySearch(this, keys,
                    Comparator.comparing((String s) -> s.split(",")[0])
                            .thenComparing((String s)-> s.split(",")[1]));
        } catch (RuntimeException e) {
            throw e;
        }

        return keyFound;
    }

    private int findIndexBySingleKey(String key) {
        int keyFound;

        try {
            keyFound = Collections.binarySearch(this, key,
                    Comparator.comparing((String s) -> s.split(",")[0]));
        } catch (RuntimeException e) {
            throw e;
        }

        return keyFound;
    }
}
