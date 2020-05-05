package com.practice.orm.annotation.entity.DBHandlers;

import java.util.Objects;
import java.util.Set;

public class TableDB {

    private Class<?> clazz;
    private boolean IsPKAutoIncrement;
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

    public boolean isPKAutoIncrement() {
        return IsPKAutoIncrement;
    }

    public void setPKAutoIncrement(boolean PKAutoIncrement) {
        IsPKAutoIncrement = PKAutoIncrement;
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

    public Set<ColumnDB> getColumnDBS() {
        return columnDBS;
    }

    public void setColumnDBS(Set<ColumnDB> columnDBS) {
        this.columnDBS = columnDBS;
    }

    public Set<ForeignKey> getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Set<ForeignKey> foreignKey) {
        this.foreignKey = foreignKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDB tableDB = (TableDB) o;
        return Objects.equals(clazz, tableDB.clazz) &&
                Objects.equals(TableName, tableDB.TableName) &&
                Objects.equals(primaryKey, tableDB.primaryKey) &&
                Objects.equals(columnDBS, tableDB.columnDBS) &&
                Objects.equals(foreignKey, tableDB.foreignKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, TableName, primaryKey, columnDBS, foreignKey);
    }
}
