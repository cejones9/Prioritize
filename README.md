# Prioritize
Prioritize is a priority-based personal assistant application created to help keep track and organize daily, weekly, and monthly tasks. It has been designed specifically with ease of use in mind, to simplify and streamline daily tasks. Prioritize differentiates itself from other calendar based applications with it's signature priority system, which will determine when Prioritize will notify the users of their events. 

Prioritize will utilize the Google Drive API, which will allow syncing of files between systems, as well as both online and offline functionality. Reminders will be stored in a SQLlite database locally, as well as in a JSON format for sharing between devices. Lookups into the database won't need to happen often, it is simply to store a list of reminder objects. Google Drive will handle the syncing aspect for us. If the user deletes the application, their data will remain linked to their Google Drive account unless deleted from there as well. This is beneficial, if a catastrophe occurs, their data will remain safe, provided they have synced with the Drive. In addition to data storage, Google Drive is beneficial in the security aspect as well, as it handles security and ecruption of data when being synced.

Requirements:
Prioritize requires an Android Device. Specific API number isn't decided yet, but the current aim for the minimum API level is 20 or 21.


Contributors: 
Joel Wilhelm (Documentation, JSON implementation and parser, Priority algorithm)
Shahrukh (GUI)
Wajahat (Notifications)
Cody Jones (Priority Algorithm, Google Drive API / SQLite Database)


