# Documentation

Interact with the application via a HTTP client.


## Create User

Create a [`user.json`](./user.json) file with contents like the following:

```json
{
  "name": "Your Name",
  "email": "your@email",
  "triggers": [
    {
      "symbol": "DXET.DE",
      "name": "DB X-TR.EO STOXX 50 ETF DR 1C",
      "buy": 33,
      "sell": -1
    },
    ...
  ]
}
```

Make sure you type your actual email address. Add that user, i.e. via `curl`.

> Depending on the server configuration you may need to pass the admin token via `?t=ADMIN_KEY` to do this.

```
curl --data @user.json -v \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  http://localhost:8080/users
```

The server will respond with a `Location` header for the newly created resource.
The last part is the `:id` you can use for inspection and administration.
Keep it secret.

```
Location: http://localhost:8080/users/6ee14223-7f6e-4cbe-a2db-b33bea54e880
```


## Show Details

```
curl -v -H "Accept: application/json" http://localhost:8080/users/:id
```


## Delete User

```
curl -v -X DELETE -H "Accept: application/json" http://localhost:8080/users/:id
```