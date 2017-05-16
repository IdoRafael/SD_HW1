package il.ac.technion.cs.sd.book.library;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import javax.inject.Inject;
import java.util.*;


public class StringStorage extends AbstractList<String> implements RandomAccess, Storage {
    private LineStorage lineStorage;

    @Inject
    public StringStorage(String fileName, LineStorageFactory lineStorageFactory) {
        this.lineStorage = lineStorageFactory.open(fileName);
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
            System.out.println("trololololollolo WTWFWTWFWTWT");
            return lineStorage.numberOfLines();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> getStringByIds(String id0, String id1) {
        int keyFound = findIndexByTwoKeys(id0, id1);
        if (keyFound >= 0) {
            return Optional.of(get(keyFound));
        } else {
            return Optional.empty();
        }
    }

    public List<String> getAllStringsById(String id) {
        int keyFound = findIndexBySingleKey(id);
        LinkedList<String> sortedBySecondaryId = new LinkedList<>();
        if (keyFound >= 0) {
            for (int i = keyFound ; i >=0 ; --i) {
                String toAdd = get(i);
                if (toAdd.split(",")[0].equals(id)) {
                    sortedBySecondaryId.addFirst(toAdd);
                } else {
                    break;
                }
            }
            for (int i = keyFound + 1 ; i < size() ; ++i) {
                String toAdd = get(i);
                if (toAdd.split(",")[0].equals(id)) {
                    sortedBySecondaryId.addLast(toAdd);
                } else {
                    break;
                }
            }
            return sortedBySecondaryId;
        } else {
            return sortedBySecondaryId;
        }
    }

    private int findIndexByTwoKeys(String key0, String key1) {
        int keyFound;

        try {
            keyFound = Collections.binarySearch(
                    this,
                    String.join(",", key0, key1),
                    Comparator.comparing((String s) -> s.split(",")[0])
                            .thenComparing((String s)-> s.split(",")[1])
            );
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
