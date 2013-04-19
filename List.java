/*
 Author: Michele Pierini
 Date: 04/14/13
 Program Name: List.java
 Objective: Create a program that can display text 
            from a file with different output options.
*/

import java.util.*;
import java.io.*;

public class List
{
    //******************************mkWrapper()*******************************
    public static void mkWrapper()
    {
        String s = Thread.currentThread().getStackTrace()[1].getClassName();
        String wrapperName = s;
        File f = new File(wrapperName);
        if(!f.exists())
        {
            try
            {
                PrintWriter pw = new PrintWriter(f);
                pw.println("#!/bin/bash");
                pw.println("export CLASSPATH=.:$CLASSPATH");
                pw.println("java $0 ${1+\"$@\"}");
                pw.close();
            }catch(FileNotFoundException e){System.err.println(e);}
        }
        try
        {
            Process p;
            p = new ProcessBuilder("chmod", "+x", f.getAbsolutePath()).start();
        }catch(IOException e){System.err.println(e);}
    }

    //******************************reverse()*******************************
    public static String reverse(String s)
    {
        return((new StringBuffer(s)).reverse().toString());
    }

    //******************************main()*******************************
    public static void main(String args[])
    {
        mkWrapper();
         
        Scanner in = null;
	FileInputStream fis;
        GetOpt go = new GetOpt(args, "nri");
	//creating new GetOpt object and defining command flags

	int c;
        int count = 0;
        int num_lines = 0;

        boolean hasNumbered = false;
        boolean hasInverse = false;
        boolean hasReverse = false;

	String t[] = go.getarg(); //get filename(s)

        ArrayList<String> fileList = new ArrayList<String>();
        for (String s: t)
	{
	    if (!s.equals(""))
	    {
	        try
		{
		    fis = new FileInputStream(s);
		    in = new Scanner(fis);
		} catch (Exception e) {}

                while (in.hasNext())
                {
                    fileList.add(in.nextLine());
                    num_lines++;
                }
             }
         }

        while ((c = go.getopt()) != -1)
        {
	     switch(c)
	     {
	         case 'r':
                     hasReverse = true;
                     break;
                     
                 case 'n':
                     hasNumbered = true;
                     break;

		 case 'i': 
                     hasInverse = true;  
                     break;
	     }         
        }

        if (hasReverse)
        {          
            String line;
            for (int j = 0; j < num_lines; j++)
            {
                line = fileList.get(j);
                fileList.remove(j);
                fileList.add(j, reverse(line));
            }
        }

        if (hasInverse)
        {
            ArrayList<String> al = new ArrayList<String>();
            for (int j = num_lines-1; j >= 0; j--)
                al.add(fileList.get(j));
            fileList = al;
        }

        if (hasNumbered)
        {
            String str;
            String numbered;
            for (int j = 0; j < num_lines; j++)
            {
                str = fileList.get(j);
                numbered = String.format("%4d %s", ++count, str);
                fileList.remove(j);
                fileList.add(j, numbered);
            }
        }           
        
        for (int i = 0; i < num_lines; i++)
	    System.out.println(fileList.get(i));
    }
}
