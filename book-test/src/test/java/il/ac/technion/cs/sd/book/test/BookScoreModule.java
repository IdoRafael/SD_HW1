package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.app.BookScoreReaderImpl;
import il.ac.technion.cs.sd.book.library.Storage;
import il.ac.technion.cs.sd.book.library.StorageFactory;
import il.ac.technion.cs.sd.book.library.StringStorage;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BookScoreInitializer.class).to(BookScoreInitializerImpl.class);
        bind(BookScoreReader.class).to(BookScoreReaderImpl.class);
        install(new FactoryModuleBuilder()
                .implement(Storage.class, StringStorage.class)
                .build(StorageFactory.class));

        bind(String.class)
                .annotatedWith(Names.named("reviewersFileName"))
        .toInstance("0");

        bind(String.class)
                .annotatedWith(Names.named("booksFileName"))
                .toInstance("1");
    }
}
