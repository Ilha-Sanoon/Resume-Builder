# folio.field Resume Backend

Spring Boot REST API for the Angular resume builder.

## Run locally

Requirements: Java 17+, Maven 3.9+, and MongoDB 6+ or MongoDB Atlas.

Set the MongoDB connection string before starting the API. Keep credentials in the environment rather than committing them to source control.

```powershell
$env:MONGODB_URI = "mongodb+srv://<username>:<password>@<cluster>/?retryWrites=true&w=majority"
```

The application uses the `resume_data` database and stores resume documents in the `resumes` collection.

```powershell
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

## Endpoints

- `GET /api/health`
- `GET /api/templates`
- `GET /api/resumes`
- `GET /api/resumes/{id}`
- `POST /api/resumes`
- `PUT /api/resumes/{id}`
- `DELETE /api/resumes/{id}`

If the `resumes` collection is empty, the API seeds three demo resumes. Existing MongoDB data is never overwritten on startup.
