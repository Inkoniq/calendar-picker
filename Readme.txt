It has a custom Calendar picker library to support calender date picker. It is supported from 2.3.3. 

Description
It has two Views.
1. CalendarScrollerView
-Starts from current month & endless to for future dates.

2. CalenderFlipperView
- Starts with current month & endless past & future dates.
-Has swipe to change month feature
-Has navigation arrows on the top to change months

Both the designs supports restriction on valid start & end dates. That means it will display the months only falls in valid time else it will display all months if no valid start or end is specified.

Once the user marks the calender for pick start date it will mark the cell as start date in red.  Only one start date is possible. The user can change the selected date directly tapping on another date.

Once the user marks the calendar for pick end date it will mark all the cells starting from the start date & ending at end date in red specifying selected.  The user can change the selected end date directly tapping on another date.
 
There is an example project to describe how to use it.

Find the project in the following link

git@github.com:Inkoniq/calendar-picker.git 