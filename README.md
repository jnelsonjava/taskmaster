# Task Master

An Android app for tracking tasks!

## Changelog

  - 10/19/20
    - initialized project
    - main tasks page added
    - add task page added
    - task list page added
  - 10/20/20
    - created task details page
    - created settings page
    - added task buttons with intents to task details
    - task list title dynamic to username in settings
  - 10/21/20
    - task class created
    - set up recycler view for task list
    - made task list clickable to view details
  - 10/22/20
    - added Room dependencies and linked Task as an Entity
    - edited task list to refer to database
    - replaced hard coded task inserts with reference to add form
    - adding tests for task and username changes
  - 10/26/20
    - adding tests for task views and main user paths
    - updated screenshots
  - 10/27/20
    - connected to DynamoDB using Amplify
    - converted task add to save to DynamoDB instead of Room
    - main page task list now updated from DynamoDB
  - 10/28/20
    - added subscription and handler to keep task list up to date with cloud
    - created state and team models with relations to the task model
    - replaced submitted popup with toast on add task form
    - added hard coded radio button team selection to add task form
    - updated task save to match relations on DynamoDB
    - removed connection to local Room storage
    - task list now displays tasks of user's team
  - 11/02/20
    - added Cognito user authentication plugin
    - created activities for signup, user confirmation, and login
    - linked new activites to user authentication operations
    - added logout button
    - adjusted main page to display signed in username
  - 11/03/20
    - connected S3 bucket
    - added file attachement to add task options
    - added image file retrieval in task details
    - updated schema to relate S3 storage key to Task objects
    - added image view on task details
  - 11/04/20
    - linked to Firebase for push notifications
    - attached Pinpoint push notifications through Firebase
  - 11/05/20
    - added Amplify Analytics plugin
    - created event tracking App Start
    - added event tracking user button clicks
    - linked Pinpoint stream to Kinesis
  - 11/09/20
    - made image shares receivable to add task form
    - shared images can now be saved with new task and viewed with task details
  - 11/10/20
    - added permissions and request for location access
    - updated schema/DynamoDB to hold address, latitude, and longitude in Task model
    - added address field to task detail view
    - included button to view map location in external map app
  - 11/11/20
    - added ad to bottom of main app view
  - 11/12/20
    - removed logs
    - removed ads
    - built release apk

## Main Page

![Main page](screenshots/main-activity-11-11-20.PNG)

## Detail View

![Task detail page](screenshots/task-details-activity-10-26-20.PNG)

## Add Task Form

![Add task page](screenshots/add-task-activity-10-28-20.PNG)

## Settings Page

![Settings page](screenshots/settings-activity-10-28-20.PNG)

## Signup Page

![Signup Page](screenshots/signup-activity-11-02-20.PNG)

## Firebase Console Message

![Firebase Console Message](screenshots/firebase-console.PNG)

## Firebase Phone Notification

![Firebase Phone Notification](screenshots/firebase-notification.PNG)

## Pinpoint Console

![Pinpoint Console](screenshots/pinpoint-console.PNG)

## Initial Pinpoint Campain Metrics

![Initial Pinpoint Campain Metrics](screenshots/pinpoint-campain-metrics.PNG)

## Kinesis Metrics Sample

![Kinesis Metrics Sample](screenshots/kinesis-analytics.PNG)

## Pinpoint Analytics

![Pinpoint Analytics](screenshots/pinpoint-analytics.PNG)

## Task Details with Shared Image

![Task Details with Shared Image](screenshots/task-details-activity-11-09-20.PNG)

## Task Details with Address

![Task Details with Address](screenshots/task-details-activity-11-10-20.PNG)

