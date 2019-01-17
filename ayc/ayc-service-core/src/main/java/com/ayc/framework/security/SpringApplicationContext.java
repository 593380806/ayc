package com.ayc.framework.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
* @ClassName: SpringApplicationContext 
* @Description: Spring 上下文容器
 * @author ysj
 * @Data 2019-01-15
*
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getApplicationContext()
			throws BeansException {
		return context;
	}

	public static Object getBean(String beanId) {
		if ((beanId == null) || (beanId.length() == 0)) {
			return null;
		}
		Object object = null;
		object = context.getBean(beanId);
		return object;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	public static void removeBean(String beanId) {
		if ((beanId == null) || (beanId.isEmpty())) {
			return;
		}
		ConfigurableApplicationContext applicationContexts = (ConfigurableApplicationContext) context;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContexts
				.getBeanFactory();
		beanFactory.removeBeanDefinition(beanId);
	}

	public static void removeBeans(String[] beanIds) {
		if ((beanIds == null) || (beanIds.length == 0)) {
			return;
		}
		ConfigurableApplicationContext applicationContexts = (ConfigurableApplicationContext) context;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContexts
				.getBeanFactory();
		for (String beanId : beanIds)
			if ((beanId != null) && (!beanId.isEmpty())
					&& (beanFactory.isBeanNameInUse(beanId)))
				beanFactory.removeBeanDefinition(beanId);
	}
}