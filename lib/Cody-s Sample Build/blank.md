This bit of java is currently how the 'due date' of a reminder with a priority level are being scheduled

I found that the best way to do this was to simply have integers representing the month, day of month, year, and optionally
hour and minute be passed into the class. A day countdown is created which is multiplied by the priority percentage to get
a new countdown for the firt priority reminder. That is then translated into a date. 

The way java handles dates is kind of wild, so the best way for us to handle them is with the most basic way possible, simply sending 
the month values and day values around (and hour and minute).

I haven't done the hour/minute work yet, that will be done tomorrow or later this week. I also haven't handled the case where the month 
of the due date is a lower value that the current month, but that will be a simple task. 

