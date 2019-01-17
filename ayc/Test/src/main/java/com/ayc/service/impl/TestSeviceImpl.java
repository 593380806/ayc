
package com.ayc.service.impl;

import com.ayc.dao.mapper.TestMapper;
import com.ayc.entity.TestEntity;
import com.ayc.framework.datasource.annotation.DataSource;
import com.ayc.service.TestSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:  ysj
 * Date:  2019/1/15 17:44
 * Description:
 */
@Service("testSevice")
public class TestSeviceImpl implements TestSevice{
    @Autowired
    private TestMapper testMapper;
    @Override
    public TestEntity reTest(@DataSource(field = "rCashId") Integer rCashId) {
        TestEntity testEntity = testMapper.queyTest();
        return testEntity;
    }
}