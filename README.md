# How to Run
## Apps: Build with Android Studio
- ```AndroidStudioProjects/Nav``` for Navigation app
- ```AndroidStudioProjects/Monitor``` for Moniting app
## Server: Build with nodeJS
- go to L```ocationUpdateServer/LocationTracter/``` run ```sudo npm test``` or ```sudo node index.js```
## Change IP to your own
- In both apps' project file you'll see ip:```192.168.55.4```, just change to your own IP address
## Repleace YOUR_KEY_HERE to your own keys
- in ```AndroidStudioProjects/Monitor/app/src/debug/res/values/google_maps_api.xml```
- in ```AndroidStudioProjects/Monitor/app/src/release/res/values/google_maps_api.xml```
- in ```AndroidStudioProjects/Nav/app/src/debug/res/values/google_maps_api.xml```
- in ```AndroidStudioProjects/Nav/app/src/release/res/values/google_maps_api.xml```
