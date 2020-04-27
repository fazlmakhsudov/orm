package com.practice.orm.db.utilDao.entiry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QueryFormer {
    private static QueryFormer queryFormer;
     private Map<String, Map<String, String>> tableQueries;
     private PropertyBundle propertyBundle;
     private Map<String, List<String>> tablesAndColumns;
     private static final Logger logger = Logger.getLogger("QueryFormer.class");

    private QueryFormer() {
        this.tableQueries = new HashMap<>();
        logger.info("QueryFormer object has been instantiated");
    }

    public static QueryFormer getInstance() {
        if (queryFormer == null) {
            queryFormer = new QueryFormer();
        }
        return queryFormer;
    }

    public void setTablesAndColumns(Map<String, List<String>> tablesAndColumns) {
        this.tablesAndColumns = tablesAndColumns;
        queryFormer.tableQueries = tablesAndColumns.keySet().stream()
                .collect(Collectors.toMap(s -> s, s -> new HashMap<>()));
        logger.log(Level.INFO, "setTablesAndColumns() with: {0}",
                new String[]{tablesAndColumns.toString()});
    }

    public void setPropertyBundle(PropertyBundle propertyBundle) {
        queryFormer.propertyBundle = propertyBundle;
        logger.log(Level.INFO, "setPropertyBundle() with: {0}",
                new String[]{propertyBundle.toString()});
    }

    public String getQuery(String tableName, String action) {
        return queryFormer.tableQueries.get(tableName).get(action);
    }

    public void formQueriesForAllTables() {
        Map<String, String> propertyBundlePatterns = queryFormer.propertyBundle.getQueriesInMap();
        queryFormer.tablesAndColumns.keySet()
                .stream()
                .forEach(tableName -> {
                    queryFormer.tableQueries.put(tableName,
                            queryFormer.getActionQueriesForTable(propertyBundlePatterns, tableName));
                });
        logger.log(Level.INFO, "formQueriesForAllTables(): {0}",
                new String[]{printTablesQueries()});
    }

    private Map<String, String> getActionQueriesForTable(Map<String, String> propertyBundlePatterns, String tableName) {
        Map<String, String> actionQueries = new HashMap<>();
        propertyBundlePatterns.forEach((action, queryPattern) ->
        {
            actionQueries.put(action, queryFormer.formQuery(tableName, action, queryPattern));
        });
        logger.log(Level.INFO, "getActionQueriesForTable({0}) returns: {1}",
                new String[]{tableName, actionQueries.toString()});
        return actionQueries;
    }

    private String formQuery(String tableName, String action, String queryPattern) {
        String formedQuery = "";
       // needs to accomplish

        logger.log(Level.INFO, "formQuery({0}, {1}, {2}) returns: {3}",
                new String[]{tableName, action, queryPattern, formedQuery});
        return formedQuery;
    }

    private String formCreateQuery(String tableName, String queryPattern) {
        String columnId = queryFormer.getColumnId(tableName);
        // needs to accomplish

        return queryPattern;
    }

    private String changeTableName(String tableName, String queryPattern) {
        // needs to accomplish
        return queryPattern.replace("*table*", tableName);
    }

    private String getColumnId(String tableName) {
        return queryFormer.tablesAndColumns.get(tableName).get(0);
    }

    private String getColumns(String tableName, String columnId) {
        return queryFormer.tablesAndColumns.get(tableName).stream()
                .filter(s -> !s.equalsIgnoreCase(columnId))
                .reduce((s1, s2) -> s1 + ", " + s2).get();
    }

    private String getValues(String tableName, String columnId) {
        // needs to accomplish

        return null;
    }

    private String formReadQuery(String tableName, String queryPattern) {
        queryPattern = queryFormer.changeTableName(tableName, queryPattern);
        queryPattern = queryPattern.replace("*condition*", queryFormer.getCondition(tableName));
        return queryPattern;
    }

    private String getCondition(String tableName) {
        return queryFormer.tablesAndColumns.get(tableName).get(0) + "=?";
    }

    private String formUpdateQuery(String tableName, String queryPattern) {
        queryPattern = queryFormer.changeTableName(tableName, queryPattern);
        // needs to accomplish

        return queryPattern;
    }

    private String getColumnValue(String tableName, String id) {
        return queryFormer.tablesAndColumns.get(tableName).stream()
                .filter(s -> !s.equalsIgnoreCase(id))
                .reduce((s1, s2) -> s1 + "=?, " + s2).get().concat("=?");
    }

    private String formDeleteQuery(String tableName, String queryPattern) {
        // needs to accomplish

        return queryPattern;
    }

    private String formReadAllQuery(String tableName, String queryPattern) {
        return queryFormer.changeTableName(tableName, queryPattern);
    }

    private String printTablesQueries() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nMy tables and queries for them:");
        queryFormer.tableQueries.forEach(
                (tableName, actionMap) -> {
                    sb.append("\n\t" + tableName + ":");
                    actionMap.forEach(
                            (action, query) -> {
                                sb.append("\n\t\t" + action + ":\t" + query);
                            }
                    );
                }
        );
        sb.append("\n");
        return sb.toString();
    }
}
