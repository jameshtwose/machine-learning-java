package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

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
    // public static class CorrOutput implements Serializable {
    //     private String column1;
    //     private String column2;
    //     private double correlation;

    //     public String getCol1() {
    //         return column1;
    //     }

    //     public String getCol2() {
    //         return column2;
    //     }

    //     public double getCorr() {
    //         return correlation;
    //     }

    //     public void setCol1(String column1) {
    //         this.column1 = column1;
    //     }

    //     public void setCol2(String column2) {
    //         this.column2 = column2;
    //     }

    //     public void setCorr(double correlation) {
    //         this.correlation = correlation;
    //     }

    // }

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

        // print the correlation matrix
        System.out.println("Pearson correlation matrix:\n" + correlation_matrix.get(0).toString());
        
        // double corr = df.stat().corr("Pregnancies", "Glucose");
        // System.out.println("Correlation between Pregnancies and Glucose is " + corr);
        spark.stop();
    }

}
