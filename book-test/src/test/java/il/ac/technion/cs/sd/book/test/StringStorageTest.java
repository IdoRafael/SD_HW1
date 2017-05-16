package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;
import org.junit.Test;


public class StringStorageTest {


  @Test
  public void testSimple2() throws Exception {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(LineStorageFactory.class).to(LineStorageFactoryTestImpl.class);
      }
    });
    StringStorage stringStorage = injector.getInstance(StringStorage.class);
    stringStorage.getAllStringsById("6");
  }
}
