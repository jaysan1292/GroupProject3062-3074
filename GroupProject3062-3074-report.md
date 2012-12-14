##COMP 3062/COMP 3074 Group Project
###GBC Scavenger Hunt
Jason Recillo (100726948), Peter Le (100714258), Mellicent Dres (100726767)

---

#####App Name
This application is simply called "GBC Scavenger Hunt".

#####Solutions
For the mobile app, we elected to use QR codes to verify check-ins to each checkpoint, as it is very easy to implement and a lot of information can be stored in a QR code.

For the web application, there are only two major servlets (LoginServlet and AjaxServlet). Obviously, LoginServlet only handles the log-in process. It does this by passing on the credentials to the web service back-end, which then verifies it against the login credentials stored in the database (no, passwords are *not* stored in the database as plaintext). AjaxServlet handles every request made from home.jsp, the central page where everything gets done. Evidently, all real work done by the application is done via AJAX get/post requests to http://WEBAPP\_HOST/ajax, which will then forward updates to the web service back-end, (i.e., http://WEB\_SERVICE\_HOST/service/players for player information, or http://WEB\_SERVICE\_HOST/service/scavengerhunts for game information).

#####Technical Details/Other Notes
As mentioned previously, the whole project is managed using Maven 3, so development is not tied to any single IDE. Compiling the entire project is as easy as typing `mvn compile` at a command prompt.* Of course, if that doesn't work, it *should* be simple enough to import to Eclipse: 

`mvn eclipse:eclipse` 

This will generate Eclipse project files so you can import the project into Eclipse (Note: I haven't tried this).

The Android project requires a minimum API level of 15.

As Maven manages all used libraries, jar dependencies should not be a problem.

*For compiling the Android project, your `$ANDROID_HOME` environment variable will have to be set to wherever your Android SDK is located.