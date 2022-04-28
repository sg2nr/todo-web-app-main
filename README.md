**ToDo-WebApp** is a simple web application which allows users to create TODOs via REST API.

## Database model

![DB model](DBModel.png)

## API description

Here you can find an overview of the operations provided by **ToDo-WebApp**.
You can find some example ready-to-execute on the attached Postman collection (path: resources/Todo API.postman_collection.json).

### Retrieve all categories
#### Request
````
GET /categories
````
#### Response
`200 OK`
````
[
    {
        "name": "sport",
        "description": "healthy activities"
    },
    {
        "name": "shopping",
        "description": "List of activities related to shopping and e-commerce."
    },
    {
        "name": "finance",
        "description": null
    },
    {
        "name": "useless",
        "description": null
    }
]
````
### Add new category
#### Request
````
POST /categories

{
    "name": "sport",
    "description": "healthy activities"
}
````
#### Response
`201 Created`
```
{
    "name": "sport",
    "description": "healthy activities"
}
```
### Update a category
#### Request
Update category having name="shopping".
````
PATCH /categories/shopping

{
    "description": "List of activities related to shopping and e-commerce."
}
````
#### Response
`200 OK`
```
{
    "name": "shopping",
    "description": "List of activities related to shopping and e-commerce."
}
```

### Delete a category
If a category doesn't have any task associated to it, it can be deleted.
#### Request
Update category having name="useless".
````
DELETE /categories/useless
````
#### Response
`204 No Content`
#### Possible errors
```
400 Bad Request

{
    "error": "INVALID INPUT DATA",
    "details": "Category with name e-commerce does not exist.",
    "timestamp": "2022-04-28T18:06:32.2675643+02:00"
}
```
```
400 Bad Request

{
    "error": "OPERATION NOT POSSIBLE",
    "details": "Impossible to delete category with name e-commerce, since there are still tasks associated to it.",
    "timestamp": "2022-04-28T18:21:53.3081662+02:00"
}
```
### Retrieve all tasks
#### Request
````
GET /tasks

{}
````
#### Response
The returned tasks are ordered by deadline (from closest to furthest in time).

`200 OK`
````
[
    {
        "id": "4",
        "name": "bank transfer",
        "description": "do bank transfer to Bob",
        "deadline": "2022-04-28T20:00:00+02:00",
        "category": "finance"
    },
    {
        "id": "3",
        "name": "jogging",
        "description": null,
        "deadline": "2022-04-29T16:00:00+02:00",
        "category": "sport"
    },
    {
        "id": "2",
        "name": "go to supermarket",
        "description": "food for the weekend",
        "deadline": "2022-04-29T23:59:00+02:00",
        "category": "e-commerce"
    },
    {
        "id": "1",
        "name": "buy food",
        "description": "everything we need for the party",
        "deadline": "2022-05-04T23:59:00+02:00",
        "category": "e-commerce"
    }
]
````

### Retrieve tasks by categories
#### Request
````
GET /tasks

{
    "categories": ["shopping", "sport"]
}
````
#### Response
The returned tasks are ordered by deadline (from closest to furthest in time).

`200 OK`
````
[
    {
        "id": "3",
        "name": "jogging",
        "description": null,
        "deadline": "2022-04-29T16:00:00+02:00",
        "category": "sport"
    },
    {
        "id": "2",
        "name": "go to supermarket",
        "description": "food for the weekend",
        "deadline": "2022-04-29T23:59:00+02:00",
        "category": "shopping"
    },
    {
        "id": "1",
        "name": "buy food",
        "description": "everything we need for the party",
        "deadline": "2022-05-04T23:59:00+02:00",
        "category": "shopping"
    }
]
````
### Retrieve tasks by deadline
#### Request
````
GET /tasks

{
    "beforeDeadline": "2022-04-30T00:00+02:00"
}
````
#### Response
The returned tasks are ordered by deadline (from closest to furthest in time).

`200 OK`
````
[
    {
        "id": "4",
        "name": "bank transfer",
        "description": "do bank transfer to Bob",
        "deadline": "2022-04-28T20:00:00+02:00",
        "category": "finance"
    },
    {
        "id": "3",
        "name": "jogging",
        "description": null,
        "deadline": "2022-04-29T16:00:00+02:00",
        "category": "sport"
    },
    {
        "id": "2",
        "name": "go to supermarket",
        "description": "food for the weekend",
        "deadline": "2022-04-29T23:59:00+02:00",
        "category": "e-commerce"
    }
]
````
### Retrieve tasks by deadline and categories
#### Request
````
GET /tasks

{
    "categories": [
        "e-commerce"
    ],
    "beforeDeadline": "2022-04-30T00:00+02:00"
}
````
#### Response
The returned tasks are ordered by deadline (from closest to furthest in time).

`200 OK`
````
[
    {
        "id": "2",
        "name": "go to supermarket",
        "description": "food for the weekend",
        "deadline": "2022-04-29T23:59:00+02:00",
        "category": "e-commerce"
    }
]
````
### Retrieve task by id
#### Request
````
GET /tasks/1
````
#### Response

`200 OK`
````
{
    "id": "1",
    "name": "buy food",
    "description": "everything we need for the party",
    "deadline": "2022-05-04T23:59:00+02:00",
    "category": "e-commerce"
}
````
#### Possible errors
```
400 Bad Request

{
    "error": "INVALID INPUT DATA",
    "details": "Task with id 3256 does not exist.",
    "timestamp": "2022-04-28T18:37:39.0395251+02:00"
}
```
### Add new task
#### Request
````
POST /tasks

{
    "name": "jogging",
    "deadline": "2022-04-29T16:00:00+02:00",
    "category": "sport"
}
````
#### Response
`201 Created`
```
{
    "id": "3",
    "name": "jogging",
    "description": null,
    "deadline": "2022-04-29T14:00:00Z",
    "category": "sport"
}
```
### Update task
#### Request
````
PATCH /tasks/1

{
    "name": "buy chips",
    "description": "chips for the party",
    "deadline": "2022-05-05T23:59:00+02:00"
}
````
#### Response
`200 OK`
````
{
    "id": "1",
    "name": "buy chips",
    "description": "chips for the party",
    "deadline": "2022-05-05T21:59:00Z",
    "category": "e-commerce"
}
````
### Delete task
Once a task is completed, it can be deleted.
#### Request
````
DELETE /tasks/1
````
#### Response
`204 No Content`
#### Possible errors
```
400 Bad Request

{
    "error": "INVALID INPUT DATA",
    "details": "Task with id 253 does not exist.",
    "timestamp": "2022-04-28T18:40:40.9930491+02:00"
}
```

