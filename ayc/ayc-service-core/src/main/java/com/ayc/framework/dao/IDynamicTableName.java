package com.ayc.framework.dao;



import com.ayc.framework.util.ModUtils;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;

public interface IDynamicTableName extends tk.mybatis.mapper.entity.IDynamicTableName {

	@Override
	@Transient
	default String getDynamicTableName() {
		StringBuilder tableName = new StringBuilder(this.getClass().getAnnotation(Table.class).name());
		Field[] fields = this.getClass().getDeclaredFields();
		int count = fields.length;
		for (int i = 0; i < count; i++) {
			if (fields[i].isAnnotationPresent(TableIndex.class) && fields[i].getAnnotation(TableIndex.class).value() > 1) {
				try {
					fields[i].setAccessible(true);
					Object obj = fields[i].get(this);
					String index = ModUtils.mod(obj.hashCode(), fields[i].getAnnotation(TableIndex.class).value());
					tableName = tableName.append("_").append(index);
					return tableName.toString();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		return tableName.toString();
	}

}
