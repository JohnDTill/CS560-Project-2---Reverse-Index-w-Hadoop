#Grab input from previous stage
cp "../00 Document Acquisition/output/shakespeare_works.txt" "./input/shakespeare_works.txt"

#Compile source code
hadoop com.sun.tools.javac.Main WordCount.java
jar cf wc.jar WordCount*.class

#Remove the output folder if it exists
rm -r ./output

#Run the program
hadoop jar wc.jar WordCount ./input ./output
