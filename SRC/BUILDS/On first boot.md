This is going in this folder for the sake of reminding me.

On the apps first initial lost two things need to happen:
1-
String uniqueID = UUID.randomUUID().toString();
this provides a unique ID for use with the google drive syncing strategy
I will append a int (0) to the end of the string, and every time this device adds an alarm that int will be extracted, incremented, and replaced back into the string

2-
The user will pick the google account that he or she will be using for syncing.
