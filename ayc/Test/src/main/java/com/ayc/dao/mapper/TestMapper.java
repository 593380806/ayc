
package com.ayc.dao.mapper;

import com.ayc.entity.TestEntity;
import com.ayc.framework.dao.IBaseMapper;

/**
 * Author:  ysj
 * Date:  2019/1/17 12:46
 * Description:
 */
public interface TestMapper extends IBaseMapper{
    TestEntity queyTest();
}