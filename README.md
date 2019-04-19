# OTP-testapp
An app which saves data to Firebase Database using OTP authentication.

Steps for using this app:
1. First of all make sure the permissions INTERNET, CAMERA and STORAGE are true.
2. Then after starting the app for the first time make sure you click the photo first using the camera provided in the app.
3. After the photo is clicked then enter the phone number.
4. Then click the button verify OTP. So here two cases occur:
  a. If you're using the same phone for authentication the app will automatically identify OTP and you won't even have to enter the  OTP for authentication.
  b. If you use different phone for authentication you'll have to enter the OTP. So now two more cases occur:
      -If the OTP is correct your photo and phone number gets uploaded to "visitors" child in Firebase API
      -If the OTP is incorrect your photo and phone number gets uploaded to "suspicious_users" in Firebase API
  The data is presented to activities.
