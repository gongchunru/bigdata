package com.gchr.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target({ ElementType.TYPE})// ע���ڽӿ���
@Retention(RetentionPolicy.RUNTIME)//VM����������Ҳ����ע�ͣ���˿���ͨ��������ƶ�ȡע�����Ϣ
@Component
public @interface RpcService {
	Class<?> value();
}
