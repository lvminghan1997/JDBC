package pers.java.preparedstatementCRUD;

import org.junit.Test;
import pers.java.bean.Order;
import pers.java.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @description: 针对Order表的查询操作
 * @author: 吕明翰
 * @createDate: 2021-06-22 15:23
 * @version: 1.0
 */
public class OrderForQuery {
    /**
     * 针对于表的字段名与类的属性名不相同的情况:
     * 1,必须声明sql时，使用类的属性名来命名字段的别名
     * 2,使用ResultSetMetaData时，需要使用getColumnLabel来替换getColumnName
     *   来获取列的别名
     *
     *  说明：如果没给字段名起别名，getColumnLabel获取的就是字段名
     */
    @Test
    public void testOrderForQuery(){
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }
    /**
     * 通用的针对order表的查询操作
     * @param sql
     * @param args
     * @return
     */
    public Order orderForQuery(String sql,Object...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            res = ps.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (res.next()){
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值: 通过ResultSet
                    Object columnValue = res.getObject(i + 1);
                    //获取每个列的别名  推荐使用
                    String columnLabel = rsmd.getColumnLabel(i + 1);
//                    //获取每个列的列名: 通过ResultSetMetaData   不推荐使用
//                    String columnName = rsmd.getColumnName(i + 1);
                    //通过反射将对象指定名的属性赋值为指定的值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,res);
        }
        return null;
    }



    @Test
    public void testQuery1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            res = ps.executeQuery();
            if (res.next()){
                int id = (int) res.getObject(1);
                String name = (String) res.getObject(2);
                Date date = (Date) res.getObject(3);
                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,res);
        }
    }
}
