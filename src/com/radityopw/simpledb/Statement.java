package com.radityopw.simpledb;

public class Statement{
    
    public static final int SELECT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int INSERT = 4;
    
    public int type;
    public String query;
}