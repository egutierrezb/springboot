# MoonPhase API

SpringBoot Application which allows REST API calls for fetching Moon Phases according to year and month values

## Accessing to API Documentation

Once your application is run, you can go through `localhost:8080/swagger-ui.html` and you will find the documentation
for the MoonPhase API application. There are two main endpoints `/api` and `/api2`. The first one contains allows to the user to inject (POST) their own JSON Objects with customized information of moon phases. Then the user can fetch/retrieve data from different API calls.
The second endpoint has the algorithm for computing moon phases according to year and month values, as well for a specific year in the case of the full moon and new moon phases. 
API documentation was provided for both endpoints. Testing was done only for `/api`endpoint. 
