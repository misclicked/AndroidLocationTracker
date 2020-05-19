# Android Location Tracker
- Navigation app: navigate ```user A``` form [NCKUCSIE](https://goo.gl/maps/B5V5brdFbGuDMofz8) to [國立成功大學榕園](https://goo.gl/maps/TY6PLecP8WCaPyfC8)
- Monitoring app: shows the real time location of ```user A```
# How to Run
## Apps: Build with Android Studio
- ```AndroidStudioProjects/Nav``` for Navigation app
- ```AndroidStudioProjects/Monitor``` for Monitoring app
## Server: Build with nodeJS
- go to ```LocationUpdateServer/LocationTracter/``` run ```sudo npm test``` or ```sudo node index.js```
## Change IP to your own
- In both apps' project file you'll see ip:```192.168.55.4```, just change to your own IP address
## Replace google_maps_key to your own key with ```Maps SDK for Android``` enabled
- in ```google_maps_api.xml```
## Replace google_direction_key to your own keys with ```Directions API``` enabled
- in ```google_maps_api.xml```

# Demo
[![Demo youtube](https://img.youtube.com/vi/tEfzAt6TMEo/0.jpg)](https://www.youtube.com/watch?v=tEfzAt6TMEo)

# Enviroment for Demo
- Android Studio 3.6.3
- Emulator for Monitoring app: 
```
Name: Pixel_2_API_R
CPU/ABI: Google Play Intel Atom (x86)
Path: /home/misclicked/.android/avd/Pixel_2_API_R.avd
Target: google_apis_playstore [Google Play] (API level R)
Skin: pixel_2
SD Card: 512M
fastboot.chosenSnapshotFile: 
runtime.network.speed: full
hw.accelerometer: yes
hw.device.name: pixel_2
hw.lcd.width: 1080
image.androidVersion.codename: R
hw.initialOrientation: Portrait
image.androidVersion.api: 29
tag.id: google_apis_playstore
hw.mainKeys: no
hw.camera.front: emulated
avd.ini.displayname: Pixel 2 API R
hw.gpu.mode: auto
hw.ramSize: 1536
PlayStore.enabled: true
fastboot.forceColdBoot: no
hw.cpu.ncore: 3
hw.keyboard: yes
hw.sensors.proximity: yes
hw.dPad: no
hw.lcd.height: 1920
vm.heapSize: 256
skin.dynamic: yes
hw.device.manufacturer: Google
hw.gps: yes
hw.audioInput: yes
image.sysdir.1: system-images/android-R/google_apis_playstore/x86/
showDeviceFrame: yes
hw.camera.back: virtualscene
AvdId: Pixel_2_API_R
hw.lcd.density: 420
hw.arc: false
hw.device.hash2: MD5:55acbc835978f326788ed66a5cd4c9a7
fastboot.forceChosenSnapshotBoot: no
fastboot.forceFastBoot: yes
hw.trackBall: no
hw.battery: yes
hw.sdCard: yes
tag.display: Google Play
runtime.network.latency: none
disk.dataPartition.size: 6442450944
hw.sensors.orientation: yes
avd.ini.encoding: UTF-8
hw.gpu.enabled: yes
```

- Emulator for Navigation app: 
```
Name: Pixel_2_API_26
CPU/ABI: Google Play Intel Atom (x86)
Path: /home/misclicked/.android/avd/Pixel_2_API_26.avd
Target: google_apis_playstore [Google Play] (API level 26)
Skin: pixel_2
SD Card: 512M
fastboot.chosenSnapshotFile: 
runtime.network.speed: full
hw.accelerometer: yes
hw.device.name: pixel_2
hw.lcd.width: 1080
image.androidVersion.codename: R
hw.initialOrientation: Portrait
image.androidVersion.api: 29
tag.id: google_apis_playstore
hw.mainKeys: no
hw.camera.front: emulated
avd.ini.displayname: Pixel 2 API 26
hw.gpu.mode: auto
hw.ramSize: 1536
PlayStore.enabled: true
fastboot.forceColdBoot: no
hw.cpu.ncore: 3
hw.keyboard: yes
hw.sensors.proximity: yes
hw.dPad: no
hw.lcd.height: 1920
vm.heapSize: 256
skin.dynamic: yes
hw.device.manufacturer: Google
hw.gps: yes
hw.audioInput: yes
image.sysdir.1: system-images/android-26/google_apis_playstore/x86/
showDeviceFrame: yes
hw.camera.back: virtualscene
AvdId: Pixel_2_API_26
hw.lcd.density: 420
hw.arc: false
hw.device.hash2: MD5:55acbc835978f326788ed66a5cd4c9a7
fastboot.forceChosenSnapshotBoot: no
fastboot.forceFastBoot: yes
hw.trackBall: no
hw.battery: yes
hw.sdCard: yes
tag.display: Google Play
runtime.network.latency: none
disk.dataPartition.size: 6442450944
hw.sensors.orientation: yes
avd.ini.encoding: UTF-8
hw.gpu.enabled: yes
```
- npm version ```6.14.4```
- node version ```v14.2.0```
# Contributor
- 趙哲宏 P76081378
- 孫祥恩 P76084708
- 王宇哲 P76084758
