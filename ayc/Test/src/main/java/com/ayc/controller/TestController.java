
package com.ayc.controller;

import com.alibaba.fastjson.JSON;
import com.ayc.configs.AppConfigs;
import com.ayc.entity.TestEntity;
import com.ayc.framework.aspect.ValidateFiled;
import com.ayc.framework.aspect.ValidateGroup;
import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizCode;
import com.ayc.framework.common.BizException;
import com.ayc.service.TestSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:  ysj
 * Date:  2019/1/15 17:26
 * Description:
 */
@Controller
@RequestMapping("/test")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TestSevice testSevice;
    @Autowired
    private AppConfigs appConfigs;
    @GetMapping("/list")
    @ResponseBody
    @ValidateGroup(fileds = { @ValidateFiled(index = 0, notNull = true, icode = BizCode.CLIENT_ERROR, message = "版本号"),
            @ValidateFiled(index = 1, notNull = true, icode = BizCode.CLIENT_ERROR, message = "电话"),
            @ValidateFiled(index = 2, notNull = true, icode = BizCode.CLIENT_ERROR, message = "密码"),
             })
    //@ValidateFiled(index = 3, notNull = true, icode = BizCode.CLIENT_ERROR, message = "表单"),
    //@ValidateFiled(index = 3, filedName = "id", notNull = true, icode = BizCode.CLIENT_ERROR, message = "id"),
    public AjaxResult login(String mobile, String password/* ,TestEntity t */) {
        long startTime = System.currentTimeMillis();
        logger.info("接口调用开始,参数:[mobile:{},password:{}]", mobile, password);
        TestEntity testEntity = null;
        try {
            testEntity = testSevice.reTest(appConfigs.getBizdbIndex());
        } catch (BizException e) {
            long endTime = System.currentTimeMillis();
            logger.error("接口调用异常!耗时:{},异常信息:{}", (endTime - startTime), e.getMessage(), e);
            return AjaxResult.failed();
        } catch (Exception e) {
            // 记录异常信息
            long endTime = System.currentTimeMillis();
            logger.error("接口调用异常!耗时:{},异常信息:{}", (endTime - startTime), e.getMessage(), e);
            return AjaxResult.failed();
        }
        long endTime = System.currentTimeMillis();
        logger.info("接口调用完成,耗时:{},返回结果:[testEntity:{}]", (endTime - startTime), testEntity);
        return AjaxResult.success(testEntity);
    }
}