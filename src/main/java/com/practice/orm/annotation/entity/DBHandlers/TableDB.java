package com.practice.orm.annotation.entity.DBHandlers;

import java.util.Set;

public class TableDB {

    private Class<?> clazz;
    private String TableName;
    private ColumnDB primaryKey;
    private Set<ColumnDB> columnDBS;
    private Set<ForeignKey> foreignKey;


    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Set<ForeignKey> getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Set<ForeignKey> foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Set<ColumnDB> getColumnDBS() {
        return columnDBS;
    }

    public void setColumnDBS(Set<ColumnDB> columnDBS) {
        this.columnDBS = columnDBS;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public ColumnDB getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnDB primaryKey) {
        this.primaryKey = primaryKey;
    }

}
