package com.practice.orm.annotation.entity.DBHandlers;

import java.util.Set;

public class TableDB {

    private Set<ColumnForDB> columnForDBS;
    private String TableName;
    private ColumnForDB primaryKey;
    private ColumnForDB foreignKey;



    public ColumnForDB getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(ColumnForDB foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Set<ColumnForDB> getColumnForDBS() {
        return columnForDBS;
    }

    public void setColumnForDBS(Set<ColumnForDB> columnForDBS) {
        this.columnForDBS = columnForDBS;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public ColumnForDB getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnForDB primaryKey) {
        this.primaryKey = primaryKey;
    }

}
