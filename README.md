# Prioritize
Prioritize is a priority-based personal assistant application created to help keep track and organize daily, weekly, and monthly tasks. It has been designed specifically with ease of use in mind, to simplify and streamline daily tasks. Prioritize differentiates itself from other calendar based applications with it's signature priority system, which will determine when Prioritize will notify the users of their events. 

Prioritize will utilize the Google Drive API, which will allow syncing of files between systems, as well as both online and offline functionality. Reminders will be stored in a SQLlite database, as well as locally, in a JSON format. Lookups into the database won't need to happen often, it is simply to store a list of reminder objects. Google Drive will handle the syncing aspect for us. If the user deletes the application, their data will remain linked to their Google Drive account unless deleted from there as well. This is beneficial, if a catastrophe occurs, their data will remain safe, provided they have synced with the Drive. In addition to data storage, Google Drive is beneficial in the security aspect as well, as it handles security and ecruption of data when being synced.

Make using Prioritize a priority today! 


Contributors: 
Joel Wilhelm (Documentation, Text to JSON, JSON to text algorithm)
Shahrukh (GUI)
Wajahat (Time Picker / Reminder)
Cody Jones (Priority Algorithm, Google Drive API)


