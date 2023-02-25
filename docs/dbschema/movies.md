# Movies
### DB Engine: mongodb
### Database name: movies_db
### Collection: movies
### Indexes
* _id: PK
* text index
```javascript
db.movies.createIndex({title: "text", genre: "text", lang: "text", releasedIn: "text"});
```
* releaseDate index
```javascript
db.movies.createIndex({releaseDate: 1})
```

### Sample data
```javascript
db.movies.insertMany([
    {
        title: "Ant-Man and the Wasp: Quantumania",
        genre: ["Action", "Sci-fi", "Adventure", "Comedy"],
        lang: ["Engilesh", "Hindi"],
        releaseDate: 1676572200,
        releasedIn: ["AMD", "VDR"]
    },
    {
        title: "Avatar: The Way of Water",
        genre: ["Action", "Sci-fi", "Adventure", "Fantacy"],
        lang: ["Engilesh", "Hindi"],
        releaseDate: 1671129000,
        releasedIn: ["AMD", "VDR"]
    }
])
```