### Nginx

A tool for serving static content and proxying requests.

The main configuration file is called `nginx.conf`.

#### Commands

Control is performed with `nginx -s <command>`, where `<command>` is:

- `stop` — fast shutdown
- `quit` — graceful shutdown
- `reload` — reload the configuration file
- `reopen` — reopen log files

**`nginx.conf`** is the configuration file.

Server configuration is located inside the `server` block:

```nginx
server {
    listen 8080;
    server_name localhost;

    location / {
        <list_of_directives>
    }
}
```

Where:

- `listen` specifies the port to listen on
- `server_name` is the domain name
- `location` defines the paths/locations to handle

#### `location`

In `location` you can specify either a path or a regular expression. Nginx first looks for a matching prefix/path-based
location, then checks regex locations. Regex locations have higher priority.

Example:

`location ~ \.(jpg|png|gif)$`

Common directives inside `location`:

- `root` points to the directory root; content from it is mapped to the location URI
- `proxy_pass` specifies the upstream URL to forward the request to and proxy the response from


