package partidasJugadas;


import java.sql.Connection;

import java.util.Date;

import javax.swing.JOptionPane;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class partidasJugadas {

	static  String user = "root";
	static  String pas = "password";
	
	private static Connection  conexión() {
		   Connection conn = null;
		   try {
			   Class.forName("com.mysql.jdbc.Driver");
			      
		} catch (ClassNotFoundException e1) {
			
			e1.printStackTrace();
		}

		      try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost/", user, pas);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   return conn;
	};
	
   public static void registraPartida(String resultado) {
 
	   Connection conn = conexión();
	   java.sql.Statement stmt = null;
	   String sql; 

   try{
      
      stmt = conn.createStatement();  
      try {

    	  sql = "CREATE DATABASE Partidas";
      stmt.executeUpdate(sql);

      }
      catch(SQLException e) {  
      }

      conn.close();
      stmt.close();
      conn = DriverManager.getConnection("jdbc:mysql://localhost/Partidas", user, pas);
      stmt = conn.createStatement();  
      
      try {
      sql = "CREATE TABLE Partidas(resultado TEXT, fecha TEXT);";
      stmt.executeUpdate(sql);
      }
      catch(SQLException e) {
      }
      Date fecha = new Date(); 
      sql = "INSERT INTO  Partidas(resultado, fecha)\n"
      		+ "VALUES(\""+resultado+"\",\""+fecha.toString()+"\");";
      stmt.executeUpdate(sql);


    
      
   }catch(SQLException se){
	   se.printStackTrace();
   }
   catch(Exception e){
   }
     try {
		stmt.close();
	} catch (SQLException e) {
	}
      try {
		conn.close();
	} catch (SQLException e) {
	}
   
   
}
   public static  String leerPartidas() {
	   String partidas="Resultado        Fecha";
	   Connection conn = conexión();
	   try {
		conn=DriverManager.getConnection("jdbc:mysql://localhost/Partidas", user, pas);
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   try {
		   PreparedStatement consulta = conn.prepareStatement("SELECT * FROM Partidas;  ");
		   ResultSet res = consulta.executeQuery();
		    
		  for(int i=1;res.next();i++){
			   
			  partidas+="\n"+i+"  "+res.getString("resultado")+"     "+res.getString("fecha");
		          }
		          res.close();
		          consulta.close();
		    
		  } catch (Exception e) {
		   JOptionPane.showMessageDialog(null, "no se pudo consultar el el historial \n"+e);
		  }
		   System.out.println(partidas);
	   return partidas;
   };
   public static void main(String arg[]) {

   }
}
