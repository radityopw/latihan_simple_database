package com.radityopw.simpledb;

import java.util.*;
import com.radityopw.simpledb.metadata.*;

public class Database{
    
    public Scanner sc;
    public String input;
    public Metadata metadata;
    
    public Database(){
        sc = new Scanner(System.in);
        input = "";
        metadata = new Metadata();
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
        System.out.print("simpledb > ");
    }
    
    public CommandResult doMetaCommand(){
        CommandResult cr = new CommandResult();
        
        if(input.equals(".exit")){
            cr.status = CommandResult.EXIT;
            
        }else if(input.equals(".tables")){
            cr.status = CommandResult.OK;
            System.out.println("----- list tables -----");
            Set<String> listTable = metadata.tables.keySet();
            Iterator<String> listTableIterator = listTable.iterator();
            while(listTableIterator.hasNext()){
                System.out.println(listTableIterator.next());
            }
            System.out.println("----- end list tables -----");
        }else if(input.substring(0,7).equals(".schema")){
            String tableName = input.replaceAll(".schema","").trim();
            if(!metadata.tables.containsKey(tableName)){
                cr.status = CommandResult.ERROR;
                cr.message = "Table Not found "+tableName;
            }else{
                cr.status = CommandResult.OK;
                System.out.println("----- list columns -----");
                for(Column c : metadata.tables.get(tableName).columns.values()){
                    System.out.println(c.name + " " +c.dataType + " " +c.length);
                }
                System.out.println("----- end list columns -----");
            }
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
        
        else if(input.indexOf("create") == 0){
            stmt.type = Statement.CREATE;
            input = input.replaceAll("  "," ");
            stmt.parserQuery = input.split(" ");
            
            if(stmt.parserQuery.length % 3 != 0){
                throw new Exception("Unsupported Statement: CREATE TABLE TABLE_NAME COL_NAME COL_TYPE COL_LENGTH");
            }
        }
        
        else {
            throw new Exception("Unsupported Statement Type");
        }
        
        return stmt;
    }
    
    public void executeStatement(Statement stmt) throws Exception{
        System.out.println("type "+stmt.type);
        System.out.println(stmt.query);
        
        if(stmt.type == Statement.CREATE){
            if(metadata.tables.containsKey(stmt.parserQuery[2])){
                throw new Exception("TABLE "+stmt.parserQuery[2]+" is Existed");
            }
            
            Table table = new Table();
            table.name = stmt.parserQuery[2];
            
            for(int i=3; i< stmt.parserQuery.length; i = i + 3){
                Column c = new Column();
                c.name = stmt.parserQuery[i];
                
                if(!stmt.parserQuery[i+1].equals("varchar")){
                    table = null;
                    c = null;
                    throw new Exception("Data Type Not Supported");
                }
                
                c.dataType = Column.VARCHAR;
                
                c.length = Integer.parseInt(stmt.parserQuery[i+2]);
                
                c.dataTypeName = stmt.parserQuery[i+1];
                
                if(table.columns.containsKey(c.name)){
                    table = null;
                    c = null;
                    throw new Exception("Duplicate Column Name Found : "+c.name);
                }
                
                table.columns.put(c.name,c);
                
            }
            
            metadata.tables.put(table.name,table);
            
        }
    }
    
    public static void main(String[] a){
        Database db = new Database();
        db.start();
    }
}