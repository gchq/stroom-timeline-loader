# Stroom Timeline Loader

Loads event data into the Stroom Timeline.

## Building
This project depends on `stroom:stroom-timeline`.

```bash
./gradlew clean build shadowJar
```

## Running
The `run` task in `build.gradle` looks for `config_dev.yml`. You'll need to edit this config file to make sure it's contents are valid, e.g. `inputDirectory` must point to an existing directory.
