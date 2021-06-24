package pers.java.transaction;

import org.junit.Test;
import pers.java.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
