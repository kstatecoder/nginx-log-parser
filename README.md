# nginx-log-parser

Prerequisites:
JDK 7 or higher
Maven 3.1 or higher

if you don't have either one of these installed you have a couple of options.  

1) install the prequesties via homebrew 
2) Manually install both from the following websites for your given platform

## Getting Started
`git clone .....`

Then inside of the project directory (ie. nginx-log-parser) run the following maven command :
`mvn clean package`

### Defaults
Read access file : `/var/log/nginx/access.log` 
Write statsd file: `/var/log/stats.log`
After the mvn clean package has completed successfully it is now time to startup the log parser with the following command at the root of the project directory :
`java -jar ./target/nginx-log-parser-0.0.1-SNAPSHOT.jar`

