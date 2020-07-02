// Read CSV
var df_census_csv = spark.read.options(Map("inferSchema"->"true","delimiter"->",","header"->"true")).csv("F:/Chapter06/Data/Census.csv")

// Read JSON
var df_census_json = spark.read.json("F:/Chapter06/Data/Census.json")

// Show the df
df_census_csv.show()
df_census_json.show()

// Writing to AVRO format

// Using CSV Data frame
df_census_csv.write.format("avro").save("F:/Chapter06/Data/Output/census_csv.avro")

// Using JSON Data frame
df_census_json.write.format("avro").save("F:/Chapter06/Data/Output/census_json.avro")


// Reading AVRO file
var df_census_avro = spark.read.format("avro").load("F:/Chapter06/Data/Output/census_csv.avro")
df_census_avro.show()
