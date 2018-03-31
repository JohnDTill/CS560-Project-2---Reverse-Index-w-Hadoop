#Grab input from previous stages
cp "../00 Document Acquisition/output/shakespeare_works.txt" "./input/shakespeare_works.txt"
cp "../02 Stop Word Identification/output/part-r-00000" "./StopWords.txt"

#Prepend line numbers to Shakespeare works
javac LinePrepender.java
java LinePrepender
rm ./input/shakespeare_works.txt

#Compile source code
hadoop com.sun.tools.javac.Main InvertedIndexBuilder.java
jar cf iib.jar InvertedIndexBuilder*.class

#Remove the output folder if it exists
rm -r ./output

#Run the program
hadoop jar iib.jar InvertedIndexBuilder ./input ./output

#Move the result
cp "./output/part-r-00000" "../04 Inverted Index Search/InvertedIndex.txt"
