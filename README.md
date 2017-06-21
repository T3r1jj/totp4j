## totp4j

This library provides a way to generate and validate **Time-based One-time Password** (token) in **Java**

#### Installation (Maven)
1. Create libs/ directory in the project root directory. Download jar from releases and place it in **libs/io/gitlab/druzyna_a/totp4j/1.1.0/** directory.

2. Add in-project repository
````Xml
<repository>
    <id>in.project</id>
    <name>libs</name>
    <url>file:${project.basedir}/libs</url>
</repository>
````
3. Add dependency
````Xml
<dependency>
    <groupId>io.gitlab.druzyna_a</groupId>
    <artifactId>totp4j</artifactId>
    <version>1.1.0</version>
</dependency>
````

#### Example usage
Securely establish common **INTERVAL**, **KEY**, **TOKEN_LENGTH** and **HMAC_ALGORITHM**. Create TOTP with the mentioned parameters. The resultant token will be an int.

````java
TOTP totp = new TOTP.Builder()
        .setInterval(INTERVAL)
        .setKey(KEY)
        .setT0(System.currentTimeMillis() / 1000)
        .setTokenLength(TOKEN_LENGTH)
        .setAlgorithm(HMAC_ALGORITHM)
        .createTOTP();
````
a) Validate integer token:

````java
boolean valid = totp.isTokenValid(token);
````
b) Generate integer token:

````java
int token = totp.generateToken();
````

### License
Copyright 2017 Damian Terlecki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.