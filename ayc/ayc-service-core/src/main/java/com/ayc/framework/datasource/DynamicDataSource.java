 package com.ayc.framework.datasource;

 import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;

 /**
  * 自定义实现spring dynamic datasource 
  * @author ysj
  *
  */
 public class DynamicDataSource extends AbstractRoutingDataSource
 {
    private List<String> dataSourceValus = new ArrayList<String>();
 
   protected Object determineCurrentLookupKey()
   {
     return DynamicDataSourceHolder.getDataSouce();
   }
 
   public void setTargetDataSources(Map<Object, Object> targetDataSources)
   {
      super.setTargetDataSources(targetDataSources);
     if (targetDataSources != null)
       for (Map.Entry<Object, Object> entry : targetDataSources.entrySet())
         if ((entry.getValue() != null) && ((entry.getValue() instanceof String)))
          this.dataSourceValus.add((String)entry.getValue());
   }
 
   public List<String> getDataSourceValues()
   {
     return this.dataSourceValus;
   }
 }
