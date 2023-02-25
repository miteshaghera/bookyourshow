# Theaters
### DB Engine: mongodb
### Database name: theaters_db
### Collection name: theaters

### Indexes
* _id: PK
* city index
```javascript
db.theaters.createIndex({city: 1})
```
* text index
```javascript
db.theaters.createIndex({name: "text", shortAddress: "text", address: "text"})
```

### Sample data
```javascript
db.theaters.insertMany([
    {
        name: "PVR",
        shortAddress: "Central bus stand",
        address: "Central bus stand, Galleria Mall",
        city: "VDR",
    },
    {
        name: "Cinepolis",
        shortAddress: "Inorbit",
        address: "Inorbit Mall, Genda circle",
        city: "VDR",
    }
]);
```