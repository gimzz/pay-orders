/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pay.orders.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author gimz
 */
public class DBConnection {
    private static final String DB_URL= "jdbc:postgresql://localhost:5432/pay_orders_db";
    private static final String USER="admin";
    private static final String PASS= "admin123";
    
    private static Connection connection =null;
    
    private DBConnection(){}
    
    public static Connection getConnection() throws SQLException{
    if(connection == null || connection.isClosed()){
    connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    return connection;
    
    }
    
}
