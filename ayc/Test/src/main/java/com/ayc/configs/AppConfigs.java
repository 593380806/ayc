
package com.ayc.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Author:  ysj
 * Date:  2018/1/17 12:48
 * Description:数据源
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfigs {
    private Integer bizdbIndex;


    public Integer getBizdbIndex() {
        return bizdbIndex;
    }

    public void setBizdbIndex(Integer bizdbIndex) {
        this.bizdbIndex = bizdbIndex;
    }
}