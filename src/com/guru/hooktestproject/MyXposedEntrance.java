package com.guru.hooktestproject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MyXposedEntrance implements IXposedHookLoadPackage {

	private String PACKAGE_NAME = "com.example.ghjkl";
	private final static String TAG = "xposedtest";
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if(lpparam.packageName.equals(PACKAGE_NAME)){
			Log.i(TAG,"----------find hook packageName = " + lpparam.packageName);
			hookLoginResult(lpparam,"com.example.ghjkl.MainActivity","ifLoginSuccess");
//			hookEmptyConstructor(lpparam,"com.example.ghjkl.MainActivity");
//			hookParamConstructor(lpparam,"com.example.ghjkl.Userinfo");
//			hookChangeStaticPrivateValue(lpparam,"com.example.ghjkl.Userinfo");
			hookPublicMethod(lpparam,"com.example.ghjkl.Userinfo");
		}
	}
	
	private static void hookLoginResult(LoadPackageParam lpparam,String className,String methodName){
		XposedHelpers.findAndHookMethod(className, lpparam.classLoader, methodName,String.class,String.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				Log.i(TAG,"after hook ifLoginSuccess");
				boolean result = (Boolean) param.getResult();
				
				Log.i(TAG,"username = " + param.args[0]);
				Log.i(TAG,"password = " + param.args[1]);
				Log.i(TAG,"result = " + result);
				result = true;
				param.setResult(result);
				Log.i(TAG,"hook result = " + param.getResult());
			}
		});
	}
	
	/**�޸��ྲ̬˽�б���*/
	private static void hookChangeStaticPrivateValue(LoadPackageParam lpparam,String className){
		final Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
		Log.i(TAG,"hook hookChangeStaticPrivateValue");
		XposedHelpers.setStaticIntField(clazz, "CODE", 666);
	}
	
	/**hook�޲ι��캯�����޸�˽�б���*/
	private static void hookEmptyConstructor(LoadPackageParam lpparam,String className){
		XposedHelpers.findAndHookConstructor(className, lpparam.classLoader, new XC_MethodHook(){
			@Override
			protected void beforeHookedMethod(MethodHookParam param)
					throws Throwable {
				super.beforeHookedMethod(param);
				Log.i(TAG,"hook Emptyconstructor before");
				//��ʱ����û���������ܻ�ȡ����param.thisObject �������޸ķǾ�̬����
			}
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				super.afterHookedMethod(param);
				Log.i(TAG,"hook Emptyconstructor after");
				XposedHelpers.setIntField(param.thisObject,"testInt", 99);
			}
		});
	}

	/**hook�вι��캯��,�޸Ĳ���*/
	private static void hookParamConstructor(LoadPackageParam lpparam,String className){
		XposedHelpers.findAndHookConstructor(className, lpparam.classLoader,int.class,String.class,String.class,new XC_MethodHook(){
			@Override
			protected void beforeHookedMethod(MethodHookParam param)
					throws Throwable {
				super.beforeHookedMethod(param);
				Log.i(TAG,"hook Paramconstructor before");
			}
		});
	}
	
	/**
	 * hook���з���
	 * �޸Ĳ���
	 * �޸�private��public��ȫ�ֱ���
	 * ����private����
	 * */
	private static void hookPublicMethod(final LoadPackageParam lpparam,final String className){
		XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "setUserName",String.class, new XC_MethodHook(){
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);
				param.args[0] = "stupid";
				XposedHelpers.setIntField(param.thisObject,"publicValue", 101);
				XposedHelpers.setIntField(param.thisObject, "privateValue", 102);
				Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
				Method method = clazz.getDeclaredMethod("getPirvateValue", String.class);
				method.setAccessible(true);
				//�÷���ķ�ʽ����
//				method.invoke(param.thisObject, "testValue by invoke");
				//��hook�Ķ���
//				XposedHelpers.callMethod(param.thisObject, "getPirvateValue", "testValue by param.thisObject");
				//contructor�������
				Constructor contructor = clazz.getConstructor();
				XposedHelpers.callMethod(contructor.newInstance(), "getPirvateValue", "testValue by contructor");
			}
		});
	}
	
	/**
	 * hook˽�з���
	 * �޸Ĳ���
	 * */
	private static void hookPrivateMethod(final LoadPackageParam lpparam,final String className){
		XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "setUserName",String.class, new XC_MethodHook(){
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);
				
			}
		});
	}
	
	
}
