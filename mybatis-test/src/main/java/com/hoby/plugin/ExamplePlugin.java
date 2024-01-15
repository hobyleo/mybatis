package com.hoby.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author hoby
 * @since 2024-01-15
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
})})
public class ExamplePlugin implements Interceptor {

    Properties properties = null;

    /**
     * 拦截方法逻辑
     * 这里主要是通过反射去获取要执行的SQL相关信息，然后进行操作
     */
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget(); // 被代理对象
        Method method = invocation.getMethod(); // 代理方法
        Object[] args = invocation.getArgs(); // 方法参数

        // do something ... 方法拦截前执行代码块
        System.out.println("target = " + target);
        System.out.println("method = " + method);
        System.out.println("args = " + Arrays.toString(args));
        System.out.println("properties = " + properties);

        Object result = invocation.proceed();

        // do something ... 方法拦截后执行代码块
        System.out.println("result = " + result);

        return result;
    }

    /**
     * 生成MyBatis拦截器代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            // 调用插件
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 设置插件属性（直接通过Spring的方式获取属性，所以这个方法一般也用不到）
     * 项目启动的时候数据就会被加载
     */
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
