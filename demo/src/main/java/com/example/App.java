package com.example;


import com.example.JavaCorrelationExample;
import com.example.readCsv;
public class App 
{
    public static void main( String[] args )
    {
        String x = "I am very confused by the amount of new things required by java to run.";
        System.out.println( x );

        // Create an instance of MyClass
        JavaCorrelationExample myObject2 = new JavaCorrelationExample();

        // Call a method on the instance
        myObject2.main();

        // Create an instance of MyClass
        readCsv myObject = new readCsv();

        // Call a method on the instance
        myObject.main();
    }
}
