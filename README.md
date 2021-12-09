# Cactus
Grocery management application that currently provides the following functionalities:
- Create an account
- Create, edit, and save grocery lists

This project contains the Cereus Android application, and the Saguaro server.

## System Requirements
Java 11 (Amazon Coretto 11 recommended)

Android 11 (API Level 30) SDK

## Setup

### Static IP
In order for the Android app to connect to the server, your device must have a static IP. The instructions for how to set this can be found [here for Windows](https://kb.netgear.com/27476/How-do-I-set-a-static-IP-address-in-Windows) or [here for Mac](https://www.macinstruct.com/tutorials/how-to-set-a-static-ip-address-on-a-mac/). 

By default, Cereus assumes that Saguaro is located at `192.168.0.127`. In other words, it assumes that your computer has this static IP. However, you can specify a different IP by creating a `network.properties` file in `cereus/src/main/resources`, and adding the property `staticIp`.

### Android SDK

As part of the Gradle project build, the correct Android SDK version should be downloaded. However, sometimes Gradle may not be able to find the SDK, causing the build to fail. Should this be the case, create a `local.properties` file at the root of the project, and add the property `sdk.dir` mapping to your SDK location.

## Test

To run all tests/checks, execute the following command at the root of the project
```bash
./gradlew check
```

Alternatively, run the `check` task from Intellij's Gradle window.

## Run

Intellij should recognize both the Spring application and Android application within the project, and automatically create appropriate run configurations. Start both applications by selecting the appropriate run configuration and clicking the "run" button at the top of the IDE window.

If Intellij was unable to create the correct run configurations, open the run configurations dropdown and select "Edit Configurations". Click the plus symbol in the top left of the popup window to create a new run configuration.

#### Saguaro:

- Create a new "Spring Boot" run configuration
- Set "Use classpath of module" to `course-project-cactus.saguaro.main`
- Set "Main class" to `com.saguaro.SaguaroApplication`
- Apply changes and exit

#### Cereus:

- Create a new "Android App" run configuration
- Set "Module" to `course-project-cactus.cereus`
- Apply changes and exit

## Documentation

Javadoc for Saguaro can be found at `javadoc/index.html`, as well as obviously in the codebase. Documentation for Cereus exists in the codebase only.