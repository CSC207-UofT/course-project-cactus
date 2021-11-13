# Cactus
Grocery management application that currently provides the following functionalities:
- Create an account
- Create, edit, and save grocery lists

This project contains the Cereus Android application, and the Saguaro server.

## System Requirements
Java 11 or later

Android 11 (API Level 30)

## Setup

### Static IP
In order for the Android app to connect to the server, your device must have a static IP. The instructions for how to set this can be found [here for Windows](https://kb.netgear.com/27476/How-do-I-set-a-static-IP-address-in-Windows) or [here for Mac](https://www.macinstruct.com/tutorials/how-to-set-a-static-ip-address-on-a-mac/). 

By default, Cereus assumes that Saguaro is located at `192.169.0.127`. In other words, it assumes that your computer has this static IP. However, you can specify a different IP by creating a `network.properties` file in `cereus/src/main/resources`, and adding the property `staticIp`.

### Android SDK

As part of the Gradle project build, the correct Android SDK version should be downloaded. However, sometimes Gradle may not be able to find the SDK, causing the build to fail. Should this be the case, create a `local.properties` file at the root of the project, and add the property `sdk.dir` mapping to your SDK location.

## Run

[comment]: <> (TODO)