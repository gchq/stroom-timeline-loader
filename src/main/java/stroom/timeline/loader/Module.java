package stroom.timeline.loader;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class Module extends AbstractModule {

    private Config config;

    public Module(Config config){
        this.config = config;
    }
    @Override
    protected void configure() {
        bind(StaxParser.class);
        bind(DirectoryWatcher.class);
        bind(Loader.class);
    }

    @Provides
    public Config config() {
        return config;
    }
    @Provides
    @Singleton
    public FileProcessor getStaxFileProcessor(StaxParser staxParser) {
        return staxParser.getFileProcessor();
    }
}
