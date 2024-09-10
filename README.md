# Hadoop MapReduce Project

## Overview

This project is part of the Programming Project I for the M.Sc. in Data Science at the Athens University of Economics and Business. It involves developing a MapReduce application using the Hadoop framework. The project is divided into two parts:
1. **Part I**: Running a provided MapReduce example on a Hadoop cluster.
2. **Part II**: Developing a custom MapReduce application to analyze Spotify song data.

## Prerequisites

To complete the project, you will need the following tools:
- [Vagrant](https://www.vagrantup.com/)
- [VirtualBox](https://www.virtualbox.org/) or other virtualization software
- [Java 8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
- [Maven](https://maven.apache.org/)
- [Apache Hadoop](https://hadoop.apache.org/)

The project setup uses a virtual machine created by Vagrant, which includes the necessary tools for development and testing. This virtual machine provides access to Java, Maven, and Hadoop services running in Docker containers.

## Part I: Running the Provided MapReduce Example

In this part, you will build and execute a MapReduce application using Hadoop. The example application processes a text file (e.g., *Moby Dick*).

### Steps to Execute

1. **Initialize the Virtual Machine**  
   Start the virtual machine using Vagrant:

   ```bash
   vagrant up
   ```

   Ensure that the setup completes successfully and the required Docker containers are running. You can verify this by connecting to the virtual machine and listing the Docker containers:

   ```bash
   vagrant ssh
   docker ps
   ```

2. **Build the Example Application**  
   Navigate to the project directory and build the provided MapReduce application using Maven:

   ```bash
   cd /vagrant/hadoop-mapreduce-examples/
   mvn clean install
   ```

3. **Copy the Application to the Namenode**  
   Copy the application JAR to the Hadoop namenode Docker container:

   ```bash
   docker cp /vagrant/hadoop-mapreduce-examples/target/hadoop-map-reduce-examples-1.0-SNAPSHOT-jar-with-dependencies.jar namenode:/
   ```

4. **Submit the Application**  
   Submit the application for execution:

   ```bash
   docker exec namenode hadoop jar /hadoop-map-reduce-examples-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

5. **Upload Input File to HDFS**  
   If you encounter an error regarding missing input files, download a text file and upload it to the Hadoop filesystem (HDFS):

   ```bash
   wget https://www.gutenberg.org/files/2701/2701-0.txt -O MobyDick.txt
   docker cp MobyDick.txt namenode:/
   docker exec namenode hdfs dfs -mkdir -p /user/hdfs/input
   docker exec namenode hdfs dfs -put MobyDick.txt /user/hdfs/input/
   ```

6. **View the Output**  
   After the job finishes, you can view the output with the following command:

   ```bash
   docker exec namenode hdfs dfs -text /user/hdfs/output/part-r-00000 | head -100
   ```

### Reporting

For Part I, include the following in your report:
- The input file URL used (e.g., *Moby Dick* from Project Gutenberg).
- Execution logs from the terminal.

## Part II: Custom MapReduce Application

In this part, you will develop a custom MapReduce application to analyze Spotify song data. The goal is to find the most “danceable” song for each country and month, and calculate the average danceability for each pair.

### Input File

Download the Spotify song data from the provided link. This file is a compressed `.csv` containing various attributes of Spotify songs. You will need to parse and process specific columns for your analysis.

### Steps to Develop the Application

1. **Map and Reduce Classes**  
   Implement the MapReduce classes in Java. Ensure that:
   - The application processes the input `.csv` file, ignoring the header row.
   - Each country-month pair outputs the most danceable song and its danceability.
   - The average danceability is calculated for each country-month pair.

2. **Combiner**  
   Use a combiner function if appropriate (i.e., if the function is commutative and associative) to optimize the intermediate data processing.

3. **Handling CSV Parsing**  
   Ensure that your CSV parser handles commas within quotes correctly. You can use the following regex-based approach:

   ```java
   String[] tokens = input.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
   ```

### Running the Application

Once the application is developed, follow similar steps to Part I for building, copying, and executing your custom MapReduce job on the virtual machine.

### Reporting

For Part II, include the following in your report:
1. Code for the Map and Reduce classes (with comments explaining the logic).
2. Sample output showing the most danceable songs and average danceability for country-month pairs.

## Execution

To execute the provided example and custom MapReduce applications:
- Use Vagrant to manage the virtual environment and Docker containers.
- Use Maven to build the Java applications.
- Use Hadoop commands to submit jobs, manage HDFS, and view job progress and results.
