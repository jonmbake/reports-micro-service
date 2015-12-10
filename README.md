# jasperreports-micro-service

A RESTful micro service for running [Jasper Reports](http://community.jaspersoft.com/).  Easily deployable to a [AWS Elastic Beanstalk Java 8/Tomcat8 Instance](https://aws.amazon.com/about-aws/whats-new/2014/11/05/aws-elastic-beanstalk-supports-java8-tomcat8/).

## Authentication

Authentication is done via the consumption of [JWT tokens](http://jwt.io/).  The JWT token must be submitted as a query parameter in order to authenticate.  The JWT secret must be specified as a config (see below).

Security Note:  Make sure to enable *https* when using this micro service.

## Authorization

Currently any user with a valid token can run any available report.  Authorization might be enabled in a future release.

## Configuration

Configuration values are specified through environment variables.

Name               | Required? | Notes/Description
------------------ | --------- | ---------------------------
REPORTS_DIR | Yes       | Direction where report definition (*.jrxml files) live.
JWT_SECRET         | Yes       | JWT secret.  Needed for authentication.
JWT_AUDIENCE       | No        | If provided, will also validate JWT audience during authentication.
JWT_ISSUER         | No        | If provided, will also validate JWT issuer during authentication.
JDBC_CONNECTION_STRING | No | Database connection string.  For example, jdbc:mysql://localhost:3306/mydatabase?user=me&password=mypassword.  If blank will run reports against an empty data source.

If a configuration value is not properly set, the application will throw a `ConfigurationException` on startup.  it may be necessary to restart the server after changing a config value.

### Uploading Reports

Reports should be uploaded to the `REPORTS_DIR`.

## The API

The API is **really** simple.  There is a method for getting info on available reports and another for running a report.

### Obtaining Info on Available Reports

The URL to run a report is of the form `/reports-api/reports/info`.  For example:

```
https://localhost/reports-api/reports/info?jwt=eyJhb...
```

This will return json of the form `{names: ['JasperRepor1', 'JasperRepor2']}`.

### Running a Report

To run a report, the sub-url is `/reports-api/reports/filled/{report-name}`.  For example:

```
https://localhost/reports-api/reports/filled/JasperReport1?fromDate=2015-10-10thruDate=2015-10-10jwt=eyJhb...
```

Every query parameter besides `jwt` will be passed in as report parameter.  The PDF version of the filled report is returned.

*Note:* The service uses version 6.2.0 of the Jasper Reports Library.  Make sure to use the corresponding version Jaspersoft Studio.

## Deploying to AWS

Once your Java 8/Tomcat8 instance and database are set up.  Simply set up the environment configs and copy over [report-api.war](https://github.com/jonmbake/jasperreports-micro-service/blob/master/target/report-api.war?raw=true) to Tomcat's `webapp` directory.  Copy over report definitions to `REPORTS_DIR`.

# License

MIT

Jasper Reports Library is licensed under LGPL.
