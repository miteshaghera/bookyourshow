# Assumptions
* XYZ wants build system to book only theaters/multiplexes. No other kind of bookings like music concerts, drama & plays etc.
* Theater partners will be onboarded as a back office operation.

# Definitions
* Individual User: A person who falls under B2C category.
* Corporate User: A person who falls under B2B category.
* Theater Partner: A person/entity who own/run theaters/multiplexes like inox, pvr etc.

# Technologies
* Base platform: Java (JVM) for backend + Vanilla js for frontend
* Framework: Eclipse Vert.x
* Security: Open id connect (based on oAuth 2 - auth0.com, okta etc)
* Notification: Nats (any distributed messaging queue for notifications - email, sms)
* Data layer: Postgres (for ACID transactional data) + Mongodb (for non structured/semi structured data) + Redis (for caching)
* Payment integration: Stripe (Because I know) or equivalent
* Deployment: OCI containers (docker swarm, kubernetes), ansible
* Tracing: open telemetry (honeycomb.io)
* Log management: ELK stack
* Load balancer: nginx
* OS: alpine (container base images based on alpine)

### Design principles/patterns
* Micro-services: I had split entire architecture in various tiny services so that we can them scale up/down individually based on our requirements.
  * place-service: Manage places where our service is available to serve our customers.
  * movie-service: Manage movies in our system
  * theater-service: To manage theaters/cinema/multiplexes in our system.
  * show-service: Manage shows like add new show, change timings, cancel show etc.
  * seat-service: Manage seats inventory except booking.
  * booking-service: Manage bookings and allocation of seats, integration with payment gateways, theater partners
  * sms-service: send sms notifications for events happens around bookings
  * email-service: send email notifications for events happens around bookings
* Reactive programming: I used reactive programming principles through RxJava 3 library to leverage power of lazy execution. 
When data is ready to process, invoke appropriate function to process it.
* Reactive systems: Eclipse vert.x uses netty to build event loop. Event loop is a mechanism to reuse same thread to perform 
different operations/executions which results higher throughput. It forces developer to use reactive programming to differ
execution until data is ready.
* Singleton pattern: All services are designed as singleton with in same jvm, to reduce no of unnecessary objects.
Also it reduces no of connection established to db server (or any other system).
* Immutability: All data objects are created as immutable objects because we use functional reactive programming which 
can easily result in thread collision. To avoid race conditions & thead collisions we can make data objects immutable so
that they are thread safe.
* Builder pattern: To build data object, I recommended to use builder.
* CQRS (command query responsibility segregation): not exactly by the definition I used, seat-service is responsible to fetch available seats 
where booking-service is responsible to allocate seat. By this way write operations & read operations are segregated for seats.
