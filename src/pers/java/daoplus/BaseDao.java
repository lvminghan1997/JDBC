package pers.java.daoplus;

import pers.java.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:    封装了针对于数据表的通用的操作
 * @author: 吕明翰
 * @createDate: 2021-06-25 10:44
 * @version: 1.0
 */
public abstract class BaseDao<T> {
    private  Class<T> clazz = null;
    //获取当前BaseDAO的子类继承的父类中的泛型
    //this 指的的是子类，因为new的是子类
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = paramType.getActualTypeArguments();//获取了父类的泛型参数
        clazz = (Class<T>) actualTypeArguments[0];
    }
    public int update(Connection conn, String sql , Object...args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(null,ps);
        }

        return 0;
    }

    public  T getInstance(Connection conn,String sql,Object...args){
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
    //通用得查询操作，用于返回数据表中的剁椒记录构成的集合，考虑上事务
    public  List<T> getForList(Connection conn, String sql, Object...args){
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
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while(rs.next()){
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(null,ps,rs);

        }
        return null;
    }

    public <E> E getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(null,ps,rs);
        }
        return null;
    }
}
