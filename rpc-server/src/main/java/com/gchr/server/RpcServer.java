package com.gchr.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *  RPC 请求注解（标注在服务实现类上）
 * @author gongchunru
 *
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		
	}
	
	
}
