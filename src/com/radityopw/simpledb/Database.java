package com.radityopw.simpledb;

import java.util.Scanner;

public class Database{
    
    private Scanner sc;
    private String input;
    
    public Database(){
        sc = new Scanner(System.in);
        input = "";
    }
    
    public void start(){
        
        while(true){
            printPrompt();
            input = sc.nextLine().trim().toLowerCase();
            
            try{
                if(input.charAt(0) == '.'){
                    CommandResult cr = doMetaCommand();
                    if(cr.status == CommandResult.ERROR){
                        System.out.println("ERROR "+cr.message);
                        continue;
                    }
                    else if(cr.status == CommandResult.EXIT){
                        break;
                    }
                }else{
                    Statement stmt = prepareStatement();
                    
                    executeStatement(stmt);
                }
            } catch(Exception e){
                System.out.println("System Internal Error : "+e);
            }
            
            
        }
        
        sc.close();
        System.out.println("");
        System.out.println("exit");
    }
    
    public void printPrompt(){
        System.out.print("latihan_db > ");
    }
    
    public CommandResult doMetaCommand(){
        CommandResult cr = new CommandResult();
        
        if(input.equals(".exit")){
            cr.status = CommandResult.EXIT;
            
        }else{
            
            cr.status = CommandResult.ERROR;
            cr.message = "unrecognize commands "+input;
        }
        
        return cr;
    }
    
    public Statement prepareStatement() throws Exception{
        Statement stmt = new Statement();
        stmt.query = input;
        
        if(input.indexOf("select") == 0){
            stmt.type = Statement.SELECT;
        }
        
        else if(input.indexOf("update") == 0){
            stmt.type = Statement.UPDATE;
        }
        
        else if(input.indexOf("delete") == 0){
            stmt.type = Statement.DELETE;
        }
        
        else if(input.indexOf("insert") == 0){
            stmt.type = Statement.INSERT;
        }
        
        else {
            throw new Exception("Unsupported Statement Type");
        }
        
        return stmt;
    }
    
    public void executeStatement(Statement stmt){
        System.out.println("type "+stmt.type);
        System.out.println(stmt.query);
    }
    
    public static void main(String[] a){
        Database db = new Database();
        db.start();
    }
}