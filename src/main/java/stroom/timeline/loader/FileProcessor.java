package stroom.timeline.loader;

@FunctionalInterface
public interface FileProcessor {
    void processFile(String fileData);
}
