package gr.aueb.panagiotisl.mapreduce.wordcount;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;

import java.io.IOException;

public class SpotifyReducer extends Reducer<Text, DoubleWritable, Text, Text> {
    private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        // Reducer function to calculate the most danceable song and average danceability
        double maxDanceability = Double.MIN_VALUE;
        double sum = 0.0;
        int count = 0;
        String mostDanceableSongName = null;

        for (DoubleWritable value : values) {
            double danceability = value.get();
            sum += danceability;
            count++;

            // Identify the most danceable song based on danceability value
            if (danceability > maxDanceability) {
                maxDanceability = danceability;
                mostDanceableSongName = key.toString(); // Using the key as the most danceable song
            }
        }

        // Calculate the average danceability
        double avg = count > 0 ? sum / count : 0.0;

        // Extract the country and month from the key
        String[] keyParts = key.toString().split(": ");
        String country = keyParts[0];
        String dateString = keyParts[1];
        
        // Parse the date using the specified format
        //LocalDate date = LocalDate.parse(dateString.replaceAll("\"", ""), inputDateFormat);

            // Format the date to "yyyy-MM"
            //String month = outputDateFormat.format(date);

            // Emit the result in the desired format
        String result = "Most Danceable Song: " + maxDanceability + ", Average Danceability: " + avg;
        context.write(new Text(country + ": " + dateString), new Text(result));
        
        
    }
}
