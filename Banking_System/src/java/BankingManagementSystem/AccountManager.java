/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BankingManagementSystem;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
/**
 *
 * @author anjal
 */
public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    AccountManager(Connection connection,Scanner scanner)
    {
        this.connection=connection;
        this.scanner=scanner;
    }
    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Amount:");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin:");
        String security_pin=scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                PreparedStatement preparedStatement=connection.prepareStatement("Select * from accounts where account_number=? and seccurity_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultset=preparedStatement.executeQuery();
                if(resultset.next())
                {
                    String credit_query="Update accounts set balance=balance+? where account_number=?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if(rowsAffected>0)
                    {
                        System.out.println("Rs."+amount+" Credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else{
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }   
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
        }
    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Amount:");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin:");
        String security_pin=scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and seccurity_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery(); 
                if(resultSet.next())
                {
                    double current_balance=resultSet.getDouble("balance");
                    if(amount<=current_balance)
                    {
                        String debit_query="Update accounts set balance=balance-? where account_number=?";
                        PreparedStatement preparestatement1=connection.prepareStatement(debit_query);
                        preparestatement1.setDouble(1, amount);
                        preparestatement1.setLong(2, account_number);
                        int rowsAffected=preparestatement1.executeUpdate();
                        if(rowsAffected>0)
                        {
                           System.out.println("Rs."+amount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return; 
                        }
                        else{
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance");
                    }
                }
                else{
                    System.out.println("Invalid Pin!");
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter the Receiver Account no:");
        long receiver_account_number=scanner.nextLong();
        System.out.println("Enter Amount");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0 )
            {
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND seccurity_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                        {
                           double current_balance=resultSet.getDouble("balance");
                           if(amount<=current_balance)
                           {
                               String debit_query="Update accounts set balance=balance - ? where account_number = ? ";
                               String credit_query="Update accounts set balance=balance + ? where account_number = ? ";
                               PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);
                               PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
                               creditPreparedStatement.setDouble(1, amount);
                               creditPreparedStatement.setLong(2, receiver_account_number);
                               debitPreparedStatement.setDouble(1, amount);
                               debitPreparedStatement.setLong(2, sender_account_number);
                               int rowsAffected1 = debitPreparedStatement.executeUpdate();
                                int rowsAffected2 = creditPreparedStatement.executeUpdate();
                                if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                                System.out.println("Transaction Successful!");
                                System.out.println("Rs."+amount+" Transferred Successfully");
                                connection.commit();
                                connection.setAutoCommit(true);
                                return;
                            } else {
                                 System.out.println("Transaction Failed");
                                 connection.rollback();
                                 connection.setAutoCommit(true);
                        }
                     
                     
                           
                           }
                           else{
                               System.out.println("Insufficient Balance!");
                           }
                        }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
            else{
                 System.out.println("Invalid account number");
            
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    
     public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND seccurity_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
}
