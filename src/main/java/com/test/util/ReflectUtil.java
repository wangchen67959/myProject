package com.test.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {

	/**
	 * 
	 * @param clazz 指定对象
	 * @param argTypes 参数类型
	 * @param args 构造参数
	 * @return 返回构造后的对象
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> T invokeConstructor(Class<T> clazz, 
			Class<?>[] argTypes, Object[] args) throws NoSuchMethodException, 
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> constructor = clazz.getConstructor(argTypes);
		return constructor.newInstance(args);
	}
	
	/**
	 * 反射调用指定对象的setter方法
	 * @param target 指定对象
	 * @param fieldName 属性名
	 * @param args 参数
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> void invokeSetter(T target, String fieldName, Object args)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String methodName = "set" + StringUtil.firstCharUpper(fieldName);
		Class<?> clazz = target.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		Method method = clazz.getMethod(methodName, field.getType());
		method.invoke(target, args);
	}
	
	/**
	 * 反射调用指定对象的getter方法
	 * @param target 指定对象
	 * @param fieldName 属性名
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Object invokeGetter(T target, String fieldName) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// 限于xxx
		String methodName = "get" + StringUtil.firstCharUpper(fieldName);
		Class<?> clazz = target.getClass();
		Method method = clazz.getMethod(methodName);
		return method.invoke(method);
	}
	
}
