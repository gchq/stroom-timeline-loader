package stroom.timeline.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Singleton
public class DirectoryWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryWatcher.class);
    private String watchedDirectory;
    private String errorMessage = null;

    private Config config;
    private FileProcessor fileProcessor;
    private boolean isWatching = true;

    @Inject
    public DirectoryWatcher(Config config, FileProcessor fileProcessor){
        this.config = config;
        this.fileProcessor = fileProcessor;

        File inputDirectory = Paths.get(config.getInputDirectory()).toFile();
        if(!inputDirectory.exists()){
            isWatching = false;
            errorMessage = "Input directory does not exist! Tried to use: " + inputDirectory.toString();
            LOGGER.error(errorMessage);
        }
        else {
            watchedDirectory = inputDirectory.toString();
            Runnable task = () -> {
                LOGGER.info("Watching {} for new input.", config.getInputDirectory());

                while (true) {
                    File[] inputFiles = inputDirectory.listFiles();
                    // Ordering by date created won't work if the volume of input files is too high,
                    // because the timestamp resolution in the system isn't high enough.
                    // We order by name. This places a requirement on the data coming in - it must be sequential.
                    Arrays.sort(inputFiles, Comparator.comparing(File::getName));

                    for (File file : inputFiles) {
                        processFile(file);
                        file.delete();
                    }
                }
            };

            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private void processFile(File inputFile){
        Path inputDirectory = Paths.get(config.getInputDirectory());
        Path child = inputDirectory.resolve(inputFile.toPath());

        boolean isFileTypeValid = isFileTypeValid(child);
        Optional<String> fileData = readData(inputFile.toPath());

        if (isFileTypeValid && fileData.isPresent()){
           fileProcessor.processFile(fileData.get());
        }
        else{
            LOGGER.error("Unable to process file: {{}}", inputFile.toPath().toAbsolutePath().toString());
        }
    }

    private static boolean isFileTypeValid(Path file){
        String fileType;

        try {
            fileType = Files.probeContentType(file);
        } catch(IOException e){
            LOGGER.error("File processing failure! Unknown file type for file {{}}, exception is: ",
                    file.toAbsolutePath().toString(), e);
            return false;
        }

        if (fileType == null ||
                (!fileType.equals("text/plain") &&
                !fileType.equals("text/xml") &&
                !fileType.equals("application/xml"))) {
            LOGGER.error(
                    "Invalid file type in InputDirectory! File name is {{}}, detected file type is {{}}",
                    file.toString(),
                    fileType);
            return false;
        }

        return true;
    }

    private static Optional<String> readData(Path file){
        String fileData = null;
        try {
            fileData = new String(Files.readAllBytes(file));
        } catch( IOException e){
            LOGGER.error("File processing failure! Cannot read file {{}}. Exception is: ",
                    file.toAbsolutePath().toString(), e);
        }
        if(fileData != null){
            return Optional.of(fileData);
        }
        else{
            return Optional.empty();
        }
    }


    public boolean isWatching() {
        return isWatching;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getWatchedDirectory() {
        return watchedDirectory;
    }
}
