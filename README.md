# expenses-app
This project was written on Java 17. Needs at least this Java version 
and Gradle installed to run, also MySql and Postman for testing APIs.

It can be built and run as follows:

1. git clone https://github.com/mariNa-kr98/expenses-app-frontend.git cd expenses-app
2. ./gradlew clean build
3. ./gradlew bootRun

Runs at port http://localhost:8080. Make sure that MySql is running and configured properly
as stated in application.properties.

Serves as the backend for expenses-app-frontend repository.
The user can insert, modify, delete and view their monthly expenses and income.
