package com.practice.orm.db.utilDao.entiry;

import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.generator.GeneratorHandler;

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
    private Map<String, Map<String, List<String>>> primaryConfiguration;
    private final StringBuffer initialDatabaseConfiguration;
    private static final Logger logger = Logger.getLogger("QueryFormer.class");

    private QueryFormer() {
        this.tableQueries = new HashMap<>();
        this.initialDatabaseConfiguration = new StringBuffer();
        logger.info("QueryFormer object has been instantiated");
    }

    public static QueryFormer getInstance() {
        if (queryFormer == null) {
            queryFormer = new QueryFormer();
        }
        return queryFormer;
    }

    public void setTablesAndColumns(Map<String, List<String>> tablesAndColumns) {
        queryFormer.tablesAndColumns = tablesAndColumns;
        queryFormer.tableQueries = tablesAndColumns.keySet().stream()
                .collect(Collectors.toMap(s -> s, s -> new HashMap<>()));
        logger.log(Level.INFO, "setTablesAndColumns() with parameter:\n {0}",
                new String[]{tablesAndColumns.toString()});
    }

    public void setPropertyBundle(PropertyBundle propertyBundle) {
        queryFormer.propertyBundle = propertyBundle;
        logger.log(Level.INFO, "setPropertyBundle() with parameter:\n {0}",
                new String[]{propertyBundle.toString()});
    }

    public void setPrimaryConfiguration(Map<String, Map<String, List<String>>> primaryConfiguration) {
        queryFormer.primaryConfiguration = primaryConfiguration;
        logger.log(Level.INFO, "setPrimaryConfiguration() with parameter:\n {0}",
                new String[]{primaryConfiguration.toString()});
    }

    public String getInitialDatabaseConfiguration() {
        queryFormer.formInitialDatabaseConfiguration();
        return queryFormer.initialDatabaseConfiguration.toString();
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

    public void formInitialDatabaseConfiguration() {
        queryFormer.primaryConfiguration.forEach(
                (tableName, columnsMap) -> {
                    queryFormer.initialDatabaseConfiguration.append(
                            queryFormer.formCreateTableQuery(tableName, columnsMap));
                }
        );
        logger.log(Level.INFO, "formInitialDatabaseConfiguration() with parameters:\n {0}",
                new String[]{printInitialDatabaseConfiguration()});
    }

    public List<String> getFieldOrder(String tableName) {
        return queryFormer.tablesAndColumns.get(tableName);
    }

    private String formCreateTableQuery(String tableName, Map<String, List<String>> columns) {
        StringBuffer sb = new StringBuffer();
        String pattern = queryFormer.propertyBundle.getQuery(DbKeys.CREATE_TABLE);
        pattern = queryFormer.changeTableName(tableName, pattern);
        List<String> tableColumnList = queryFormer.tablesAndColumns.get(tableName);
        for (int i = 0; i < tableColumnList.size(); i++) {
            String columnName = tableColumnList.get(i);
            sb.append(formColumnQueryForCreateTable(tableName, columnName, columns.get(columnName)));
        }
        sb.append("PRIMARY KEY (" + queryFormer.getColumnId(tableName) + ")");
        pattern = pattern.replace("*columns*", sb.toString());
        return pattern;
    }

    private String formColumnQueryForCreateTable(String tableName, String columnName, List<String> columnParameters) {
        String autoIncremet = "";
        String columnId = queryFormer.getColumnId(tableName);
        boolean flag = columnName.equalsIgnoreCase(columnId);
        if (!GeneratorHandler.getInstance().isContainedTable(tableName) && flag) {
            autoIncremet = " AUTO_INCREMENT";
        }
        return columnName + " " + columnParameters.get(0) + " " + columnParameters.get(1) + autoIncremet + ",";
    }


    private Map<String, String> getActionQueriesForTable(Map<String, String> propertyBundlePatterns, String tableName) {
        Map<String, String> actionQueries = new HashMap<>();
        propertyBundlePatterns.forEach((action, queryPattern) -> {
            if (action.matches("\\w+")) {
                actionQueries.put(action, queryFormer.formQuery(tableName, action, queryPattern));
            }
        });
        logger.log(Level.INFO, "getActionQueriesForTable({0}) returns: {1}",
                new String[]{tableName, actionQueries.toString()});
        return actionQueries;
    }

    private String formQuery(String tableName, String action, String queryPattern) {
        String formedQuery = "";
        switch (action.toLowerCase().trim()) {
            case "create":
                formedQuery = queryFormer.formCreateQuery(tableName, queryPattern);
                break;
            case "read":
                formedQuery = queryFormer.formReadQuery(tableName, queryPattern);
                break;
            case "update":
                formedQuery = queryFormer.formUpdateQuery(tableName, queryPattern);
                break;
            case "delete":
                formedQuery = queryFormer.formDeleteQuery(tableName, queryPattern);
                break;
            default:
                formedQuery = queryFormer.formReadAllQuery(tableName, queryPattern);
                break;
        }
        logger.log(Level.INFO, "formQuery({0}, {1}, {2}) returns: {3}",
                new String[]{tableName, action, queryPattern, formedQuery});
        return formedQuery;
    }

    private String formCreateQuery(String tableName, String queryPattern) {
        String columnId = queryFormer.getColumnId(tableName);
        queryPattern = queryFormer.changeTableName(tableName, queryPattern);
        queryPattern = queryPattern.replace("*columns*", queryFormer.getColumns(tableName, columnId));
        queryPattern = queryPattern.replace("*values*", queryFormer.getValues(tableName, columnId));
        return queryPattern;
    }

    private String changeTableName(String tableName, String queryPattern) {

        return queryPattern.replace("*table*", tableName);
    }

    public String getColumnId(String tableName) {
        return queryFormer.tablesAndColumns.get(tableName).get(0);
    }

    private String getColumns(String tableName, String columnId) {
        return queryFormer.tablesAndColumns.get(tableName).stream()
                .reduce((s1, s2) -> changeCreateQueryIfBean(tableName, s1, s2)).get();
    }

    private String changeCreateQueryIfBean(String tableName, String s1, String s2) {
        String value = Handler.getBeanMap(tableName, s1);
        if (value != null) {
            s1 = value;
        }
        value = Handler.getBeanMap(tableName, s2);
        if (value != null) {
            s2 = value;
        }
        return s1 + ", " + s2;
    }

    private String getValues(String tableName, String columnId) {
        return getColumns(tableName, columnId).replaceAll("[\\w]+", "?");
    }

    private boolean isAnnotatedByGenerator(String tableName) {
        return GeneratorHandler.getInstance().isContainedTable(tableName);
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
        String id = queryFormer.tablesAndColumns.get(tableName).get(0);
        String columnValue = queryFormer.getColumnValue(tableName, id);
        queryPattern = queryPattern.replace("*set-block*", columnValue);
        queryPattern = queryPattern.replace("*condition*", queryFormer.getCondition(tableName));
        return queryPattern;
    }

    private String getColumnValue(String tableName, String id) {
        return queryFormer.tablesAndColumns.get(tableName).stream()
                .filter(s -> !s.equalsIgnoreCase(id))
                .reduce((s1, s2) -> changeUpdateQueryIfBean(tableName, s1, s2)).get().concat("=?");
    }

    private String changeUpdateQueryIfBean(String tableName, String s1, String s2) {
        String value = Handler.getBeanMap(tableName, s1);
        if (value != null) {
            s1 = value;
        }
        value = Handler.getBeanMap(tableName, s2);
        if (value != null) {
            s2 = value;
        }
        return s1 + "=?, " + s2;
    }

    private String formDeleteQuery(String tableName, String queryPattern) {
        queryPattern = queryFormer.changeTableName(tableName, queryPattern);
        queryPattern = queryFormer.changeTableName(tableName, queryPattern);
        queryPattern = queryPattern.replace("*condition*", queryFormer.getCondition(tableName));
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

    private String printInitialDatabaseConfiguration() {
        return queryFormer.initialDatabaseConfiguration.toString().replaceAll(";", ";\n");
    }
}