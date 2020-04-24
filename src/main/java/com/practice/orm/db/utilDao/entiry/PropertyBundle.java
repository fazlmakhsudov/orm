package com.practice.orm.db.utilDao.entiry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Keeps ResourceBundle object and gives access to its content
 */
public class PropertyBundle {
    private final ResourceBundle resourceBundle;
    private static final Logger logger = Logger.getLogger("DBUtil.class");

    public PropertyBundle(String path) {
        this.resourceBundle = ResourceBundle.getBundle(path);
        logger.info(String.format("property object has been initialised with %s", path));
    }

    public String getQuery(String key) {
        logger.info(String.format("property object returns value of key %s", key));
        return this.resourceBundle.getString(key);
    }

    public Map<String, String> getQueriesInMap() {
        return this.resourceBundle.keySet().stream().filter(k -> {
            return k.matches("query.+");
        }).collect(Collectors.toMap(s -> s.substring(6), s -> this.getQuery(s)));
    }
}
