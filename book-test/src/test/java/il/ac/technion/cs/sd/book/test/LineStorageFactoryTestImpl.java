package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

public class LineStorageFactoryTestImpl implements LineStorageFactory {
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        return new LineStorage() {
            final int SIZE = 100;

            @Override
            public void appendLine(String s) {

            }

            @Override
            public String read(int i) throws InterruptedException {
                return "" + (i/10) + "," + i + "," + (i%10);
            }

            @Override
            public int numberOfLines() throws InterruptedException {
                return SIZE;
            }
        };
    }
}
