package com.jiaochuan.hazakura.service;

import org.hibernate.dialect.MariaDB53Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMariaDB53Dialect extends MariaDB53Dialect {
    private static final Logger LOG = LoggerFactory.getLogger(CustomMariaDB53Dialect.class);

    public CustomMariaDB53Dialect() {
        super();
        registerFunction("convertEncode", new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using ?2)"));
    }
}
