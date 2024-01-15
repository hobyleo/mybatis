package com.hoby;

import com.hoby.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * @author hoby
 * @since 2024-01-15
 */
public class App {

    public static void main(String[] args) throws IOException {

        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);

        // 1 通过加载配置文件构建一个SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        // 2 数据源执行器 DefaultSqlSession
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 3 执行查询 底层执行jdbc
            User user = session.selectOne("com.hoby.mapper.UserMapper.selectById", 1);

            // 创建动态代理
            // UserMapper mapper = session.getMapper(UserMapper.class);
            // System.out.println(mapper.getClass());
            // User user = mapper.selectById(1);

            System.out.println(user.getUsername());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

}
