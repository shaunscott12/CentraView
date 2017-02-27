This is the README file on usage of Authentication of users on LDAP and AD 

1. Dependencies: javax.naming package

2. Component: DirectoryAuthentication

The compoenent provides authentication on the desired directory and validating the 
desired attributes of the user authenticated on the directory.

The attributes should always be used from the ones available as public properties of the component.

3. How to use:

a. Authenticating the user:

Set the SERVER (public IP or fully qualified name), 
PORT (if different from 389), 
USERNAME (assumed to be of the form xyz@abc.com/co.uk etc) and PASSWORD properties on the component. 

Call on to AuthenticateUser function which will throw an exception if the user cannot be authenticated on the desired directory

b. Verifying attribues

There are already public attribute names defined with the component.
If any additional attributes are required, they should be defined similarly.
Ideally, attributes from this set should be used for this purpose just to maintain the uniformity. 

Use the compareAttribute function to verify the attribute value for the already authenticated user.

c. refreshAllAttributes - Use this function if you want to force the refreshing information for the user from the directory