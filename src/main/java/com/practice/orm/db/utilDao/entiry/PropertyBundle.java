package com.practice.orm.db.utilDao.entiry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Keeps ResourceBundle object and gives access to its content
 */
public class PropertyBundle {
    private ResourceBundle resourceBundle;
    private Map<String, String> queriesInMap;
    private static final Logger logger = Logger.getLogger("DBUtil.class");

    public PropertyBundle() {
        try {

            String propertyFileName = getPropertyFileName();
            this.resourceBundle = ResourceBundle.getBundle(propertyFileName);
            QueryPatternMap.getInstance().setDbName(resourceBundle.getString("db"));
            this.queriesInMap = QueryPatternMap.getInstance().getQueriesInMap();

        } catch (IOException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
        logger.info("property object has been instantiated");
    }

    public String getQuery(String key) {
        return this.queriesInMap.get(key);
    }

    public String getConfiguration(String key) {
        logger.info(String.format("property object returns value of key %s", key));
        return this.resourceBundle.getString(key);
    }

    public Map<String, String> getQueriesInMap() {
        return this.queriesInMap;
    }

    private String getPropertyFileName() throws IOException {
        String propertyName = getPropertyFile().get().getFileName().toString()
                .replace(".properties", "");
        return propertyName;
    }

    private Optional<Path> getPropertyFile() throws IOException {
        return Files.walk(Paths.get("src"))
                .filter(Files::isRegularFile)
                .filter(file -> isSourceClass(file))
                .findFirst();
    }

    private boolean isSourceClass(Path file) {
        String path = file.toString();
        boolean flag = path.matches(".+resources/.+[.]properties");
        flag = flag && !path.matches("src/test.+");
        return flag;
    }
}