package com.techjar.vivecraftforge.util;

import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.network.play.ServerPlayNetHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionHelper {
	public static ReflectionField GoalSelector_goals = new ReflectionField(GoalSelector.class, "field_220892_d", "d", "goals");

	public static Class reflectClass(String name) {
		try {
			return Class.forName(name);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("reflecting class " + name, e);
		}
	}

	public static class ReflectionField {
		private final Class<?> clazz;
		private final String srgName;
		private final String obfName;
		private final String devName;
		private Field field;

		public ReflectionField(Class<?> clazz, String srgName, String obfName, String devName) {
			this.clazz = clazz;
			this.srgName = srgName;
			this.obfName = obfName;
			this.devName = devName;
		}

		public Object get(Object obj) {
			if (field == null)
				reflect();
			try {
				return field.get(obj);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		public void set(Object obj, Object value) {
			if (field == null)
				reflect();
			try {
				field.set(obj, value);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		private void reflect() {
			try
			{
				field = clazz.getDeclaredField(srgName);
			}
			catch (NoSuchFieldException e)
			{
				try
				{
					field = clazz.getDeclaredField(obfName);
				}
				catch (NoSuchFieldException e1)
				{
					try
					{
						field = clazz.getDeclaredField(devName);
					}
					catch (NoSuchFieldException e2)
					{
						StringBuilder sb = new StringBuilder(srgName);
						if (!srgName.equals(obfName))
							sb.append(',').append(obfName);
						if (!srgName.equals(devName))
							sb.append(',').append(devName);
						throw new RuntimeException("reflecting field " + sb.toString() + " in " + clazz.toString(), e);
					}
				}
			}

			field.setAccessible(true);
		}
	}

	public static class ReflectionMethod {
		private final Class<?> clazz;
		private final String srgName;
		private final String obfName;
		private final String devName;
		private final Class<?>[] params;
		private Method method;

		public ReflectionMethod(Class<?> clazz, String srgName, String obfName, String devName, Class<?>... params) {
			this.clazz = clazz;
			this.srgName = srgName;
			this.obfName = obfName;
			this.devName = devName;
			this.params = params;
		}

		public Object invoke(Object obj, Object... args) {
			if (method == null)
				reflect();
			try {
				return method.invoke(obj, args);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		private void reflect() {
			try
			{
				method = clazz.getDeclaredMethod(srgName, params);
			}
			catch (NoSuchMethodException e)
			{
				try
				{
					method = clazz.getDeclaredMethod(obfName, params);
				}
				catch (NoSuchMethodException e1)
				{
					try
					{
						method = clazz.getDeclaredMethod(devName, params);
					}
					catch (NoSuchMethodException e2)
					{
						StringBuilder sb = new StringBuilder(srgName);
						if (!srgName.equals(obfName))
							sb.append(',').append(obfName);
						if (!srgName.equals(devName))
							sb.append(',').append(devName);
						if (params.length > 0) {
							sb.append(" with params ");
							sb.append(Arrays.stream(params).map(Class::getName).collect(Collectors.joining(",")));
						}
						throw new RuntimeException("reflecting method " + sb.toString() + " in " + clazz.toString(), e);
					}
				}
			}

			method.setAccessible(true);
		}
	}

	public static class ReflectionConstructor {
		private final Class<?> clazz;
		private final Class<?>[] params;
		private Constructor constructor;

		public ReflectionConstructor(Class<?> clazz, Class<?>... params) {
			this.clazz = clazz;
			this.params = params;
		}

		public Object newInstance(Object... args) {
			if (constructor == null)
				reflect();
			try {
				return constructor.newInstance(args);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		private void reflect() {
			try
			{
				constructor = clazz.getDeclaredConstructor(params);
			}
			catch (NoSuchMethodException e)
			{
				StringBuilder sb = new StringBuilder();
				if (params.length > 0) {
					sb.append(" with params ");
					sb.append(Arrays.stream(params).map(Class::getName).collect(Collectors.joining(",")));
				}
				throw new RuntimeException("reflecting constructor" + sb.toString() + " in " + clazz.toString(), e);
			}

			constructor.setAccessible(true);
		}
	}
}
