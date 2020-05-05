package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.CreatorTables;
import com.practice.orm.annotation.entity.DBHandlers.ForeignKey;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Creator;
import com.practice.orm.annotation.entity.entityHandler.Handler;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        Creator.addAnnotatedClass(User.class);

        Creator.addAnnotatedClass(Book.class);
        Set<TableDB> tablesDB = Handler.getTablesDB();
        for (TableDB table :
                tablesDB) {
            System.out.println(table.getTableName());
        }
        System.out.println(tablesDB.size());

        Creator.build();
//        Set<TableDB> tablesDB = Handler.getTablesDB();
//        for (TableDB table :
//                tablesDB) {
//            System.out.println("Name Table:"+table.getTableName());
//            System.out.println("PK:"+table.getPrimaryKey().getName());
//            System.out.println("NullablePK:"+table.getPrimaryKey().getNullable());
//            System.out.println("LengthPK:"+table.getPrimaryKey().getLength());
//            System.out.println("TypePK:"+table.getPrimaryKey().getType());
//            System.out.println("CLass:"+table.getClazz().getSimpleName());
//            System.out.println();
//            for (ColumnDB columnDB:
//                 table.getColumnDBS()) {
//                System.out.println("NameOfColumn:"+columnDB.getName());
//                System.out.println("Nullable:"+columnDB.getNullable());
//                System.out.println("Length:"+columnDB.getLength());
//                System.out.println("Type:"+columnDB.getType());
//            }
//            for (ForeignKey foreignKey:
//                 table.getForeignKey()) {
//                System.out.println("NameOfColumnFK:"+foreignKey.getName());
//                System.out.println("NullableFK:"+foreignKey.getNullable());
//                System.out.println("LengthFK:"+foreignKey.getLength());
//                System.out.println("TypeFK:"+foreignKey.getType());
//                System.out.println("ToNameOfClassFK:"+foreignKey.getClazz());
//            }
//            System.out.println("_______________");
//        }
//        Set<TableDB> relationalTables = Handler.getRelationalTables();
//        for (TableDB table :
//                relationalTables) {
//            System.out.println("Name Table:"+table.getTableName());
//            System.out.println("PK:"+table.getPrimaryKey().getName());
//            System.out.println("NullablePK:"+table.getPrimaryKey().getNullable());
//            System.out.println("LengthPK:"+table.getPrimaryKey().getLength());
//            System.out.println("TypePK:"+table.getPrimaryKey().getType());
//            System.out.println();
//
//            for (ForeignKey foreignKey:
//                 table.getForeignKey()) {
//                System.out.println("NameOfColumnFK:"+foreignKey.getName());
//                System.out.println("NullableFK:"+foreignKey.getNullable());
//                System.out.println("LengthFK:"+foreignKey.getLength());
//                System.out.println("TypeFK:"+foreignKey.getType());
//                System.out.println("ToNameOfClassFK:"+foreignKey.getClazz());
//            }
//            System.out.println("_______________");
//            CreatorTables.generateTable(table);
////        }
//        }

//        Handler.addTable(new TableDB());
//        System.out.println(Handler.getTableDBS().size());
//
//        Handler.addTable(new TableDB());
//        System.out.println(Handler.getTableDBS().size());


    }
}
