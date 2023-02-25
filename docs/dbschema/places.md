# Places
### DB Engine: mongodb
### Database name: places_db
### Collection: places
### Indexes:
* _id: PK
* code: Unique index
```javascript
db.places.createIndex({code:  1}, {unique: true});
```
* text index
```javascript
db.places.createIndex({code: "text", name:"text"});
```

### Sample data
```javascript
db.places.insertMany([
    {
        "code": "AMD",
        "name": "Ahmedabad"
    },
    {
        "code": "VDR",
        "name": "Vadodara"
    }
])
```