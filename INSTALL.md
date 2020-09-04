# freelanceando.com #

## Build & Run ##

```sh
$ cd freelanceando
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.


### Run with a different database

You can set the environment variable `FL_DATABASE` to read the database from
a different directory when calling sbt. By deafult, the app will look for the
database on a directory `./database`

```sh
$ cd freelanceando
$ FL_DATABASE="new_directory_path" sbt
> jetty:start
> browse
```

When compiling, sbt will generate two new directories `target` and `project` that you should NEVER commit to your git repository.

### Re-starting

The server won't re-start after every change, you can stop it manually with

```sh
> jetty:stop
```
or even chain both commands with

```sh
> jetty:stop;jetty:start
```

