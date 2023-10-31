# Permissions model for ThuBOT routes

There are 2 permission levels:
- USER -> accessible by users with role "user"
- SYSTEM -> accessible by users with role "system"

The authentication will be done with the `Authentication` header and will take a bearer token of the user.

When a permissions error occurs (not enough permissions, or auth header missing), a `403 FORBIDDEN` will be returned.


