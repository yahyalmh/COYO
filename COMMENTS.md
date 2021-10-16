# Comments 
In this document, below topics are explained

* [Architecture decisions](#main-architecture-decisions-and-why)

* [Third-party libraries](#list-of-third-party-libraries-if-any-with-whys)

* [What could be improved](#What-could-be-improved-if-you-had-more-time)
  
* [Asked but not delivered](#mention-anything-that-was-asked-but-not-delivered-and-why-and-any-additional-comments)


## Main architecture decisions and why

* I used `Model-View-ViewModel(MVVM)` architecture because it is a clean architecture that separate view from business logic.

* I used _single activity_ pattern with different fragments and the `navigation` library to navigate between them. Navigate between fragments is faster than activities, so this pattern is more agile. 

* I used `ViewBinding` to get rid of findViewById() and access the view elements easily.

* I used `DataBinding` to set the data on view quickly, which reduces code volume noticeably.

* I used the `Paging`  library version 3 to fetch data from the server. It is easy to load data lazily, and it is integrated with the RecyclerView.

* I used `Hilt` for dependency injection. Google builds it based on dagger for android applications; it is easy to configure and use.

* I used `Room` as ORM. I can use the DAO pattern and map objects and even nested objects to tables, retrieve them, and handle migrations.

* I used Kotlin's `coroutines` and `suspend` methods to do asynchronous jobs. It is one of the best solutions to deal with asynchronous tasks, as I can run tasks on any thread(Main or IO) and get the result on the UI thread.

## List of third party libraries if any with whys

* I used the `Retrofit2` to interact with API and `Gson` to map response's JSON to data classes. I can retrieve and upload JSON to a rest-based web service using the retrofit library.

* I used the `Lottie` library to show lottie animations.

## What could be improved if you had more time

* If I had more time, I would have written more UI, integration, and unit tests.

* If I had more time, I might have used google best practices to better handle server success or failure responses.

* If I had more time, I would improve the app UI.

## Mention anything that was asked but not delivered and why and any additional comments

* I have tried to cover everything, but I may have forgotten something or was not implemented completely. 