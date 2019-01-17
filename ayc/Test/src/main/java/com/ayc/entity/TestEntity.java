
package com.ayc.entity;

import java.io.Serializable;
/**
 * Author:  ysj
 * Date:  2019/1/15 17:43
 * Description:
 */
public class TestEntity implements Serializable{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}