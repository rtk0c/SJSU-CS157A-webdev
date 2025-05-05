# CS 157A Library Management Application

## Database Setup

This is a general outline of the steps. Go to [example steps](#database-example-steps) for the default settings you can simply copy paste.

This project uses PostgreSQL. Install it according to your platform's guides.
For reference, here is a limited list of common platforms:
[Windows](https://www.postgresql.org/download/windows/),
[macOS](https://postgresapp.com/) (one of many choices),
[Ubuntu](https://documentation.ubuntu.com/server/how-to/databases/install-postgresql/index.html),
[Fedora](https://docs.fedoraproject.org/en-US/quick-docs/postgresql).

Create a user and a database. Below these will be `dbuser` with password `password` and `cs157a_webdev_db`, replace all occurrences of them with your choice.

Initialize the database by running the `db_setup.sql` file. This creates the necessary tables and other things.

Insert example data by running `db_initialize_data.sql`. This loads the set of example data for demonstration purposes, as required by the assignment.

## Database Example Steps
### `psql` command line
This is typically applicable to Linux, or macOS Postgres installed with Homebrew.
Assuming you have already installed Postgres, run
```sh
$ sudo -u postgres psql # start interactive session
CREATE USER dbuser WITH PASSWORD 'password';
CREATE DATABASE cs157a_webdev_db OWNER dbuser;
$ psql -U dbuser -d cs157a_webdev_db -f db_setup.sql
$ psql -U dbuser -d cs157a_webdev_db -f db_initialize_data.sql
```

### Postgres App
The equivalent steps above.
