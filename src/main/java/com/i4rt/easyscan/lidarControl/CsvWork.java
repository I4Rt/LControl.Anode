package com.i4rt.easyscan.lidarControl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class CsvWork {
    private static final String CSV_FILE = "D:\\GitHub\\EasyScan\\src\\main\\test.csv";

    public static void runCSV(List<Double> coordinates) throws IOException {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("x;y;z;angle;refl"));
        ) {
            //for (int j = 0; j < 300; j++){
            //    for (int i = 0; i < xg.length; i++){
            //        String text = xg[i] + ";" + yg[i] + ";" + (j + 1) + ";" +
            //                angles[i] + ";" + (int) refl[i];
            //        csvPrinter.printRecord(text);
            //    }
            //}
            for (int i = 0; i < coordinates.size()/5; i++){
                String text = (coordinates.get(i * 5)) + ";" + (coordinates.get(i * 5 + 1)) + ";" +
                        (coordinates.get(i * 5 + 4)) + ";" + (coordinates.get(i * 5 + 3)) + ";" +
                        (coordinates.get(i * 5 + 2));
                csvPrinter.printRecord(text);
            }
            csvPrinter.flush();
        }
    }
}