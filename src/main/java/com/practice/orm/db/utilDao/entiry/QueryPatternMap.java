package com.practice.orm.db.utilDao.entiry;

import java.util.HashMap;
import java.util.Map;

public class QueryPatternMap {
    private static QueryPatternMap queryPatternMap;
    private final Map<String, Map<String, String>> bigPatternMap;
    private String dbName;

    private QueryPatternMap() {
        this.bigPatternMap = new HashMap<>();
    }

    public static QueryPatternMap getInstance() {
        if (queryPatternMap == null) {
            queryPatternMap = new QueryPatternMap();
            queryPatternMap.initializeBigPatternMap();
        }
        return queryPatternMap;
    }

    private void initializeBigPatternMap() {
        bigPatternMap.put(DbKeys.MYSQL, new HashMap<>());
        bigPatternMap.put(DbKeys.POSTGRES, new HashMap<>());
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.CREATE, "INSERT INTO *table* (*columns*) VALUES (*values*);");
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.READ, "SELECT * FROM *table* WHERE *condition*;");
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.UPDATE, "UPDATE *table* SET *set-block* WHERE *condition*;");
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.DELETE, "DELETE FROM *table* WHERE *condition*;");
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.READ_ALL, "SELECT * FROM *table*;");
        bigPatternMap.get(DbKeys.MYSQL).put(DbKeys.CREATE_TABLE, "CREATE TABLE IF NOT EXISTS *table*(*columns*);");

        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.CREATE, "INSERT INTO *table* (*columns*) VALUES (*values*);");
        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.READ, "SELECT * FROM *table* WHERE *condition*;");
        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.UPDATE, "UPDATE *table* SET *set-block* WHERE *condition*;");
        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.DELETE, "DELETE FROM *table* WHERE *condition*;");
        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.READ_ALL, "SELECT * FROM *table*;");
        bigPatternMap.get(DbKeys.POSTGRES).put(DbKeys.CREATE_TABLE, "CREATE TABLE IF NOT EXISTS *table*(*columns*);");
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPattern(String key) {
        return queryPatternMap.bigPatternMap.get(dbName).get(key);
    }

    public Map<String, String> getQueriesInMap() {
        return queryPatternMap.bigPatternMap.get(dbName);
    }
}