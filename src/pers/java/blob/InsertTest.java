package pers.java.blob;

import org.junit.Test;
import pers.java.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @description:    批量插入数据
 * @author: 吕明翰
 * @createDate: 2021-06-23 18:02
 * @version: 1.0
 */
public class InsertTest {
    @Test
    public void insertTest1(){

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=20000; i++) {
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

    }

    /**
     *使用batch提高批量插入的效率
     * 1.配置文件的url中加入rewriteBatchedStatements=true
     * addBatch() executeBatch() clearBatch()
     *
     * 20000//22s - 560ms
     */
    @Test
    public void insertTest2(){

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            long start = System.currentTimeMillis();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=1000000; i++) {
                ps.setObject(1,"name_"+i);

                //1."攒"sql
                ps.addBatch();
                if (i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

    }

    /**
     * 批量插入再次优化
     * 设置数据库自动提交为FALSE
     * 1000000//6s-3s
     */
    @Test
    public void insertTest3(){

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            long start = System.currentTimeMillis();
            //设置数据库自动提交为FALSE
            conn.setAutoCommit(false);
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=1000000; i++) {
                ps.setObject(1,"name_"+i);

                //1."攒"sql
                ps.addBatch();
                if (i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println(end-start);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

    }
}
