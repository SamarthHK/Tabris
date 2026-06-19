# Tabris README

This project is the Backend for Remmixxer Art built without backend frameworks to deepen my understanding about HTTP servers and how front end and backend interact on a deeper level.

---

## Architecture

As of right now, the project runs from `Main.java`, it currently initializes the routes and runs the `ServerListener` thread (inside `core` folder).

Inside `ServerListener` I accept requests from the port and create a request object.

---

## Request Format

The request object (inside `format` folder) contains headers such as:

- Method
- Path
- Version
- Host
- MIME type
- Origin
- Access Control Request Method
- Access Control Request Headers

It also contains an ArrayList to store webform fields / elements.

---

## Middleware

This request is passed onto the middleware layer (inside `middleware` folder).

Everything is handled inside `HandleMiddleware.java`.

First, a CORS check is done, it checks if the origin is `localhost:8080`, if not it sets the blocked param inside the request object to true.

Then it parses the webform body, and will parse JSON (support not added yet), this is stored inside the webform param inside the request object.

Then the request object is passed onto the router if it's not blocked by middleware.

---

## Routing

Inside the router (inside `router` folder), depending on the HTTP method and route, a handler will be fetched.

If there is no handler a `SERVER_ERROR 500` is returned.

---

## Logic Layer

Inside `logic` folder I currently handle core features:

- Static file serving through `StaticFileHandler.java`, it looks inside `resources/static` to serve files
- `CorsAccept.java` sends a CORS accept response (this only happens if the request isn't blocked)
- `ImageReceiver.java` receives images and stores them with a 12 digit UUID, then returns the UUID to serve the image
- `getImage` inside `ImageReceiver` sends the image back if it finds a stored image with the specific UUID

Once logic runs, it returns a response object.

---

## Response Format

The response object (inside `format` folder) contains headers such as:

- Version
- Status Code
- Reason
- Mime type
- Content type
- Content Length
- Keep alive
- Server Name
- Date
- Origin
- Access Control Allow Method
- Content Disposition
- File Name

---

## Response Flow

The response object is converted into a byte array and sent to the client.

After that, the connection is closed and that's the architecture.

---

## File Tree
```src/
├── main/
│   ├── java/com/viveka01/
│   │   ├── core/
│   │   ├── format/
│   │   ├── logic/
│   │   ├── middleware/
│   │   └── router/
│   └── resources/
│       ├── config/
│       ├── database/
│       └── static/
└── test/
└── java/
```


---

## Extra

This readme ain't AI slop i wrote it >:(

The backend is named after Kaworu’s angel name from Neon Genesis Evangelion.

To me Kaworu symbolized free will and awareness, which is also why I wanted to write my own HTTP server from scratch instead of jumping into Spring.

Out of my own free will and extra time I wanted to learn how HTTP servers worked from a lower level, and Kaworu just symbolized that for me.
<div align = "center"><image src = "https://static.wikia.nocookie.net/sumoverse/images/4/47/Tumblr_lwumlgXag41r44et9o3_1280.png/revision/latest?cb=20171224023303" alt = "picture of kaworu"></div>
