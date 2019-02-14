package com.codecasales.amazonviewer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.codecasales.amazonviewer.db.DataBase.*;

public interface IDBConection {
	
	default Connection connecToDB() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL+DB, USER, PASSWORD);
			if(connection != null) {
				System.out.println("Se establecio la coneccion :)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return connection;
		}
	}
	
}
