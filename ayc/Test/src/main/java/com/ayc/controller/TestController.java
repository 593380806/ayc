
package com.ayc.controller;

import com.ayc.configs.AppConfigs;
import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizException;
import com.ayc.service.TestSevice;
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
    @Autowired
    private TestSevice testSevice;
    @Autowired
    private AppConfigs appConfigs;
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult login(String mobile, String password) {
        try {
            return AjaxResult.success(testSevice.reTest(appConfigs.getBizdbIndex()));
        } catch (BizException ex) {
            return AjaxResult.failed(ex.getCode());
        }
    }
}