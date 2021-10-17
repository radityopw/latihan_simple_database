package com.radityopw.simpledb;

import java.util.*;

public class Statement{
    
    public static final int SELECT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int INSERT = 4;
    public static final int ALTER = 5;
    public static final int CREATE = 6;
    
    public int type;
    public String query;
    public String[] parserQuery;
}