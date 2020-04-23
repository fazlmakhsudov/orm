package com.practice.orm.db.utilDao.entiry;

import java.util.ResourceBundle;
import java.util.logging.Logger;

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
}
