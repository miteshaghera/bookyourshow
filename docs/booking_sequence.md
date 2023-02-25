# Sequence flow for booking use case

```plantuml
@startuml
actor "Individual/corporate user" as user
participant "Places service" as ps
participant "Movies service" as ms
participant "Theater service" as ts
participant "Show service" as ss
participant "Booking service" as bs
participant "Payment gateway" as pg
participant "Theater partner" as tp

activate ps
activate ms
activate ts
activate ss
activate bs
activate pg
activate tp

user -> ps: list/find cities
ps -> user: returns matched cities

user -> ms: list movies in selected place
ms -> user: returns released (on going movies)

user -> ts: list theaters for selected place & movie
ts -> user: returns list of theaters showing selected movie in selected place

user -> ss: list shows for selected theater & date
ss -> user: returns list of shows running on selected theater & date

user -> bs: display available seats for selected show
bs -> user: returns available seats with their pricing

user -> bs: book selected seats
bs -> bs: lock selected seats
bs -> pg: initiate payment
pg -> bs: payment successful
bs -> tp: book selected seats
tp -> bs: booking confirm
bs -> bs: mark the seats as booked
bs -> user: booking is confirmed
@enduml
```

_Note: Above diagram show direct user interact with backend services but actually user will interact with browser/mobile
application_

Solution to handle transactions for booking:

1. Lock selected seats in our platform so that other user from our platform can not take same seats for same show.
   * Lock has to be timed based. Within given time frame user has to complete payment and confirm booking with theater
     partner,
     otherwise lock will be released and user will not able to complete the booking & payment will be result in refund
     if it was successful at payment gateway.
2. Initiate booking with theater partner (if they have mechanism) / Buy from/reserve some seats at theater partner
    * Initiate booking at theater partner so that other aggregator can not book same seat(s) if their system supports
      it.
    * Or buy / reserve some seat(s) from theater partners so that other aggregators can not book same seat(s)
3. Initiate payment at payment gateway
4. Receive/confirm payment from payment gateway
    * if payment is not failed, release the locks and cancel initiation at theater partner.
    * if payment is not confirmed with in given time period, release the locks and cancel initiation at theater partner.
5. Confirm booking with theater partner.
    * If payment is received and booking can not be confirmed, raise refund for the booking and ask user to retry it.
6. Send notifications to user for confirmed booking.

To handle locks for seat(s), we have to use database which allows ACID transactions. 
To reduce read/write race conditions we can cache (who allow Atomic operations) locked + booked seat(s) for the upcoming shows (which are not yet started).
To reduce size of the cache, we can evict entries once the show is started.
