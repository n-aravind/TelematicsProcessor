# TelematicsProcessor
**Assignment:** Write a producer program which writes telematics to files and write a consumer program which reads the files, calculates some averages, and writes to an html file.

**Producer**

The producer program shoud run in an infinite loop taking the following input from the console

- VIN
- Odometer miles
- Gallons of fuel remaining
- Current date/time

These values should be written to a file with the name being the current time in milliseconds and a suffix "json". For example 
1564931382194.json

**Consumer**

The consumer program shoud run in an infinite loop reading the files that were written by the producer and processing them. After processing the telematics file delete it from the file system.

After processing a telematics json file the consumer should write to an html. The top of the html file should show

- The number of telematics entrys
- The average odometer reading
- The average fuel remaining

The rest of the html file should be a list of the individual telematics entries (most recent on top).

Note that the consumer should write an empty html file when first starting up and then overwrite the file as it processes the telematic entries. 

Put a META refresh in the html file so that it refreshes by itself. 

Also note that the consumer should sleep (maybe for 500 milliseconds) in the loop.