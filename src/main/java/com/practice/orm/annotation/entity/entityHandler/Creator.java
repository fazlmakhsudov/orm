package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.DBHandlers.CreatorTables;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.entityHandler.exceptions.NotFoundAnnotatedClasses;

import java.util.List;

public class Creator {

    public static void addAnnotatedClass(Class<?> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new Exception("Not found Entity annotation");
        } else {
            Handler.addClass(clazz);
        }
    }

    public static List<Class> getClasses() {
        return Handler.getClasses();
    }

    public static void build() throws NotFoundAnnotatedClasses {
        if (Handler.getTablesDB() != null) {
            CreatorTables.generateTables(
                    Handler.getTablesDB());
        } else {
            throw new NotFoundAnnotatedClasses("Not found annotated classes");
        }
    }
}
