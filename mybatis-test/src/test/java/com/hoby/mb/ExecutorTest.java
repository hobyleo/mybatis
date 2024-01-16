package com.hoby.mb;

import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hoby
 * @since 2024-01-16
 */
public class ExecutorTest {

    SqlSessionFactory sqlSessionFactory;
    Configuration configuration;
    Connection connection;
    Transaction transaction;

    @Before
    public void before() throws Exception {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        configuration = sqlSessionFactory.getConfiguration();
        connection = configuration.getEnvironment().getDataSource().getConnection();
        transaction = configuration.getEnvironment().getTransactionFactory().newTransaction(connection);
    }

    @Test
    public void simple() throws SQLException {

        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration, transaction);

        MappedStatement mappedStatement = configuration.getMappedStatement("com.hoby.mapper.UserMapper.selectById");

        List<Object> list = simpleExecutor.doQuery(mappedStatement, 1,
                RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, mappedStatement.getBoundSql(1));
        list = simpleExecutor.doQuery(mappedStatement, 1,
                RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, mappedStatement.getBoundSql(1));

        System.out.println(list.get(0));

    }


    /**
     * 如果mysql驱动没开启预编译支持，其实预编译没意义
     */
    @Test
    public void reuse() throws SQLException {

        ReuseExecutor executor = new ReuseExecutor(configuration, transaction);

        MappedStatement mappedStatement = configuration.getMappedStatement("com.hoby.mapper.UserMapper.selectById");

        List<Object> list = executor.doQuery(mappedStatement, 1,
                RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, mappedStatement.getBoundSql(1));
        List<Object> list2 = executor.doQuery(mappedStatement, 1,
                RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, mappedStatement.getBoundSql(1));

        System.out.println(list.get(0));

    }


    @Test
    public void batch() throws SQLException {

        BatchExecutor executor = new BatchExecutor(configuration, transaction);

        MappedStatement mappedStatement = configuration.getMappedStatement("com.hoby.mapper.UserMapper.updateUsernameById");

        Map<String, Object> map = new HashMap<>();
        map.put("arg0", 1);
        map.put("arg1", "hoby");
        int result = executor.doUpdate(mappedStatement, map);

        map.put("arg0", 2);
        map.put("arg1", "yeliner");
        int result2 = executor.doUpdate(mappedStatement, map);

        List<BatchResult> batchResults = executor.doFlushStatements(false);
        System.out.println(batchResults.get(0).getUpdateCounts().length);
    }

}
