Zip weather collector
---------------------
1. Zipcode data obtained from "http://www.zipcodestogo.com/Texas/". Extracted the zipcode, county and city and stored it in zips.csv. The file uploaded in git is the first 20 records in zips.csv.
2. Uploaded a video on youtube that demos the data collector and the web service.
3. Uploaded the code in git. Let me know and I can send a zip if git doesn\'92t work.

Prerequisites to run app:
-------------------------
1. Install Elasticsearch: To setup ES, I ran the following steps on the Mac.

brew update
brew install elasticsearch
To have launchd start elasticsearch now and restart at login:
brew services start elasticsearch
Or, if you don't want/need a background service you can just run:
elasticsearch
then run the following APIs
curl -X PUT "http://localhost:9200/phunware"
2. Setup maven.


Run the data collector app. using the following commands:
--------------------------
    $ cd $application_directory.
    $ java -cp target/zipweather-1.0-SNAPSHOT-jar-with-dependencies.jar com.phunware.interviews.App src/main/resources/config.properties ~/wd/data/tz.csv

# web service to do multi get
# this is under the assumption that Y! Weather was able to respond with the correct data for the following zip codes
curl 'http://localhost:9200/phunware/zipweather/_mget' -d '{"ids" : ["75023", "75019", "75014"]}'

Config (config.properties):
--------------------------
$ cat src/main/resources/config.properties 
YAHOO_WEATHER_API_BASE_URL=http://query.yahooapis.com/v1/public/yql?q=
SLEEP_INTERVAL_BEFORE_RETRY=2000
NUM_YQL_RETRIES=5
ELASTICSEARCHURL=http://localhost:9200
PHUNWAREESINDEX=phunware

Algorithm:
----------
1. Data collector: Y! weather app probably does a rate limit and hence returns the results element as null if you hit it frequently. 
Hence the data collector application retries NUM_YQL_RETRIES # of times when it gets a rate limited response. 
Before each try, it sleeps for SLEEP_INTERVAL_BEFORE_RETRY milliseconds. 
Tune these 2 parameters in the config. file to get the weather info. for all 20 zip codes.
2. Elasticsearch: The phunware index contains the zip weather mapping that stores the weather document for each zip code (_id = zip code)
Zip code has been chosen as the id so that you can use it for the multi get.

