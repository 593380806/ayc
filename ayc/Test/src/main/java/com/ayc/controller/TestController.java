
package com.ayc.controller;

import com.ayc.framework.common.AjaxResult;
import com.ayc.framework.common.BizException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:  ysj
 * Date:  2019/1/15 17:26
 * Description:
 */
@Controller
public class TestController {
    @PostMapping
    @ResponseBody
    public AjaxResult login(String mobile, String password) {
        try {
            return AjaxResult.success(null);
        } catch (BizException ex) {
            return AjaxResult.failed(ex.getCode());
        }
    }
}