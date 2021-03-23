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

    static void checkUsername(String username) throws UserException {
        if (username == null || username.equals("")) {
            throw new UserException("用户名不能为空！");
        }
        if (username.length() > 16) {
            throw new UserException("用户名长度不能大于16个字符！");
        }
    }

    static void checkPassword(String password) throws UserException {
        if (password == null) {
            throw new UserException("密码不能为空！");
        }
        if (!StringUtils.isAlphanumericSpace(password)) {
            throw new UserException("密码中存在特殊字符，请检查输入。");
        }
        if (password.length() < 8) {
            throw new UserException("密码长度不能小于8位字符。");
        }
        if (password.length() > 16) {
            throw new UserException("密码长度不能大于16位字符。");
        }
    }

    static void checkCell(String cell) throws UserException {
        if (cell == null || cell.length() != 11) {
            throw new UserException("手机号码长度不能多于或少于11位。");
        }
    }

    static void checkEmail(String email) throws UserException {
        if (email == null || email.equals("") || !email.contains("@")) {
            throw new UserException("电子邮箱格式不正确。");
        }
        if (email.length() > 64) {
            throw new UserException("电子邮箱长度不能大于64个字符！");
        }
    }
}
