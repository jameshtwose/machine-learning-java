package com.example;

import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.stat.Correlation;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;


public class JavaCorrelationExample {

  public static void main() {
    // the following logger code is used to suppress all the info messages from spark
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);
    Logger.getRootLogger().setLevel(Level.OFF);


    SparkSession spark = SparkSession
      .builder()
      .appName("JavaCorrelationExample")
      .config("spark.master", "local")
      .getOrCreate();

    List<Row> data = Arrays.asList(
      RowFactory.create(Vectors.sparse(4, new int[]{0, 3}, new double[]{1.0, -2.0})),
      RowFactory.create(Vectors.dense(4.0, 5.0, 0.0, 3.0)),
      RowFactory.create(Vectors.dense(6.0, 7.0, 0.0, 8.0)),
      RowFactory.create(Vectors.sparse(4, new int[]{0, 3}, new double[]{9.0, 1.0}))
    );

    StructType schema = new StructType(new StructField[]{
      new StructField("features", new VectorUDT(), false, Metadata.empty()),
    });

    Dataset<Row> df = spark.createDataFrame(data, schema);
    df.show();
    Row r1 = Correlation.corr(df, "features").head();
    System.out.println("Pearson correlation matrix:\n" + r1.get(0).toString());

    Row r2 = Correlation.corr(df, "features", "spearman").head();
    System.out.println("Spearman correlation matrix:\n" + r2.get(0).toString());

    spark.stop();
  }
}