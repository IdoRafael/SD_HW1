package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.ext.LineStorage;

import java.util.ArrayList;

public class LineStorageTestImpl implements LineStorage{
    ArrayList<String> lines = new ArrayList<>();

    private static final int NUMBER_OF_LINES_SLEEP_DURATION = 100;

    @Override
    public void appendLine(String s) {
        lines.add(s);
    }

    @Override
    public String read(int i) throws InterruptedException {
        String toReturn = lines.get(i);
        Thread.sleep(toReturn.length());
        return toReturn;
    }

    @Override
    public int numberOfLines() throws InterruptedException {
        Thread.sleep(NUMBER_OF_LINES_SLEEP_DURATION);
        return lines.size();
    }
}
