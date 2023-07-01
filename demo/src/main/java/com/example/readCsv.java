package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.ml.stat.Correlation;

public class readCsv {
    public static void main() {
        SparkSession spark = SparkSession
                .builder()
                .appName("readCsv")
                .config("spark.master", "local")
                .getOrCreate();

        // String path = "https://raw.githubusercontent.com/jameshtwose/example_deliverables/main/classification_examples/pima_diabetes/diabetes.csv";
        String path = "demo/src/main/java/com/example/diabetes.csv";
        Dataset<Row> df = spark.read().option("header", "true").option("inferSchema", "true").csv(path);
        df.limit(10).show();
        df.printSchema();

        df.stat().corr("Pregnancies", "Glucose");

        double corr = df.stat().corr("Pregnancies", "Glucose");
        System.out.println("Correlation between Pregnancies and Glucose is " + corr);
        spark.stop();
    }
}
