// Activity

// Read CSV
val df_ses_log_csv = spark.read.options(Map("inferSchema"->"true","delimiter"->",","header"->"true")).csv("F:/Chapter06/Data/session_log.csv")

// Show the DF
df_ses_log_csv.show(false)


// Using CSV Data frame to write in different file formats

// Writing AVRO
df_ses_log_csv.write.format("avro").save("F:/Chapter06/Data/session_log.avro")
// Output: 454 MB


// Writing ORC
df_ses_log_csv.write.orc("F:/Chapter06/Data/session_log.orc")
// Output: 182 MB


// Writing PARQUET
df_ses_log_csv.write.parquet("F:/Chapter06/Data/session_log.parquet")
// Output: 115 MB



// Query Performance

// To execute perfomance metric, first we will read the data
// Read the data frame
var df_ses_log_parquet = spark.read.parquet("F:/Chapter06/Data/session_log.parquet")
var df_ses_log_orc = spark.read.orc("F:/Chapter06/Data/session_log.orc")
var df_ses_log_avro = spark.read.format("avro").load("F:/Chapter06/Data/session_log.avro")


// Function for time consumption

def time[A](f: => A) = {
	val s = System.nanoTime
	val ret = f
	println("time: "+(System.nanoTime-s)/1e6+"ms")
	ret
  }


// Query SET 1
// Count query within time function

time{df_ses_log_avro.groupBy("session_nb").count()}
time{df_ses_log_parquet.groupBy("session_nb").count()}
time{df_ses_log_orc.groupBy("session_nb").count()}



// Query SET 2
// Group By query (Executing SQL over data frame)

// Creating a table from the data frame 
// Parquet
df_ses_log_parquet.createOrReplaceTempView("session_log_parquet")
// ORC
df_ses_log_orc.createOrReplaceTempView("session_log_orc")
// AVRO
df_ses_log_avro.createOrReplaceTempView("session_log_avro")


// Defining sql query as a variable
val p_yr_query = "Select count(Year),Year from (Select SUBSTRING(event_date,7,10) as Year from session_log_parquet) GROUP BY Year"


val o_yr_query = "Select count(Year),Year from (Select SUBSTRING(event_date,7,10) as Year from session_log_orc) GROUP BY Year"


val a_yr_query = "Select count(Year),Year from (Select SUBSTRING(event_date,7,10) as Year from session_log_avro) GROUP BY Year"

// Executing the query inside the time function
time{spark.sql(p_yr_query)}
time{spark.sql(o_yr_query)}
time{spark.sql(a_yr_query)}
