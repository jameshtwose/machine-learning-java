package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.stat.Correlation;

// import org.projectlombok.val;


public class readCsv {
    public static void main() {
        boolean stop_logging = true;

        if (stop_logging) {
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("akka").setLevel(Level.OFF);
            Logger.getRootLogger().setLevel(Level.OFF);
        }

        SparkSession spark = SparkSession
                .builder()
                .appName("readCsv")
                .config("spark.master", "local")
                .getOrCreate();

        // String path = "https://raw.githubusercontent.com/jameshtwose/example_deliverables/main/classification_examples/pima_diabetes/diabetes.csv";
        String path = "demo/src/main/java/com/example/diabetes.csv";
        Dataset<Row> df = spark.read().option("header", "true").option("inferSchema", "true").csv(path);
        df.limit(5).show();
        df.printSchema();

        // get the columns as a list
        String[] columns_list = df.columns();

        System.out.println("Columns list: " + Arrays.toString(columns_list));

        // get the cartesian product of the columns list and print the correlation between each pair
        for (String column : columns_list) {
            for (String column2 : columns_list) {
                double current_corr = Precision.round(df.stat().corr(column, column2), 3);
                System.out.println("Correlation between " + column + " and " + column2 + " is " + current_corr);

                // create a data frame with the correlation between each pair of columns
                // Dataset<Row> corr_df = spark.createDataFrame(
                //         Arrays.asList(
                //                 new CorrelationData(column, column2, current_corr)
                //         ),
                //         CorrelationData.class
                // );
                
            }
        }
        
        // double corr = df.stat().corr("Pregnancies", "Glucose");
        // System.out.println("Correlation between Pregnancies and Glucose is " + corr);
        spark.stop();
    }

}
