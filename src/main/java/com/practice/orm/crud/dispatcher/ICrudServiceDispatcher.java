package com.practice.orm.crud.dispatcher;

import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.generatorTable.Creator;
import com.practice.orm.annotation.generator.GeneratorHandler;
import com.practice.orm.crud.service.ICrudService;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public interface ICrudServiceDispatcher<C> {
    List<Object> addedObject = new ArrayList<>();

    static void initializeCrudSystem(Class... classes) {
        Arrays.stream(classes).forEach(cl -> {
            try {
                Creator.addAnnotatedClass(cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            Creator.build();
            List<Class> annotatedClasses = Arrays.asList(classes);
            GeneratorHandler.getInstance().setAnnotatedClasses(annotatedClasses);
            GeneratorHandler.getInstance().buildTablesCounterGenerator();
            PropertyBundle propertyBundle = new PropertyBundle();
            QueryFormer queryFormer = QueryFormer.getInstance();
            queryFormer.setPropertyBundle(propertyBundle);
            queryFormer.setTablesAndColumns(Handler.getTable());
            queryFormer.formQueriesForAllTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static <C> void create(C object) {
        OrderForCrud orderForCrud = new OrderForCrud(object);
        try {
            Map<Integer, List<Object>> layersOfCrud = orderForCrud.getOrderMap();
            for (List<Object> layerOfCrud : layersOfCrud.values()) {
                listToCrudService(layerOfCrud);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static <C> C read(Object id, Class cl) {
        return ICrudService.read(id, cl);
    }

    static <C> boolean update(Object id, C object) {
        return ICrudService.update(id, object);
    }

    static <C> boolean delete(Object id, Class cl) {
        return ICrudService.delete(id, cl);
    }

    static <C> List<C> readAll(Class cl) {
        return ICrudService.readAll(cl);
    }

    static boolean remarkAddedObject(Object obj) {
        boolean flag = false;
        if (!addedObject.contains(obj)) {
            addedObject.add(obj);
            flag = true;
        }
        return flag;
    }

    private static void listToCrudService(List<Object> objects) {
        for (Object obj : objects) {
            if (!addedObject.contains(obj)) {
                ICrudService.create(obj);
            }
        }
    }

}
