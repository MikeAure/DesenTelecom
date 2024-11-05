package com.lu.gademo.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class DesenResultConverter<T> {
    private InstanceFactory<T> factory;

    public DesenResultConverter(InstanceFactory<T> factory) {
        this.factory = factory;
    }

    public List<T> getDesenResult(Map<String, List<Object>> desenData, String idColumnName) throws IllegalAccessException {
        List<T> result = new ArrayList<>();
        log.info("DesenDataList size: {}", desenData.get(idColumnName).size());
        for (int i = 0; i < desenData.get(idColumnName).size(); i++) {
            T element = factory.create();
            Field[] declaredFields = element.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                Object data = desenData.get(field.getName()).get(i);

                field.setAccessible(true);

                if (data == null) {
                    field.set(element, null);
                } else {
                    if (field.getType() == Double.class) {
                        Double value = Double.parseDouble(data.toString());
                        field.set(element, value);
                    } else if (field.getType() == Date.class) {
                        Date value = (Date) data;
                        field.set(element, value);
                    } else if (field.getType() == Long.class) {
                        Long value = Long.parseLong(data.toString());
                        field.set(element, value);
                    } else if (field.getType() == Integer.class) {
                        Integer value = Integer.parseInt(data.toString());
                        field.set(element, value);
                    } else {
                        String value = (String) data;
                        field.set(element, value);
                    }
                }
            }
            result.add(element);
        }
        return result;
    }
}
