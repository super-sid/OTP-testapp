# OTP-testapp
An app which saves data to Firebase Database using OTP authentication.

## Steps for using this app:
1. First of all make sure the permissions for INTERNET, CAMERA and STORAGE are true for this app.
2. Then after starting the app for the first time make sure you click the photo first using the camera provided in the app.
3. After the photo is clicked then enter the phone number.
4. Then click the button verify OTP. So here two cases occur:
  - If you're using the same phone for authentication the app will automatically identify OTP and you won't even have to enter the  OTP for authentication.
  - If you use different phone for authentication you'll have to enter the OTP. So now two more cases occur:
      - If the OTP is correct your photo and phone number gets uploaded to `visitors` child in Firebase API
      - If the OTP is incorrect your photo and phone number gets uploaded to `suspicious_users` in Firebase API

The data in APIs is fetched then to respective activities.

## Tools and Resources 

The OTP authentication used in this app is done using the [Firebase Authentication](https://firebase.google.com/docs/auth/android/phone-auth).

For Uploading Image Files to database and API, [Firebase Cloud Storage](https://firebase.google.com/docs/storage/android/start) was used.

The Camera used in this app is not the Native Camera App but an external library called [CameraKit](https://github.com/CameraKit/camerakit-android).

The whole app uses [Material Design Library](https://github.com/material-components/material-components-android) by Google for its User interface.

## Database Structure
![alt text](https://raw.githubusercontent.com/super-sid/OTP-testapp/master/firebase.JPG)
