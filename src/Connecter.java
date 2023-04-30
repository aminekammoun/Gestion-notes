import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Connecter {
   public  Connection conn;
    public Connecter(){
    try{
    Class.forName("com.mysql.cj.jdbc.Driver");
    }catch(ClassNotFoundException e){
    System.err.println(e);

    }
    try{
    	String url = "jdbc:mysql://localhost/gestion";
    	String user = "root";
    	String password = "Manager21c";
    	conn = DriverManager.getConnection(url, user, password);
    }catch(SQLException e){System.err.println(e);}
    }
    Connection obtenirconnexion(){return conn;}
    
}