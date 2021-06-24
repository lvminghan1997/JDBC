package pers.java.transaction;

import org.junit.Test;
import pers.java.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:    数据库事务操作
 * @author: 吕明翰
 * @createDate: 2021-06-24 10:46
 * @version: 1.0
 */

/**
 * DDL操作一旦执行，就会自动提交
 *  set autocommit = false 对DDL操作失效
 * DML操作默认情况下，一旦执行，就会自动提交
 *  set autocommit = false 有效
 * 默认在关闭连接时，会自动提交数据
 */
public class TransactionTest {

    @Test
    public  void testUpdate() throws Exception {
        String sql1 = "update user_table set balance = balance-100 where user = ?";
        update(sql1,"AA");
        //模拟网络异常
        System.out.println(10/0);
        String sql2 = "update user_table set balance = balance+100 where user = ?";
        update(sql2,"BB");
        System.out.println("转账成功！");
    }

    public int update(String sql , Object...arg){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < arg.length; i++) {
                ps.setObject(i+1,arg[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

        return 0;
    }

    //***************考虑数据库事务后的转账操作*****************
    @Test
    public void testUpdateWithTx(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据库的自动提交
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance-100 where user = ?";
            update(conn,sql1,"AA");
            //模拟网络异常
            System.out.println(10/0);
            String sql2 = "update user_table set balance = balance+100 where user = ?";
            update(conn,sql2,"BB");
            System.out.println("转账成功");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }

    //通用的增删改--version2.0(考虑上事务)
    //连接从外部传入
    public int update(Connection conn,String sql , Object...arg){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < arg.length; i++) {
                ps.setObject(i+1,arg[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(null,ps);
        }

        return 0;
    }

    //*********************数据库隔离级别********************
    //演示隔离级别的问题
    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn,User.class,sql,"CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 5000, "CC");
        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    //通用的查询操作，version2.0,考虑上事务
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过元数据获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取结果集的列名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //通过反射，给cust对象的指定columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,rs);

        }
        return null;
    }
}
