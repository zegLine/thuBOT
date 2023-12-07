# API Routes for ThuBOT

## API Documentation
The maven dependency `springdoc-openapi` automatically generates the API documentation under `/v3/api-docs` and also provides the Swagger UI under `/swagger-ui/index.html`

## API Guidelines
For the sake of consistency and clean code practices, the routes will start with `/api`, followed by the object controller name, followed by the operation (example: `/api/users/create`)
Please follow common HTTP verb rules where applicable (example: a `GET` route should only be used to retrieve information, etc.)

## API authentication
Each route will be protected with one of two permissions:
- USER
- SYSTEM

More information in the [permissions_model.MD](permissions_model.MD)
