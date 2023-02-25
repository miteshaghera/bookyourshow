# ER-Diagram

```plantuml
entity "place" as plc {
    *code: string <<unique>>
    *name: string
}

entity "movie" as mv {
    *id: string <<generated, unique>>
    *title: string
    *genre: array
    *language: array
    *release_date: date
    *released_in: array <<FK>>
}

entity "theater" as thtr {
    *id: string <<generated, unique>>
    *name: string
    *place: string <<FK>>
    *address: string
    short_address: string
}

entity "screen" as scrn {
    *id: string <<generated, unique>>
    *theater: string <<FK>>
    *totalSeats: int
    *name: string
}

entity "show" as shw {
    *id: string <<generated, unique>>
    *screen: string <<FK>>
    *movie: string <<FK>>
    *timing: long
}

entity "seat" as st {
    *id: string <<generated, unique>>
    *screen: string <<FK>>
    *row: char
    *seat: int
    *category: string
    *coast: decimal
}

entity "booking" as bkng {
    *id: string <<generated, unique>>
    *email:string
    *phone:string
    *show: string <<FK>>
    *amount: decimal
    *status: enum {initiated, confirmed, failed}
    *payment_status: enum {initiated, paid, refunded}
}

entity "show_seat" as bkst {
    *id: string <<generated, unique>>
    *booking: string <<FK>>
    *show: string <<FK>>
    *seat: string <<FK>>
}


plc ||--o{ mv
plc ||--|{ thtr
thtr ||--|{ scrn
scrn ||--|{ st
mv ||--|{ shw
bkng ||--|{ bkst
shw ||--|{ bkng
st ||--|{ bkst
shw ||--o{ bkst
scrn ||--|| shw

note right of plc::code
    3 characters code
end note

note right of shw::timing
    combination of screen and timing is unique
end note
```