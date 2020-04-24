package com.practice.orm.db.utilDao.entiry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryFormer {
    public static QueryFormer queryFormer;
    private Map<String, Map<String, String>>  tableQueries;
    private PropertyBundle propertyBundle;
    private Map<String, List<String>> tablesAndColumns;

    private QueryFormer() {
        this.tableQueries = new HashMap<>();
    }

    public static QueryFormer getInstance() {
        if (queryFormer == null) {
            queryFormer = new QueryFormer();
        }
        return queryFormer;
    }

    public void initializeTableQueries(Map<String, List<String>> tablesAndColumns) {
        this.tablesAndColumns = tablesAndColumns;
        queryFormer.tableQueries = tablesAndColumns.keySet().stream()
                .collect(Collectors.toMap(s -> s, s -> new HashMap<>()));
    }

    public void setPropertyBundle(PropertyBundle propertyBundle) {
        queryFormer.propertyBundle = propertyBundle;
    }

    public void formQueriesForAllTables() {
        Map<String, String> propertyBundlePatterns = queryFormer.propertyBundle.getQueriesInMap();
//        queryFormer.tableQueries.forEach(
//                (table,actionMap) -> actionMap.forEach(
//
//                )
//        );
    }

    private Map<String,String> getActionQueries(Map<String,String> propertyBundlePatterns, String tableName) {
        Map<String, String> actionQueries = new HashMap<>();
        for (String key : propertyBundlePatterns.keySet()) {
            String query = propertyBundlePatterns.get(key);
        }
     }



}
