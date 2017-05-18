package il.ac.technion.cs.sd.book.library;

public interface StorageFactory {
    Storage create(String fileName);
    Storage create(String fileName, String xmlString, String xmlQuery,boolean swapKeys);
}
