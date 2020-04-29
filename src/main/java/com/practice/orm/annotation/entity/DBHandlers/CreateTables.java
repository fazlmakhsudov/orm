package com.practice.orm.annotation.entity.DBHandlers;

import com.mongodb.DB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateTables {
    private static final String sql = "CREATE TABLE IF NOT EXISTS";
    private static DBUtil dbUtil;

    static {
        try {
            dbUtil = DBUtil.getInstance(new PropertyBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection connection = null;
    private static Statement statement = null;


    public static void generateTables(Set<TableDB> tablesDB) {
        List<String> tablesQuery = new ArrayList<>();
        for (TableDB tableDB :
                tablesDB) {
            tablesQuery.add(createTableQuery(tableDB));
        }
        try {
            connection = dbUtil.getConnectionFromPool();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            for (String s :
                    tablesQuery) {
                statement.executeUpdate(s);
            }
            connection.setAutoCommit(true);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateTable(Class<?> clazz) {
        try {
            connection = dbUtil.getConnectionFromPool();
            TableDB tableDB = Handler.getTableDB(clazz);
            Statement statement = connection.createStatement();
            System.out.println(statement.executeUpdate(createTableQuery(tableDB)));
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getFieldPrimaryKeyToDb(ColumnForDB columnForDB) {
        StringBuilder stringBuilder = new StringBuilder("PRIMARY KEY(");
        stringBuilder.append(columnForDB.getName());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static String createTableQuery(TableDB tableDB) {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" "+tableDB.getTableName() + "(");
        stringBuilder.append(generatorBDFields(tableDB.getPrimaryKey()));
        for (ColumnForDB c :
                tableDB.getColumnForDBS()) {
            stringBuilder.append(generatorBDFields(c));
        }
        stringBuilder.append(getFieldPrimaryKeyToDb(tableDB.getPrimaryKey()));
        stringBuilder.append(");");
        return stringBuilder.toString();
    }


    private static String generatorBDFields(ColumnForDB columnForDB) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(columnForDB.getName() + " ");
        stringBuilder.append(columnForDB.getType());
        if (columnForDB.getType() == "VARCHAR")
            stringBuilder.append("(" + columnForDB.getLength() + ")");
        if (!columnForDB.getNullable())
            stringBuilder.append(" NOT NULL ");
        stringBuilder.append(",");
        return stringBuilder.toString();
    }
}
