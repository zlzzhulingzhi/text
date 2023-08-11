/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.qbs.wa.teach.config.mybatisplus;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


/**
 * 通用填充类 适用于mybatis plus
 *
 * @author Caratacus
 */
public class CommonMetaObjectHandler implements MetaObjectHandler {

    /**
     * 创建时间
     */
    private final String CREATE_TIME = "createTime";

    /**
     * 修改时间
     */
    private final String UPDATE_TIME = "updateTime";

    /**
     * 创建者ID
     */
    private final String CREATE_BY = "createBy";

    /**
     * 修改者ID
     */
    private final String UPDATE_BY = "updateBy";


    @Override
    public void insertFill(MetaObject metaObject) {
        Date nowDate = new Date();
        Long userId = SecurityContextHolder.getUserId();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        if (metaObject.hasSetter(CREATE_TIME) && metaObject.getValue(CREATE_TIME) == null) {
            if (metaObject.getSetterType(CREATE_TIME) == LocalDateTime.class) {
                this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, nowLocalDateTime);
            } else {
                this.strictInsertFill(metaObject, CREATE_TIME, Date.class, nowDate);
            }
        }
        if (metaObject.hasSetter(UPDATE_TIME) && metaObject.getValue(UPDATE_TIME) == null) {
            if (metaObject.getSetterType(UPDATE_TIME) == LocalDateTime.class) {
                this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, nowLocalDateTime);
            } else {
                this.strictInsertFill(metaObject, UPDATE_TIME, Date.class, nowDate);
            }
        }

        if (metaObject.hasSetter(CREATE_BY) && metaObject.hasGetter(CREATE_BY) && metaObject.getValue(CREATE_BY) == null) {
            //todo
            //setInsertFieldValByName(createBy, UserContext.getUserId(), metaObject);
            this.strictInsertFill(metaObject, CREATE_BY, Long.class, userId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = SecurityContextHolder.getUserId();
        if (metaObject.hasSetter(UPDATE_TIME) && metaObject.hasGetter(UPDATE_TIME)) {
            if (metaObject.getSetterType(UPDATE_TIME) == LocalDateTime.class) {
                this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
            } else {
                this.strictUpdateFill(metaObject, UPDATE_TIME, Date.class, new Date());
            }
        } else if (metaObject.hasGetter(Constants.ENTITY)) {
            Optional.ofNullable(metaObject.getValue(Constants.ENTITY)).ifPresent(obj -> {
                MetaObject etMeta = SystemMetaObject.forObject(obj);
                if (etMeta.hasSetter(UPDATE_TIME) && etMeta.hasGetter(UPDATE_TIME)) {
                    if (etMeta.getSetterType(UPDATE_TIME) == LocalDateTime.class) {
                        this.strictUpdateFill(etMeta, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
                    } else {
                        this.strictUpdateFill(etMeta, UPDATE_TIME, Date.class, new Date());
                    }
                }
            });
        }

        if (metaObject.hasSetter(UPDATE_BY) && metaObject.hasGetter(UPDATE_BY) && metaObject.getValue(UPDATE_BY) == null) {
            //todo
            //setUpdateFieldValByName(updateBy, UserContext.getUserId(), metaObject);
            this.strictUpdateFill(metaObject, UPDATE_BY, Long.class, userId);
        }
    }


}
