package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	static Conexion instancia;
	static Connection connec = null;
	

	private Conexion() {
		connec = this.getConexion();
		
	}

	public static Connection getInstance(){
		if (instancia == null) {
			instancia = new Conexion();				 		
		}
		 return connec;
		
	}
	

	private Connection getConexion() {
	
			Properties prop = new Properties();
			InputStream is = null;
			Connection con = null;

			try {
				is = new FileInputStream("framework.properties");
				prop.load(is);
				// Connection con = null;
				// armar la conexion
				String s = prop.getProperty("ubicacion");
				Class.forName(prop.getProperty("Driver"));
				con = DriverManager.getConnection(s, prop.getProperty("usuario"), prop.getProperty("pass"));
				
			

			} catch (IOException | ClassNotFoundException | SQLException e) {
				System.out.println(e.toString());
			}
			return con;

		}


}
