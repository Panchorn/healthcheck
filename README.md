Run program by,
$ java -jar ./target/healthcheck-0.0.1-SNAPSHOT.jar

API,
method: POST
endpoint: http://localhost:8443/healthcheck/api/websites/validation
body: use form-data with file .csv (key is 'file')

Remark,
Program read file by skip the header

File test,
healthcheck/src/main/resources/websites.csv

Run unit test by,
$ mvn test
