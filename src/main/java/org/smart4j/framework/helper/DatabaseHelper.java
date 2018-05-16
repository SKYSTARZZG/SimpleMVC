package org.smart4j.framework.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.Customer;
import org.smart4j.framework.utils.CollectionUtil;
import org.smart4j.framework.utils.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author zzg
 * @Date 2018-05-02
 * @since 1.0.0
 */
public class DatabaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DatabaseHelper.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;
    static{
       CONNECTION_HOLDER = new ThreadLocal<>();
       QUERY_RUNNER = new QueryRunner();
        Properties props= PropsUtil.loadProps("config.properties");
        String driver=props.getProperty("jdbc.driver");
        String url=props.getProperty("jdbc.url");
        String username=props.getProperty("jdbc.username");
        String password=props.getProperty("jdbc.password");
        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }
    /**获取数据库连接*/
    public static Connection getConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if(conn==null){
            try{
                conn=DATA_SOURCE.getConnection();
            }catch (Exception e){
                LOGGER.error("getConnection failture",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    public static void executeSqlFile(String filePath) throws IOException {
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        try{
            String sql;
            while((sql=reader.readLine())!=null){
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failture",e);
            throw  new RuntimeException(e);
        }
    }
    /**查询实体类列表*/
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try{
            Connection conn=getConnection();
            /**第三个参数决定sql语句返回的对象类型,DbUtils给我们提供了很多handler:
             * BeanHandler：返回Bean对象    BeanListHandler:返回List对象   BeanMapHandler：返回Map对象
             * ArrayHandler：返回Object[]对象  ArrayListHandler：返回List对象  MapHandler：返回Map对象
             * MapListHandler：返回Map对象    ScalarHandler：返回某列的值  ColumnListHandler：返回某列的值列表
             * KeyedHandler:返回Map对象，需要指定列名
             * 以上这些handler都实现了ResultSetHandler*/
            entityList=QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**查询实体*/
    public static <T> T getEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        try {
            Connection conn=getConnection();
            entity=QUERY_RUNNER.query(conn,sql,new BeanHandler<>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity failture",e);
            throw new RuntimeException();
        }
        return entity;
    }

    public static List<Map<String,Object>> executeQuery(String sql, Object... params){
        List<Map<String,Object>> result;
        try{
            Connection conn=getConnection();
            result=QUERY_RUNNER.query(conn,sql, new MapListHandler(),params);
        }catch (Exception e){
            LOGGER.error("execute query failture",e);
            throw new RuntimeException();
        }
        return result;
    }

    /**执行更新语句(包括update，insert，delete)*/
    public static int executeUpdate(String sql,Object... params){
        int rows=0;
        try{
            Connection conn=getConnection();
            rows=QUERY_RUNNER.update(conn,sql,params);
        }catch (Exception e){
            LOGGER.error("execute update failture!",e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**插入实体*/
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }
        String sql="INSERT INTO" +getTableName(entityClass);
        StringBuilder columns=new StringBuilder("(");
        StringBuilder values=new StringBuilder("(");
        fieldMap.keySet().forEach((fieldName)->{
            columns.append(fieldName).append(", ");
            values.append("?,");
        });
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),columns.length(),")");
        sql+=columns+" VALUES "+values;
        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**更新实体*/
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }

        List<Object> paramList=new ArrayList<>();
        String sql="UPDATE "+getTableName(entityClass)+" SET ";
        StringBuilder columns=new StringBuilder();
        fieldMap.forEach((fieldName,param)->{
            columns.append(fieldName).append(" =?, ");
            paramList.add(param);
        });
        sql+=columns.substring(0,columns.lastIndexOf(", "))+" WHERE id=?";
        paramList.add(id);

        Object[] params=paramList.toArray();
        return executeUpdate(sql,params)==1;
    }

    /**执行删除*/
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM " +getTableName(entityClass)+" WHERE id=?";
        return executeUpdate(sql,id)==1;
    }
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    /**开启事务*/
    public static void beginTransaction(){
        Connection conn=getConnection();
        try{
            conn.setAutoCommit(false);
        }catch (Exception e){
            LOGGER.error("begin transaction failture",e);
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.set(conn);
        }
    }

    /**提交事务*/
    public static void commitTransaction(){
        Connection conn=getConnection();
        try{
            conn.commit();
            conn.close();
        }catch (Exception e){
            LOGGER.error("commit Transaction failture",e);
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.remove();
        }
    }

    /**回滚事务*/
    public static  void rollbackTransaction(){
        Connection conn=getConnection();
        try{
            conn.rollback();
            conn.close();
        }catch (Exception e){
            LOGGER.error("rollback transsaction failture");
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.remove();
        }
    }
}
