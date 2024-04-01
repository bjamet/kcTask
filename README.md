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
"label": "urgent"
}
```
```
{
    "id": "e4e638b7-4813-4659-9ada-43f903929d48",
    "label": "urgent",
    "complete": false,
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
    "complete": false,
    "author": "user"
}
```
### List all the tasks
```
GET /tasks?complete=false
```
```
[
    {
        "id": "[îd]]",
        "label": "tres urgent",
        "complete": false,
        "author": "user"
    }
]
```
#### Params
* complete (optional) : 
  * not set : get all the tasks
  * false   : get all the not completed tasks
  * true    : get all the completed tasks
### Update a task

Only the author can update a task
```
PUT /tasks/[id]
{
	"label": "tres urgent"
}
```
```
{
    "id": "[id]]",
    "label": "urgent",
    "complete": false,
    "author": "user"
}
```

#### Error
* 403 : the authenticated user is not the author of the task and can't update it

#### Complete/Uncomplete a task
Only the author can complete or uncomplete a task

```
POST \tasks\[id]\complete
POST \tasks\[id]\uncomplete
```
The answer body has the field "complete" to true in the first case, false in the second case. 

#### Error
* 403 : the authenticated user is not the author of the task and can't update it

### Delete a task
Only the author can update a task
```
DELETE /tasks/[id]
```
#### Error
* 403 : the authenticated user is not the author of the task and can't update it
