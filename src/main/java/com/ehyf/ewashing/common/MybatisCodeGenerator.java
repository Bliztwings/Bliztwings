package com.ehyf.ewashing.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动生成MyBatis的实体类、映射XML文件、servcie和dao层代码
 * 
 * @author:	  jelly  
 * @date:	  2017年5月17日 上午9:59:13
 * @version:  v1.0
 */
public class MybatisCodeGenerator {
	/** 项目名称 **/
	private final String project_name = "ewashing";
	
	/** 项目包前缀 **/
	private final String package_prefix = "com.ehyf.ewashing";
 
	/** MySQL数据类型定义  **/
    private final String type_char = "char";
 
    private final String type_date = "date";
 
    private final String type_timestamp = "timestamp";
 
    private final String type_int = "int";
 
    private final String type_bigint = "bigint";
 
    private final String type_text = "text";
 
    private final String type_bit = "bit";
 
    private final String type_decimal = "decimal";
 
    private final String type_blob = "blob";
    
    private final String baseDir="E:\\source\\generateCode";
 
    /** 自动生成代码的目录定义 **/
    private final String entity_path = baseDir+ project_name + "/entity";
 
    private final String dao_path = baseDir + project_name + "/dao";
 
    private final String xml_path = baseDir + project_name + "/mapper";
    
    private final String service_path = baseDir + project_name + "/service";
 
    private final String entity_package = package_prefix + ".entity";
 
    private final String dao_package = package_prefix + ".dao";
    
    private final String service_package = package_prefix + ".service";
 
    /** 数据库连接信息 **/
    private final String driverName = "com.mysql.jdbc.Driver";
 
    private final String user = "ewashing";
 
    private final String password = "ewashing123@!~";
 
    private final String url = "jdbc:mysql://118.178.134.100:3306/ewashing_test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
 
    private String tableName = null;
 
    private String entityName = null;
 
    private String daoName = null;
    
    private String serviceName = null;
 
    private Connection conn = null;
 
 
    /**
     * 
     * 连接数据库
     * 
     */
    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
    }
 
    /**
     * 
     * 根据表名得到entityName和daoName
     * 
     */
    private void processTable(String table) {
        StringBuffer sb = new StringBuffer(table.length());
        String tableNew = table.toLowerCase();
        String[] tables = tableNew.split("_");
        String temp = null;
        for ( int i = 0 ; i < tables.length ; i++ ) {
            temp = tables[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        
        entityName = sb.toString();
        daoName = entityName + "Dao";
        serviceName = entityName + "Service";
    }
 
    /**
     * 根据数据库字段类型，得到entity属性类型
     * @param type
     * @return
     */
    private String mysqlTypeToJavaType(String type) {
        if ( type.indexOf(type_char) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_bigint) > -1 ) {
            return "Long";
        } else if ( type.indexOf(type_int) > -1 ) {
            return "Integer";
        } else if ( type.indexOf(type_date) > -1 ) {
            return "java.util.Date";
        	//return "String";
        } else if ( type.indexOf(type_text) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_timestamp) > -1 ) {
            return "java.util.Date";
            //return "String";
        } else if ( type.indexOf(type_bit) > -1 ) {
            return "Boolean";
        } else if ( type.indexOf(type_decimal) > -1 ) {
            return "java.math.BigDecimal";
        } else if ( type.indexOf(type_blob) > -1 ) {
            return "byte[]";
        }
        return null;
    }
    
    /**
     * 根据数据库字段类型，得到mapper jdbc类型
     * @param type
     * @return
     */
    private String mysqlTypeToJdbcType(String type) {
    	type = type.toLowerCase();
    	if(type.indexOf("(") > 0){
    		type = type.substring(0, type.indexOf("("));
    	}
    	if(type.equals("datetime")){
    		type = "timestamp";
    	}
    	if(type.equals("int")){
    		type = "integer";
    	}
    	return type;
    }
 
    /**
     * 处理字段，大写变小写，并处理字段中的下划线
     * @param column
     * @return
     */
    private String columnToJavaProperty(String column) {
        StringBuffer sb = new StringBuffer(column.length());
        column = column.toLowerCase();
        String[] fields = column.split("_");
        String temp = null;
        sb.append(fields[0]);
        for ( int i = 1 ; i < fields.length ; i++ ) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }
 
 
    /**
     *  构建类上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    private BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
    	if(text==null || "".equals(text)){
    		return bw;
    	}
    	else{
    		bw.newLine();
            bw.newLine();
            bw.write("/**");
            bw.newLine();
            bw.write(" * ");
            bw.newLine();
            bw.write(" * " + text);
            bw.newLine();
            bw.write(" * ");
            bw.newLine();
            bw.write(" **/");
            return bw;
    	}
    }
 
 
    /**
     *  构建方法上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("unused")
	private BufferedWriter buildMethodComment(BufferedWriter bw, String text) throws IOException {
    	if(text==null || "".equals(text)){
    		return bw;
    	}
    	else{
	        bw.newLine();
	        bw.write("\t/**");
	        bw.newLine();
	        bw.write("\t * ");
	        bw.newLine();
	        bw.write("\t * " + text);
	        bw.newLine();
	        bw.write("\t * ");
	        bw.newLine();
	        bw.write("\t **/");
	        return bw;
    	}
    }
 
 
    /**
     *  自动生成Entity
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException 
     */
    private void generateEntityBean(List<String> properties, List<String> javaTypes, List<String> comments, String tableComment) throws IOException {
        File folder = new File(entity_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File beanFile = new File(entity_path, entityName + ".java");
        if(!beanFile.exists()){
        	beanFile.createNewFile();
        }
        	
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + entity_package + ";");
        bw.newLine();
        bw.newLine();
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        bw.write("public class " + entityName + " extends BaseEntity<"+entityName+"> {");
        bw.newLine();
        bw.newLine();
        bw.write("\tprivate static final long serialVersionUID = 1L;");
        bw.newLine();
        bw.newLine();
        int size = properties.size();
        for ( int i = 0 ; i < size ; i++ ) {
        	if(comments.get(i)!=null && !comments.get(i).equals("")){
        		bw.write("\t/**" + comments.get(i) + "**/");
                bw.newLine();
        	}
            bw.write("\tprivate " + javaTypes.get(i) + " " + properties.get(i) + ";");
            bw.newLine();
            bw.newLine();
        }
        // 生成get 和 set方法
        String tempField = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = properties.get(i).substring(0, 1).toUpperCase() + properties.get(i).substring(1);
            bw.write("\tpublic void set" + tempField + "(" + javaTypes.get(i) + " " + properties.get(i) + "){");
            bw.newLine();
            bw.write("\t\tthis." + properties.get(i) + " = " + properties.get(i) + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            bw.write("\tpublic " + javaTypes.get(i) + " get" + tempField + "(){");
            bw.newLine();
            bw.write("\t\treturn this." + properties.get(i) + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
        }
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }
    
    /**
     *  自动生成Service
     *
     * @throws IOException 
     */
    private void generateService() throws IOException {
        File folder = new File(service_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperFile = new File(service_path, serviceName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + service_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + entity_package + "." + entityName + ";");
        bw.newLine();
        bw.write("import " + dao_package + "." + daoName + ";");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw = buildClassComment(bw, serviceName + "  Service服务接口类");
        bw.newLine();
        bw.write("@Service");
        bw.newLine();
        bw.write("public class " + serviceName + " extends BaseService<"+daoName+","+entityName+"> {");
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }
 
 
    /**
     *  自动生成Dao
     *
     * @throws IOException 
     */
    private void generateDao() throws IOException {
        File folder = new File(dao_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperFile = new File(dao_path, daoName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + dao_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + entity_package + "." + entityName + ";");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Repository;");
        bw = buildClassComment(bw, daoName + "  数据库操作接口类");
        bw.newLine();
        bw.write("@Repository");
        bw.newLine();
        bw.write("public interface " + daoName + " extends BaseDao<"+entityName+"> {");
        bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }
 
 
    /**
     *  构建实体类映射XML文件
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException 
     */
    private void generateMapperXml( List<String> columns, List<String> jdbcTypes, List<String> javaTypes, List<String> properties) throws IOException {
        File folder = new File(xml_path);
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperXmlFile = new File(xml_path, daoName + ".xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        bw.newLine();
        bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + dao_package + "." + daoName + "\">");
        bw.newLine();
        bw.newLine();
 
        // 下面开始写SqlMapper中的方法
        buildSQL(bw, columns, jdbcTypes, javaTypes, properties);
 
        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }
 
 
    private void buildSQL( BufferedWriter bw, List<String> columns, List<String> jdbcTypes, List<String> javaTypes, List<String> properties) throws IOException {
        int size = columns.size();
        
        // 通用BaseResultMap
        bw.write("\t<!-- 通用ResultMap  -->");
        bw.newLine();
        bw.write("\t<resultMap id=\"BaseResultMap\" type=\""+entityName+"\">");
        bw.newLine();
 
        bw.write("\t\t <id column=\"id\" jdbcType=\"VARCHAR\" property=\"id\" />");
        bw.newLine();
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t\t <result column=\""+columns.get(i)+"\" jdbcType=\""+jdbcTypes.get(i)+"\" property=\""+properties.get(i)+"\" />");
            if ( i != size - 1 ) {
            	bw.newLine();
            }
        }
 
        bw.newLine();
        bw.write("\t</resultMap>");
        bw.newLine();
        bw.newLine();
        
        // 通用结果列
        bw.write("\t<!-- 通用查询结果列  -->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");
        bw.newLine();
 
        bw.write("\t\t id,");
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write(" " + columns.get(i).toLowerCase());
            if ( i != size - 1 ) {
                bw.write(",");
            }
        }
 
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
 
 
        // 查询（根据主键ID查询）
        bw.write("\t<!-- 查询 (根据主键ID查询) -->");
        bw.newLine();
        bw.write("\t<select id=\"getById\" parameterType=\"" + javaTypes.get(0) + "\" resultMap=\"BaseResultMap\">");
        bw.newLine();
        bw.write("\t\t select");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t from " + tableName);
        bw.newLine();
        bw.write("\t\t where " + columns.get(0).toLowerCase() + " = #{" + properties.get(0) + "}");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完
 
 
        // 删除（根据主键ID删除）
        bw.write("\t<!--  删除 (根据主键ID删除)  -->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteById\" parameterType=\"" + javaTypes.get(0) + "\">");
        bw.newLine();
        bw.write("\t\t delete from " + tableName);
        bw.newLine();
        bw.write("\t\t where " + columns.get(0).toLowerCase() + " = #{" + properties.get(0) + "}");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完
 
 
        // 添加insert方法
        bw.write("\t<!-- 添加 -->");
        bw.newLine();
        bw.write("\t<insert id=\"insert\" parameterType=\"" + entityName + "\">");
        bw.newLine();
        bw.write("\t\t insert into " + tableName);
        bw.newLine();
        bw.write(" \t\t(");
        for ( int i = 0 ; i < size ; i++ ) {
            bw.write(columns.get(i).toLowerCase());
            if ( i != size - 1 ) {
                bw.write(",");
            }
        }
        bw.write(") ");
        bw.newLine();
        bw.write("\t\t values ");
        bw.newLine();
        bw.write(" \t\t(");
        for ( int i = 0 ; i < size ; i++ ) {
            bw.write("#{" + properties.get(i) + "}");
            if ( i != size - 1 ) {
                bw.write(",");
            }
        }
        bw.write(") ");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // 添加insert完

        // 修改update方法
        bw.write("\t<!-- 修改  -->");
        bw.newLine();
        bw.write("\t<update id=\"update\" parameterType=\"" + entityName + "\">");
        bw.newLine();
        bw.write("\t\t update " + tableName);
        bw.newLine();
        bw.write(" \t\t <set> ");
        bw.newLine();
        
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t\t\t<if test=\"" + properties.get(i) + " != null and "+properties.get(i)+" != ''\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + " = #{" + properties.get(i) + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
 
        bw.write(" \t\t </set>");
        bw.newLine();
        bw.write("\t\t where " + columns.get(0) + " = #{" + properties.get(0) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update方法完毕
        
        
        //分页查询开始
        bw.write("\t<!-- 分页查询  -->");
        bw.newLine();
        bw.write("\t<select id=\"findList\" parameterType=\"" + entityName + "\" resultMap=\"BaseResultMap\">");
        bw.newLine();
        bw.write("\t\t select");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t from " + tableName);
        bw.newLine();
        bw.write("\t\t where 1=1 ");
        bw.newLine();
        
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t\t\t<if test=\"" + properties.get(i) + " != null and "+properties.get(i)+" != ''\">");
            bw.newLine();
            bw.write("\t\t\t\t and " + columns.get(i).toLowerCase() + " = #{" + properties.get(i) + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
 
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        //分页查询结束
    }
 
 
    /**
     *  获取所有的数据库表注释
     *
     * @return
     * @throws SQLException 
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = conn.prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while ( results.next() ) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }
    
    /**
     *  获取所有的表
     *
     * @return
     * @throws SQLException 
     */
    @SuppressWarnings("unused")
	private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        PreparedStatement pstate = conn.prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
        while ( results.next() ) {
            String tableName = results.getString(1);
            tables.add(tableName);
        }
        return tables;
    }
 
 
    public void generate() throws ClassNotFoundException, SQLException, IOException {
        init();
        String prefix = "show full fields from ";
        List<String> columns = null; 	//字段
        List<String> jdbcTypes = null;  //jdbc类型
        List<String> javaTypes = null;  //java类型
        List<String> comments = null;	//字段备注
        List<String> properties = null; //属性
        
        PreparedStatement pstate = null;
        ResultSet rs = null;
        
        //List<String> tables = getTables();
        List<String> tables = new ArrayList<String>();
//        tables.add("check_code");
//        tables.add("member_receive_address");
     
        
        Map<String, String> tableComments = getTableComment();
        
        for ( String table : tables ) {
            columns = new ArrayList<String>();
            jdbcTypes = new ArrayList<String>();
            javaTypes = new ArrayList<String>();
            comments = new ArrayList<String>();
            properties = new ArrayList<String>();
            
            pstate = conn.prepareStatement(prefix + table);
            rs = pstate.executeQuery();
            while (rs.next() ) {
                columns.add(rs.getString("FIELD").toLowerCase());
                jdbcTypes.add(mysqlTypeToJdbcType(rs.getString("TYPE")).toUpperCase());
                javaTypes.add(mysqlTypeToJavaType(rs.getString("TYPE")));
                comments.add(rs.getString("COMMENT"));
                properties.add(columnToJavaProperty(rs.getString("FIELD")));
            }
            tableName = table;
            processTable(table);
            
            String tableComment = tableComments.get(tableName);
            
            generateEntityBean(properties, javaTypes, comments, tableComment);
            generateService();
            generateDao();
            generateMapperXml(columns, jdbcTypes, javaTypes, properties);
        }
        conn.close();
    }
 
 
    public static void main( String[] args ) {
        try {
            new MybatisCodeGenerator().generate();
            // 自动打开生成文件的目录
            //Runtime.getRuntime().exec("cmd /c start explorer D:\\mybatisCodeGen");
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
