library(gutenbergr)
library(dplyr)
library(stringr)
	
#Look for Shakespeare in the local metadata file,
#excluding collections (containing "Works") and translations
shakespeare_metadata <- gutenberg_metadata %>%
	filter(author == "Shakespeare, William",
		language=="en",
		!str_detect(title,"Works"),
		has_text)

#Eliminate redundant entries
shakespeare_unique = shakespeare_metadata[ t( !duplicated(shakespeare_metadata$title) ), ]

#Create a table with Shakespeare's works
shakespeare_works <- gutenberg_download(shakespeare_unique$gutenberg_id)
#readRDS(file = 'output/shakespeare_works.rds')

#Write the table to disk
saveRDS(shakespeare_works, "output/shakespeare_works.rds") #save in R's format
write.table(shakespeare_works, file = 'output/shakespeare_works.txt', sep = '\t', row.names = FALSE) #save as text

#Write a file for the ID/title correspondence
mapping <- data.frame(id=shakespeare_unique$gutenberg_id, title=shakespeare_unique$title)
write.table(mapping, file = 'output/id_mapping.txt', sep = '\t', row.names = FALSE)
