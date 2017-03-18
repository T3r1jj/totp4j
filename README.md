## tot4j

This module is pretty small but will allow for consistent generation of **Time-based One-time Password** in **Java**

#### Installation (Maven)
1. Create libs/ directory in the project root directory. **[Download jar from releases](/uploads/d2ad229b5674ac1d1fd7cb5943528646/totp4j-1.0.0.jar)** and place it in libs/ directory.

2. Add in-project repository
````Xml
<repository>
    <id>in-project</id>
    <name>libs</name>
    <url>file://${project.basedir}/libs</url>
</repository>
````
3. Add dependency
````Xml
<dependency>
    <groupId>io.gitlab.druzyna_a</groupId>
    <artifactId>totp4j</artifactId>
    <version>1.0.0</version>
</dependency>
````

#### Example usage
1. Securely acquire (accessing API) / relay (defining access to your own API) **INTERVAL**, **KEY**, **TOKEN_LENGTH** and **HMAC_ALGORITHM** beforehand (check other project pages / wikis). Create TOTP with mentioned parameters.
````Java
TOTP totp = new TOTP.Builder()
        .setInterval(INTERVAL)
        .setKey(KEY)
        .setT0(System.currentTimeMillis() / 1000)
        .setTokenLength(TOKEN_LENGTH)
        .setAlgorithm(HMAC_ALGORITHM)
        .createTOTP();
````
a) Validate integer token:
````Java
boolean valid = totp.isTokenValid(token);
````
b) Generate integer token:
````Java
int token = totp.generateToken();
````
