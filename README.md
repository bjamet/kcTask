# Task application

## run the project
```
mvn spring-boot:run
```
## Authentication

The project as a demo has 2 inmemory accounts 
* user:password
* user2:password

The following apis are Basic authenticated

## APIs


### Create a task
```
POST /tasks
{
"label": "urgent",
"description": "repondre mail "
}
```
```
{
    "id": "e4e638b7-4813-4659-9ada-43f903929d48",
    "label": "urgent",
    "description": "repondre mail ",
    "author": "user"
}
```
### Get a task
```
GET /tasks/[id]
```
```
{
    "id": "[id]]",
    "label": "urgent",
    "description": "repondre mail ",
    "author": "user"
}
```
### List all the tasks
```
GET /tasks
```
```
[
    {
        "id": "[îd]]",
        "label": "tres urgent",
        "description": "repondre mail ",
        "author": "user"
    }
]
```
### Update a task

Only the author can update a task
```
PUT /tasks/[id]
{
	"label": "tres urgent",
    "description": "repondre mail "
}
```
```
{
    "id": "[id]]",
    "label": "urgent",
    "description": "repondre mail ",
    "author": "user"
}
```

#### Error
* 403 : the authenticated user is not the author of the task and can't update it

### Delete a task
Only the author can update a task
```
DELETE /tasks/[id]
```
#### Error
* 403 : the authenticated user is not the author of the task and can't update it
