# Keycloak – Authorization Code Grant Type - Configuration

This configuration procedure is specific to **OAuth2 Authorization Code Grant Sample Implementation**. This is to make the implementation works and anyone can play with it. 

## Pre-requisite

* Keycloak v21.0[^1]

  Do avoid too much configuration and if you can use docker container use the following command only for the first time:

  ```
  docker container run -d -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin123% -p 8080:8080 -p 8443:8443 --name keycloak keycloak/keycloak:21.0 start-dev
  ```
  
  > To **stop the container** use the following command:
  >
  > ```
  > docker container stop keycloak
  > ```
  
  > To **start the container** use the following command:
  >
  > ```
  > docker container start keycloak
  > ```

## Creating a New Client and User

1. **Sign in** to **keycloak admin console** using the following address:

   > Must know the a valid credential. 

   > If the docker command in the pre-requisite was used the credential is as follows:
   >
   > | Field    | Value     |
   > | -------- | --------- |
   > | Username | admin     |
   > | Password | admin123% |

   http://localhost:8080/admin/

2. **Create** a **new realm** named **myrealm** *(i.e. our realm for this sample)*: 

   1. **Click** the **dropdown** after the **current realm** *(i.e. master)*.

   2. **Click** the **Create Realm button**.

      ![create-realm](images/create-realm.png)

   3. **Fill-in** the **Realm name field** with **myrealm** *(i.e. the realm use in this sample implementation)*.

   4. **Click** the **Create button**.

      ![new-realm](images/new-realm.png)

      > Expect to see that the current realm is now **myrealm**.

3. **Create** a **new client** as follows:

   1. **Click** the **Clients menu**.

   2. **Click** the **Create client button**.

   3. **Ensure** that **OpenID Connect** is the **Client type**.

   4. **Provide** a **Client ID** named **codeflowclient** *(i.e. the client use in this sample implementation)*.

   5. **Click** the **Next button**.

      ![general-settings](images/general-settings.png)

   6. **Enable** the **Client authentication**.

   7. **Enable Authorization**.

   8. In the **Authentication flow**, **unselect** the **Direct access grants**.

   9. **Click** the **Next button**.

      ![capability-config](images/capability-config.png)

   10. **Fill-in** the **Valid redirect URIs** for the redirection that will normally receive the **access token**. In this sample implementation, we will use the following:

       ```
       http://localhost:9010/callback
       ```

   11. **Click** the **Save button**.

       ![login-settings](images/login-settings.png)

   

4. **Create** a **new user** as follows:

   1. **Click** the **Users menu**.

   2. **Click** the **Add user button**.

   3. **Fill-in** the **username field** with **testuser** *(i.e. the user use in this sample implementation)*.

   4. **Click** the **Create button**. 

      ![create-user](images/create-user.png)

   5. **Click** the **Credentials tab**.

   6. **Click** the **Set password button**.

   7. **Fill-in** the **Password field** with **test1234** *(i.e. the user password use in this implementation)*.

   8. **Fill-in** the **Password confirmation field** with **test1234**.

   9. **Turn-off temporary**.

   10. **Click** the **Save button**.

      ![set-password](images/set-password.png)

   11. **Click** the **Save password button**.

       > Note the following test account credential that is use in this sample implementation:
       >
       > | Field    | Value    |
       > | -------- | -------- |
       > | Username | testuser |
       > | Password | test1234 |

## Identifying the Endpoints

1. **Click** the **Realm settings menu** of **myrealm**.

   ![](images/realm-settings.png)

2. **Ensure** that the **General tab** is **active**.

3. At the **Endpoints section** click the **OpenID Endpoint Configuration**.

   ![](images/myrealm-general.png)

4. On the opened **browser**, you are interested in the **following entries**:

   * authorization_endpoint
   * token_endpoint

   ![](images/end-points.png)

## Finding out the Client Secret

The **client secret** is needed when requesting for **authorization token** and **refreshing the access token**.

1. In **Keycloak**, **ensure** that you are in the **myrealm**.

2. **Load** the client **codeflowclient**.

3. **Click** the **Credentials tab**.

4. **Look** for **Client secret**.

5. **Click** the **Copy icon** *(alternatively, you can display the secret by clicking the eye icon.)*.

   ![](images/client-secret.png)

[Back](../README.md)

[^1]: https://www.keycloak.org/archive/downloads-21.0.0.html