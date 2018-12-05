package com.workbei.dao.user;

import org.apache.ibatis.annotations.Param;

/**
 * @author Wallace Mao
 * Date: 2018-12-05 16:01
 */
public interface BaseCrudDao<T, PK> {

    void saveOrUpdate(T object);

    void deleteById(@Param("id") PK id);

    T getById(@Param("id") PK id);
}
