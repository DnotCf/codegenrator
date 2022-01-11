package com.cmsiw.update;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author tangs
 * @date 2019/9/3 14:56
 */
@Component
public class GlobEntity implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("operateTime", LocalDateTime.now());
        metaObject.setValue("operator", "tangs");
        metaObject.setValue("operateIp", "127.0.0.1");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("operateTime", LocalDateTime.now());
        metaObject.setValue("operator", "tangs");
        metaObject.setValue("operateIp", "127.0.0.1");
    }
}
