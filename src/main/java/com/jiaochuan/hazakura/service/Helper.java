package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import java.util.Set;

class Helper {

    private Helper() {}

    static void checkFields(Class<?> clazz, Object entity) throws AppException, UserException {
        checkFields(clazz, entity, null);
    }

    static void checkFields(Class<?> clazz, Object entity, Set<String> mandatoryFieldsSet)
            throws AppException, UserException {
        if (entity.getClass() != clazz) {
            throw new AppException("内部错误，请联系系统管理员：函数参数中对象的类跟传进来的类不匹配。");
        }

        String className = clazz.getSimpleName();

        ReflectionUtils.doWithFields(clazz, (field) -> {
            if (!field.trySetAccessible()) {
                throw new IllegalAccessException(String.format("创建用户时无法取得%s的反射访问权限，其成员变量无法通过反射访问。", className));
            }

            boolean check = false;
            if (mandatoryFieldsSet != null && mandatoryFieldsSet.contains(field.getName())) {
                check = true;
            }

            Column col = field.getAnnotation(Column.class);
            check |= (col != null && !col.nullable());
            if (check && ((field.getType() == String.class && StringUtils.isBlank((String) field.get(entity))) || field.get(entity) == null)) {
                throw new IllegalArgumentException(String.format("必填项%s不能为空。", field.getName()));
            }
        });
    }

}
