 package com.ayc.framework.datasource.impl.rules;


 import com.ayc.framework.datasource.DataSourceKeyRule;

 /**
  * 取模路由规则
  * @author ysj
  *
  */
 public class DataSourceKeyModuloRule extends ADataSourceKeyRule
   implements DataSourceKeyRule
 {
   private Long moduloNumber;
   private Long remainder;
 
   public Long getModuloNumber()
   {
      return this.moduloNumber;
   }
   public void setModuloNumber(Long moduloNumber) {
      this.moduloNumber = moduloNumber;
   }
   public Long getRemainder() {
      return this.remainder;
   }
   public void setRemainder(Long remainder) {
     this.remainder = remainder;
   }
   public boolean applyRule(String field, Long value) {
      return applyRule(field, value, false);
   }
 
   public boolean applyRule(String field, Long value, boolean readonly) {
      if ((field == null) || (value == null)) {
       return false;
     }
      if (field.compareTo(getField()) != 0) {
       return false;
     }
      if ((!isReadonly()) && (readonly)) {
        return false;
     }
     if ((isReadonly()) && (!readonly)) {
       return false;
     }
     if (value.longValue() % this.moduloNumber.longValue() == this.remainder.longValue()) {
       return true;
     }
     return false;
   }
 }
