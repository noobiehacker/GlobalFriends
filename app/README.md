Sashimi
=======

- An Android Front End solution to save time and hassle for recreation sports league players and admins
to find their desired leagues through our Mitoo platform

Features
--------

- Understand what Mitoo Is
- Login
- Reset Password
- Compeition Search

Installation:
--------------

- Use Android studio to open the project, IntelliJ works too but I think it runs better with AS
- download the latest Android SDK and JDK, JRE environment (I am using Java 1.8 and Android API 21)

http://www.oracle.com/technetwork/java/javase/downloads/index.html
https://developer.android.com/sdk/installing/index.html?pkg=tools
https://developer.android.com/sdk/installing/studio.html

Screen Sizes
--------------

- currently optimized for Nexus 5 and XXhdpi phones
- this application will support only phones with Ice cream sandwitch and up

Running the app
--------------

- I advice using Genymotion as the emulator for running it, another option is to use an Android device
- To run app: go to Build->Make Project, than Run->Run App
- The API that I am hitting is my local rails server, you can change the endpoint in the String.properties file

http://www.genymotion.com/

Trouble
--------------

- Android Studio has a bug, if it can't find your emulator try killing the ADB server and restart both
Android Studio + Genymotion
- libraries and dependency is mainly managed with Gradle, it is similar to RVM for a ruby dev's perspective

Architecture
--------------

- A lot of the architecture is referenced from futurice's best practice guide : https://github.com/futurice/android-best-practices .
- A lot of system classes uses Singleton for now, will use an Injection library to decrease the coupling
- Fragments (View and Controller) that are connected to our Models, which get data through Network or Local Storage
- All of the view related stuff should be in the fragment, the activity is mainly served as a master controller for all the system data related content
- Model stores data and uses Otto for event handling to connect with Fragment and our Services
- MitooActivity is the base activity and all the other views are fragments that gets switched depending on the event
- Network Access uses a ServiceFactory to set the endPoints and create the services that we need to access (Steak , Algolia etc)
- For SteakAPI, it will return an Observer to do asynchronous access and not block anything that the app
needs to use.  After it has recieved the object, it will push the object to its subscriber which will post
an event to eventbus to pass it to the right part of the component

Important Libraries
--------------

- Otto for event busing to create an MVC like architecture
- Retrofit + OKHTTP and Jackson for API access
- A lot of the UI stuff are referenced from Android Arsenal such as AndroidViewAnimation

Services classes / Tricky Pieces
--------------
- Location Service Management is pretty tricky and need some more research on how to do it properly.
For now the user will only be prompt to turn on location services when he/she goes to the competition
search page, which will than cause a lag because it will take around a second to get GPS data( or even get GPS
data at all), I purpose to have another prompt when we first open the app so the duration between the prompt and when
the user uses that feature will be lenghten which allows more time for location services to get GPS
in the background
- In order to speed up compilation time for debuging, can use offline mode for gradle:  Android Studios ->
Preference -> Gradle -> Offline Mode

Running Test
--------------

- Test will be broken into two different frameworks.  One of them will be POJO test that just runs normal
junit 3.
- For Android and UI specifc tests, I am using espresso framework that is based on Robotium

License
-------