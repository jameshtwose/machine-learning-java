package com.example;


import com.example.newPrint;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String x = "I am very confused by the amount of new things required by java to run.";
        System.out.println( x );

        // Create an instance of MyClass
        newPrint myObject = new newPrint();

        // Call a method on the instance
        myObject.main();
    }
}
