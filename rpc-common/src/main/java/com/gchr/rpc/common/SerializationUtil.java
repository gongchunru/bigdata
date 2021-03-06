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
 * 序列化工具类（基于 Protostuff 实现）
 * @author gongchunru
 *
 */
public class SerializationUtil {
	
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
	
	private static Objenesis objenesis = new ObjenesisStd(true);

	/**
	 * 获取类的schema
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
	 * 序列化（对象--> 字节数组）
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
	 * 反序列化 (字节数组-> 对象)
	 * @param data
	 * @param cls
	 * @return
	 */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
		
		try {
			
			/*
	    	 * 如果一个类没有参数为空的构造方法时候，那么你直接调用newInstance方法试图得到一个实例对象的时候是会抛出异常的
	    	 * 通过ObjenesisStd可以完美的避开这个问题
	    	 * */
			T message = (T) objenesis.newInstance(cls);
			Schema<T> schema = getSchema(cls);// 获取当前类的schema
			ProtostuffIOUtil.mergeFrom(data, message, schema);
			return message;
		}catch (Exception e){
			throw new IllegalStateException(e.getMessage(),e);
		}
	}
	
}






















