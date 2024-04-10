# OAuth2 Authorization Code Grant Sample Implementation

This is a sample implementation of authorization code grant with java. 

- Involves the user directly.
- User logs in to the authorization server *(e.g., Google, Facebook, Keycloak)* and grants permission to the client application to access their data.
- More secure as the client application never gets the user's credentials.

Here's a simplified breakdown:

1. User attempts to access a resource on your application.
2. Your application redirects the user to the authorization server's login page.
3. User logs in and grants permission to your application.
4. Authorization server redirects the user back to your application with an authorization code.
5. Your application exchanges the authorization code for an access token from the authorization server (using your client credentials).
6. Your application uses the access token to access the user's data on the resource server.

For simplicity all the tokens *(i.e. access and refresh tokens)* and session state are all stored as browser cookies, which is not ideal. I recommend that in the actual implementation only the access token is available in browser cookie if possible and both the refresh token and the session state must be persisted to somewhere only the application knows about *(e.g. database or file)*.

## Pre-requisite

* Java 17

## [Keycloak Configuration](docs/keycloak-configuration.md)

**Keycloak** is the **IDAM** use in this sample implementation. If you don't have it configured yet do the procedure from [here](docs/keycloak-configuration.md).

## Classes of Interests

All the classes mentioned here are within the following package:

```
xyz.ronella.sample.oauth.authcode.controller.impl
```

Yes, these are all controller endpoints.

### Home Class

The **Home class** is simple implementation of a protected resource. This is the only one must be accessed. It is accessible through the following address once the application is already running:

```
http://localhost:9010
```

The home class only check the existence of a valid access token.

### Refresh Class

The **Refresh class** is the one that the **Home class redirects into** if there is no valid access token. This class checks for the **availability of a valid refresh token**. If it exists, it uses it to request from IDAM a valid access token. Otherwise, it will redirect to the EntryPoint class.

### EntryPoint Class

The **EntryPoint class** is the one that redirect the user to **IDAM authentication page**. The user only reaches this endpoint if and only if there are not valid access token and refresh token. 

### Callback Class

The **Callback class** is the one that receives the **authorization token** *(i.e. from the IDAM authentication)* and use it to **request from IDAM a valid access token**. Once an valid access token was received, this class now redirects the user to the home page *(i.e. the protected resource.)*. A valid access token that we are mentioning here means that the token is not yet expired.

## The application.properties file

The **application.properties file** holds the configurations specific to the application such as the following:

* server.port 

  This is the port that the **server bind into**. This is defaulted to 9010 in this sample implementation. 

  Change this if the port 9010 was already in used.

* auth.url

  You can find this using the **Identifying the Endpoints** section in [keycloak configuration](docs/keycloak-configuration.md). Normally the following default is suffice:

  ```
  http://localhost:8080/realms/myrealm/protocol/openid-connect/auth
  ```

* token.url

  You can find this using the **Identifying the Endpoints section** in [keycloak configuration](docs/keycloak-configuration.md). Normally the following default is suffice:

  ```
  http://localhost:8080/realms/myrealm/protocol/openid-connect/token
  ```

* redirect.url

  This is the URL that the **IDAM authentication endpoints redirects into**. This is defaulted to the following:

  ```
  http://localhost:9010/callback
  ```

  If there's a change in server.port, update this property to reflect it.

* client.id

  You can find this using the **Creating a New Client and User section** in [keycloak configuration](docs/keycloak-configuration.md). Normally the following default is suffice:

  ```
  codeflowclient
  ```

* client.secret

  You can find this using the **Finding out the Client Secret section** in [keycloak configuration](docs/keycloak-configuration.md).

The application.properties file is located in the following directory:

```
<PROJECT_DIR>\conf
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

The **class that reads this file** is the following:

```
AppConfig
```

If want to add more/update configuration settings you can **update this properties file** and don't forget to also update the class that reads it. 

In the **actual package**, you can find this file in the following directory:

```
<APPLICATION_ROOT>\conf
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

## Application class

The **Application class** is the bootstrap of the server. By default, the server is listening on port **9010**. You can change this by updating it in the **application.properties** file. If you are planning to run the server using IDE. Run the following class:

```
xyz.ronella.sample.oauth.authcode.Application
```

If you've packaged it and wants to run the server, execute the following batch file:

```
<APPLICATION_ROOT>\auth-code-grant.bat
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

If you ran the server, expect to see the following output:

```
The app started on port 9010
Press enter to stop...
```

To test it: 

1. Ensure that **keycloak was configured and running**. 

2. **Open a browser** and use the following address:

   http://localhost:9010

   Expect to see the following output:

   > If it is accessed the first time, it will redirect to IDAM authentication page. See the **Creating a New Client and User section** in [keycloak configuration](docs/keycloak-configuration.md) to find you the credential to use.
   
   ```
   This is a protected page.
   ```
   

> If you want to **stop the server**, just **press the enter key**.
>

## The log4j2.xml file

The **log4j2.xml file** holds the logging configuration and it is located in the following location:

```
<PROJECT_DIR>\src\main\resources
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

In the **actual package**, you can find this file in the following directory:

```
<APPLICATION_ROOT>\conf
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb
