# Screens
### DB Engine: mongodb
### Database name: theaters_db
### Collection: screens
### Indexes
* _id: PK
* theater index
```javascript
db.scrrens.createIndex({theater: 1});
```

### Sample data
```javascript
db.screens.insertMany([
    {
        theater: "63fb95772194ece7f96ff45f",
        name: "Screen 1",
        totalSeats: 200
    },
    {
        theater: "63fb95772194ece7f96ff45f",
        name: "Screen 2",
        totalSeats: 160
    },
    {
        theater: "63fb95772194ece7f96ff460",
        name: "Screen 1",
        totalSeats: 70
    },
    {
        theater: "63fb95772194ece7f96ff460",
        name: "Screen 2",
        totalSeats: 90
    }
]);
```