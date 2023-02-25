# Shows
### DB Engine: mongodb
### Database name: shows_db
### Collection name: shows
### Indexes
* _id: PK
* movie,screen, startTime, endTime index
```javascript
db.shows.createIndex({movie: 1, screen: 1, startTime: 1, endTime: 1})
```

### Sample data
```javascript
db.shows.insertMany([
    {
        screen: "63fba1f52194ece7f96ff461",
        movie: "63fb6a252194ece7f96ff45d",
        startTime: 1677570300,
        endTime:1677581100
        
    },
    {
        screen: "63fba1f52194ece7f96ff462",
        movie: "63fb6a252194ece7f96ff45d",
        startTime: 1677581100,
        endTime: 1677591900
    }
]);
```