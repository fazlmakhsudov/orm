package com.practice.orm.crud.dispatcher;

import com.practice.orm.annotation.entity.entityHandler.Handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderForCrud<C> {
    private final C object;
    private Map<Integer, List<Object>> orderMap;

    public OrderForCrud(C object) {
        this.object = object;
        this.orderMap = new HashMap<>();
    }

    public Map<Integer, List<Object>> getOrderMap() throws IllegalAccessException {
        orderMap = this.scan(object, 0, orderMap);
        return orderMap;
    }

    private Map<Integer, List<Object>> scan(Object object, int layer,
                                            Map<Integer, List<Object>> orderMap) throws IllegalAccessException {
        String tableName = Handler.getNameTable(object.getClass());
        List<Object> parentOfCurrentLayer = new ArrayList<>();
        parentOfCurrentLayer.add(object);
        if (Handler.hasBeanInside(tableName)) {
            List<Object> fieldList = getBeans(object, tableName);
            addBeansToCertainLayer(fieldList, layer, orderMap);
            fieldList.stream().filter(fieldObj -> !Handler.isBean(fieldObj.getClass())).forEach(
                    fieldObj -> {
                        try {
                            parentOfCurrentLayer.add(fieldObj);
                            scan(fieldObj, (layer + 1), orderMap);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        orderMap.put((layer + 1), parentOfCurrentLayer);
        return orderMap;
    }

    private Map<Integer, List<Object>> addBeansToCertainLayer(List<Object> fieldList, int layer,
                                                              Map<Integer, List<Object>> orderMap) {
        List<Object> nextLayerOfOrder = fieldList.stream().filter(field -> {
            return Handler.isBean(field.getClass());
        }).collect(Collectors.toList());
        if (orderMap.containsKey(layer)) {
            List<Object> currentLayer = orderMap.get(layer);
            currentLayer.addAll(nextLayerOfOrder);
        } else {
            orderMap.put(layer, nextLayerOfOrder);
        }
        return orderMap;
    }

    private List<Object> getBeans(Object object, String tableName) throws IllegalAccessException {
        List<Object> listOfBeans = new ArrayList<>();
        Map<String, String> mapOfFields = Handler.getBeanMap(tableName);
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (mapOfFields.containsKey(fieldName)) {
                listOfBeans.add(field.get(object));
            }
        }
        return listOfBeans;
    }
}
