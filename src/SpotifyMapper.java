package gr.aueb.panagiotisl.mapreduce.wordcount;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;


public class SpotifyMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() > 0){
            String[] tokens = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            // Extract relevant information from the CSV columns
            String country = tokens[6];
            String dateString = tokens[7];
            LocalDate date = LocalDate.parse(dateString.replaceAll("\"", ""), inputDateFormat);

            // Format the date to "yyyy-MM"
            String month = outputDateFormat.format(date);
            double danceability = Double.parseDouble(tokens[13].replaceAll("\"", ""));

            // Create country-month key
            String countryMonthKey = country + ": " + month;

            // Emit country-month key and danceability value
            context.write(new Text(countryMonthKey), new DoubleWritable(danceability));
        }
    }
}
