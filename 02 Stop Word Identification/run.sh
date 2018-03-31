#Grab input from previous stage
cp "../00 Document Acquisition/output/shakespeare_works.txt" "./input/shakespeare_works.txt"

#Compile source code
hadoop com.sun.tools.javac.Main StopWords.java
jar cf sw.jar StopWords*.class

#Remove the output folder if it exists
rm -r ./output

#Run the program
hadoop jar sw.jar StopWords ./input ./output
