package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import javafx.scene.chart.PieChart.Data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.stat.Correlation;
import org.apache.spark.ml.feature.VectorAssembler;

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
        String path = "demo/src/main/java/com/example/diabetes-smaller.csv";
        Dataset<Row> df = spark.read().option("header", "true").option("inferSchema", "true").csv(path);
        // df.limit(5).show();
        // df.printSchema();
        // df.groupBy("Age").mean().show();

        // get the columns as a list
        String[] columns_list = df.columns();

        System.out.println("Columns list: " + Arrays.toString(columns_list));

        // create a transformer to convert the columns into a vector
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(columns_list)
                .setOutputCol("features");
            
        // transform the data
        Dataset<Row> output = assembler.transform(df);

        // get the correlation matrix
        Row correlation_matrix = Correlation.corr(output, "features").head();

        Dataset<Row> correlation_matrix_dataset = Correlation.corr(output, "features");

        correlation_matrix_dataset.show();

        // print the correlation matrix
        System.out.println("Pearson correlation matrix:\n" + correlation_matrix.get(0).toString());

        // print the correlation matrix to 2 decimal places
        // Precision.round(correlation_matrix.get(0), 2);
        double rounded_number = Precision.round(41.12334133, 2);
        System.out.println("Rounded number: " + rounded_number);
        
        // write the correlation matrix to a file
        try {
            PrintWriter writer = new PrintWriter("demo/src/main/java/com/example/correlation_matrix.txt", "UTF-8");
            writer.println(correlation_matrix.get(0).toString());
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file");
        }
        // double corr = df.stat().corr("Pregnancies", "Glucose");
        // System.out.println("Correlation between Pregnancies and Glucose is " + corr);
        spark.stop();
    }

}
