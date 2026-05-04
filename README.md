# DevLibrary

## Description

DevLibrary will help the university students to find the subjects sources in one organize place
and it will Provide an easy usage for adding, deleting, updating and seeing the resources.

## Core Features

- **Resource CRUD Operations:** Users can add, view, update, and delete educational resources.
- **Multiple Resource Types:** The system supports books, slides, notes, and projects.
- **Optional Fields:** Each resource type can include optional attributes such as author, week number, note type, project language, and project type.
- **Link and File References:** Resources can be represented as either external links or file references.
- **Database Integration:** Resources are stored in a cloud database using Spring Data JPA.
- **Authenticated Upload Tracking:** The `uploadedBy` field is automatically taken from the logged-in user.
- **Facade Layer:** adding and deleting and updating and seeing logic is handled inside `ResourceFacade`, keeping the controller simple and organized.

### API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/resources` | Retrieve all resources |
| GET | `/api/resources/{id}` | Retrieve a resource by ID |
| POST | `/api/add` | Add a new resource |
| PUT | `/api/update/{id}` | Update a resource |
| DELETE | `/api/delete/{id}` | Delete a resource |
## Usage

To build and run the app, use:

POSTMAN with the above API endpoints



## License

MIT
