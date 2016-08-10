This is project is the server of my `Blog-System`;

Powered by JFinal.

Provided API for Android and Web.

For Android API,all data is `List<DataType>` though the number of the data is 1. By doing this,the Android can parse JSON convenient.

Architecture
------
Router：route the http request to a suitable Controller
Controller:the main component to do business logic
Service:operate Database by DAO-layer.(SQL)
DAO:operate the Database directly.


Shortcoming
-----
No use REST model.


