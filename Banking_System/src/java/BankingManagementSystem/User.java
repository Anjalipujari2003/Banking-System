/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BankingManagementSystem;

/**
 *
 * @author anjal
 */
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public class PasswordEncryptor {
        public static String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

        public static boolean CheckPassword(String password, String hashedPassword) {
            return BCrypt.checkpw(password, hashedPassword);
        }
    }

    public void register() {
        scanner.nextLine();
        System.out.println("Full Name:");
        String full_name = scanner.nextLine();
        System.out.println("Email");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        if (user_exist(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String storedHashedPassword=PasswordEncryptor.hashPassword(password);
        String register_query="Insert into user(full_name,email,password) values(?,?,?)";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,storedHashedPassword);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Registration Successfull!"); 
            }
            else {
                System.out.println("Registration Failed!");
            }


        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }
    public String login()
    {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        //String storedHashedPassword=PasswordEncryptor.hashPassword(password);
        String login_query="Select password from user where email=? ";
        
        
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            
            preparedStatement.setString(1,email);
            
            ResultSet resultset=preparedStatement.executeQuery();
            if(resultset.next())
            {
                 String storedHashedPassword = resultset.getString("password");

                boolean passwordMatch = PasswordEncryptor.CheckPassword(password, storedHashedPassword);
                if (passwordMatch) {
                    return email;
                } else {
                    System.out.println("Incorrect Password!");
                }
            }
            else
            {
                System.out.println("User not found!");
            }
            
        }
        catch(SQLException e)
       {
            e.printStackTrace();
        }
        
       return null;
    }
    public boolean user_exist(String email)
    {
        String query="select * from user where email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultset=preparedStatement.executeQuery();
            if(resultset.next())
            {
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
