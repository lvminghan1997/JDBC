package pers.java.dbutils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import pers.java.bean.Customer;
import pers.java.util.JDBCUtils;

import java.sql.Connection;
import java.util.List;

/**
 * @description: apache 组织提供的开源JDBC工具类库，封装了crud操作
 * @author: 吕明翰
 * @createDate: 2021-06-28 14:39
 * @version: 1.0
 */
public class QueryRunnerTest {
    @Test
    public void testInsert(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            int insertCount = runner.update(conn, sql, "蔡徐坤", "7777@qq.com", "1992-09-04");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //连接由busy改为free返回连接池，不是关闭连接
            JDBCUtils.CloseResource(conn,null);
        }


    }
    @Test
    public void testQuery(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id = ?";
            //BeanHandler是ResultSetHandler接口的实现类，用于封装表中的一条记录
            BeanHandler<Customer> handler = new BeanHandler<> (Customer.class);
            Customer customer = runner.query(conn, sql, handler, 13);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }
    @Test
    public void testQuery1(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id > ?";
            //BeanListHandler是ResultSetHandler接口的实现类，用于封装表中的多条记录
            BeanListHandler<Customer> handler = new BeanListHandler(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 13);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }
    //查询特殊值
    @Test
    public void testQuery2(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "select count(*) from customers";
            //BeanListHandler是ResultSetHandler接口的实现类，用于封装表中的多条记录
            ScalarHandler handler = new ScalarHandler();
            Long count = (long) runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }
}
