package com.gchr.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * ���л������ࣨ���� Protostuff ʵ�֣�
 * @author gongchunru
 *
 */
public class SerializationUtil {
	
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
	
	private static Objenesis objenesis = new ObjenesisStd(true);

	/**
	 * ��ȡ���schema
	 * @param cls
	 * @return
	 */
	private static <T> Schema<T> getSchema(Class<T> cls){
		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
		if(schema == null) {
			schema = RuntimeSchema.createFrom(cls);
			if(schema != null) {
				cachedSchema.put(cls, schema);
			}
		}
		return schema;
	}
	
	/**
	 * ���л�������--> �ֽ����飩
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T obj) {
		Class<T> cls = (Class<T>)obj.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		
		try {
			Schema<T> schema = getSchema(cls);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}
	
	/**
	 * �����л� (�ֽ�����-> ����)
	 * @param data
	 * @param cls
	 * @return
	 */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		
		try {
			
			/*
	    	 * ���һ����û�в���Ϊ�յĹ��췽��ʱ����ô��ֱ�ӵ���newInstance������ͼ�õ�һ��ʵ�������ʱ���ǻ��׳��쳣��
	    	 * ͨ��ObjenesisStd���������ıܿ��������
	    	 * */
			T message = (T) objenesis.newInstance(cls);
			Schema<T> schema = getSchema(cls);// ��ȡ��ǰ���schema
			ProtostuffIOUtil.mergeFrom(data, message, schema);
			return message;
		}catch (Exception e){
			throw new IllegalStateException(e.getMessage(),e);
		}
	}
	
}






















