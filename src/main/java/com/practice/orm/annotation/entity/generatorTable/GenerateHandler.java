package com.practice.orm.annotation.entity.generatorTable;

import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.ForeignKey;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

final public class GenerateHandler {

    private static final String sql = "CREATE TABLE IF NOT EXISTS ";
    private static final String addRelation = "ALTER TABLE ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static DBUtil dbUtil;
    private static Connection connection = null;
    private static final Statement statement = null;

    static {
        try {
            dbUtil = DBUtil.getInstance(new PropertyBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateTables(Set<TableDB> tablesDB) throws Exception {
        List<String> strings = new ArrayList<>();
        for (TableDB tableDB :
                tablesDB) {
            strings.add(createTableQuery(tableDB));
        }
        createQuery(strings);
    }

    public static void generateRelation(Set<TableDB> tableDBS) throws Exception {
        List<String> strings = new ArrayList<>();
        for (TableDB tableDB :
                tableDBS) {
            if (!tableDB.getForeignKey().isEmpty()) {
                String[] queries = getQueryRelation(tableDB.getForeignKey(), tableDB.getTableName()).split(";");
                Arrays.stream(queries).forEach(s -> {
                    strings.add(s + ";");
                });
            }
        }
        createQuery(strings);
    }

    public static void generateForeignKeys(Set<TableDB> tableDBS) {
        List<String> strings = new ArrayList<>();
        for (TableDB tableDB :
                tableDBS
        ) {
            if (!tableDB.getForeignKey().isEmpty()) {
                String[] queries = getForeignKeys(tableDB.getForeignKey()).split(";");
                Arrays.stream(queries).forEach(s ->
                {
                    strings.add(s + ";");
                });
            }
        }
        createQuery(strings);
    }

    public static void dropTables(Set<TableDB> tableDBS) {
        List<String> strings = new ArrayList<>();
        for (TableDB tableDB : tableDBS) {
            strings.add(getQueryDrop(tableDB));
        }
        createQuery(strings);
    }

    private static void createQuery(List<String> strings) {
        try {
            connection = dbUtil.getConnectionFromPool();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            for (String s :
                    strings) {
                statement.executeUpdate(s);
            }
            connection.setAutoCommit(true);
            statement.close();
            dbUtil.returnConnectionToPool(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getForeignKeys(Set<ForeignKey> foreignKeys) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ForeignKey foreignKey :
                foreignKeys) {
            stringBuilder.append(generateFK(foreignKey));
        }
        return stringBuilder.toString();
    }

    private static String generateFK(ForeignKey foreignKey) {
        StringBuilder stringBuilder = new StringBuilder(addRelation);
        stringBuilder.append(foreignKey.getNameTableFrom());
        stringBuilder.append(" ADD COLUMN ");
        stringBuilder.append(foreignKey.getName() + " ");
        stringBuilder.append(foreignKey.getType());
        if (foreignKey.getType() == "VARCHAR")
            stringBuilder.append("(" + foreignKey.getLength() + ")");
        if (!foreignKey.getNullable())
            stringBuilder.append(NOT_NULL);
        stringBuilder.append(";");
        return stringBuilder.toString();
    }

    private static String getQueryRelation(Set<ForeignKey> foreignKeys, String tableDB) throws Exception {
        StringBuilder strings = new StringBuilder();
        for (ForeignKey foreignKey :
                foreignKeys) {
            strings.append(generatorForeignKeys(foreignKey, tableDB));
        }
        return strings.toString();
    }

    private static String getFieldPrimaryKeyToDb(ColumnDB columnDB) {
        StringBuilder stringBuilder = new StringBuilder("PRIMARY KEY(");
        stringBuilder.append(columnDB.getName());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static String createTableQuery(TableDB tableDB) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(tableDB.getTableName() + "(");
        stringBuilder.append(generatorBDFields(tableDB.getPrimaryKey()));
        if (tableDB.getColumnDBS() != null) {
            for (ColumnDB c :
                    tableDB.getColumnDBS()) {
                stringBuilder.append(generatorBDFields(c));
            }
        }
        stringBuilder.append(getFieldPrimaryKeyToDb(tableDB.getPrimaryKey()));
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    private static String generatorForeignKeys(ForeignKey foreignKey, String tableName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(addRelation);
        stringBuilder.append(foreignKey.getNameTableFrom());
        stringBuilder.append(" ADD ");
        stringBuilder.append(" FOREIGN KEY (");
        stringBuilder.append(foreignKey.getName());
        stringBuilder.append(") REFERENCES ");
        stringBuilder.append(foreignKey.getNameTableTo());
        stringBuilder.append("(");
        stringBuilder.append(foreignKey.getNameColumnTo());
        stringBuilder.append(") ON UPDATE CASCADE ON DELETE CASCADE;");
        return stringBuilder.toString();
    }

    private static String generatorColumnByForeignKey(ForeignKey foreignKey) {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(foreignKey.getNameTableFrom() + " ");
        stringBuilder.append("ADD COLUMN ");
        stringBuilder.append(foreignKey.getName() + " ");
        stringBuilder.append(foreignKey.getType());
        if (foreignKey.getType() == "VARCHAR")
            stringBuilder.append("(" + foreignKey.getLength() + ")");
        if (!foreignKey.getNullable())
            stringBuilder.append(" NOT NULL ");
        return stringBuilder.toString();
    }

    private static String generatorBDFields(ColumnDB columnDB) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(columnDB.getName() + " ");
        stringBuilder.append(columnDB.getType());
        if (columnDB.getType() == "VARCHAR")
            stringBuilder.append("(" + columnDB.getLength() + ")");
        if (!columnDB.getNullable())
            stringBuilder.append(NOT_NULL);
        stringBuilder.append(",");
        return stringBuilder.toString();
    }

    private static String getQueryDrop(TableDB tableDB) {
        StringBuilder stringBuilder = new StringBuilder(DROP_TABLE);
        stringBuilder.append(tableDB.getTableName());
        stringBuilder.append(" cascade;");
        return stringBuilder.toString();
    }
}
