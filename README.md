<h1 align="center">Air Quality Monitoring</h1>

<p align="center">
This is an Android  App which shows live AQI (Air quality index) data for different cities using Web-socket.
<br>This project is for focusing especially on the new jetpack libraries.<br>
Also fetching data from the network and integrating persisted data in the database via repository pattern.
</p>
<br>

<p align="center">
  <img alt="home" src="https://github.com/kanikatandelnetweb/AirQualityMonitoring/blob/master/screenshots/citylist.jpg" width=200/>
  <img alt="graph" src="https://github.com/kanikatandelnetweb/AirQualityMonitoring/blob/master/screenshots/graph.jpg" width=200/>
</p><br>


## Features
- Live AQI data for cities.
- Detail screen which show live AQI chart for selected city.
- Good chart data visulization
- Reconnection to Socket once user comes from App Background mode.
- Error handling for Socket connection.
<br>
## Note
- Suppose AQI value is *50.23* which lies between *Good* and *Satisfactory* Categoty.
- In that case, it is consider to be in *Good* category. We can change this logic as required.
<br>
## Architecture
- MVVM code architecture pattern is used.
<br>
- City list
    - *ViewModel* of City list connects to the web socket and listens to the callbacks.
    - It creates *CityModel* if it doesn't already exist (using city name) and adds
    *AQIModel* in aqi history array. If city already exist, it just adds *AQIModel*.
    - Once datasource is prepared, *ViewModel* updates *View* using delegate to refresh the table.
    - If there is error in socket connection, it updates *View* and user is promoted with error message with *Retry* button.
<br>
- AQI chart for city
    - Once user select any city, it will pass *CityModel* to the detail screen. As data is shared among two screen, *Class* is used so it automatically refresh the chart.
    - *View* will ask *ViewModel* to prepare datasource based on history data and after Chart data is prepared, it will updates *View* using delegate to refresh the chart.
    - If there is an AQI update for selected city, *ViewModel* of City list will update *ViewModel* of chart view, which will updated the chart datasource and will update to *View* to refresh the chart.
<br>
## Logic
- Create city list
    - Response array is looped through and existing datasource is checked whether it contains a city with *cityName*. If not, new instance is created for city and AQI record is added to history.
    - AQICategory Enum is created based on the AQI value and each category is assigned Title and Color.
<br>
- Create chart data
    - AQI history is looped through and array of *ChartDataEntry* is created and then *Data set* is created.
    - X-Axis time values are assigned through *dataPoints* array.
    - For the sake of demo and for better chart UI, only 20 records are stored for AQI history.
<br>
## Time taken
- Choose socket library and its use: 2 Hours
- UI Design: 4 Hours
- Prepare datasource / Different logic etc: 4 hours
- Detail screen: 2 hours
- Chart library integration / Chart datasource / UI configuration: 4 hours
- Error handling and UI: 1 hour
<br>
## Libraries and tech used
- Hilt for dependency injection.
- JetPack
  - Flow - notify domain layer data to views.
  - Lifecycle - observing data when lifecycle state changes.
  - ViewModel - lifecycle aware UI related data holder.
  - Room Persistence - construct a database to store city aqi data.
  <br>
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [OkHttp3](https://github.com/square/okhttp) - for WebSocket connection.
- [Gson](https://github.com/google/gson/) - A JSON library for parsing network response.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for CardView.
<br>
