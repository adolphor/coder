package com.bob.coder.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySQLTableComment {

  public static Connection getMySQLConnection() throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nMall", "nMall", "nMall");
    return conn;
  }

  public static Map<String, String> getCommentByTableName(List<String> tableName) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    Connection conn = getMySQLConnection();
    Statement stmt = conn.createStatement();
    for (int i = 0; i < tableName.size(); i++) {
      String table = (String) tableName.get(i);
      ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
      if (rs != null && rs.next()) {
        String create = rs.getString(2);
        String comment = parse(create);
        map.put(table, comment);
      }
      rs.close();
    }
    stmt.close();
    conn.close();
    return map;
  }

  public static List<String> getAllTableName() throws Exception {
    List<String> tables = new ArrayList<String>();
    Connection conn = getMySQLConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SHOW TABLES ");
    while (rs.next()) {
      String tableName = rs.getString(1);
      tables.add(tableName);
    }
    rs.close();
    stmt.close();
    conn.close();
    return tables;
  }

  public static String parse(String all) {
    String comment = null;
    int index = all.indexOf("COMMENT='");
    if (index < 0) {
      return "";
    }
    comment = all.substring(index + 9);
    comment = comment.substring(0, comment.length() - 1);
    return comment;
  }

  public static void main(String[] args) throws Exception {
    List<String> tables = getAllTableName();
    Map<?, ?> tablesComment = getCommentByTableName(tables);
    Set<?> names = tablesComment.keySet();
    Iterator<?> iter = names.iterator();
    while (iter.hasNext()) {
      String name = (String) iter.next();
      System.out.println("Table Name: " + name + ", Comment: " + tablesComment.get(name));
    }
  }

}