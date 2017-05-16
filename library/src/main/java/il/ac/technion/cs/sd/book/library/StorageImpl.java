package il.ac.technion.cs.sd.book.library;

import il.ac.technion.cs.sd.book.ext.LineStorage;

import javax.inject.Inject;
import java.util.*;

public class StorageImpl extends AbstractList<String> implements RandomAccess, Storage {
    private LineStorage lineStorage;

    @Inject
    public StorageImpl(LineStorage lineStorage) {
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

    private int findIndexByTwoKeys(String string) {
        int keyFound;

        try {
            keyFound = Collections.binarySearch(this, string,
                    Comparator.comparing((String s) -> s.split(",")[0])
                            .thenComparing((String s)-> s.split(",")[1]));
        } catch (RuntimeException e) {
            throw e;
        }

        return keyFound;
    }
}
