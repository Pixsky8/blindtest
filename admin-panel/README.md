# Admin Panel

The admin panel is used to help organizer give points and change the questions
of the quiz.

## Build

To build the admin panel, run the following commands:
```sh
$ mkdir build
$ cmake .. && make -j
```

The compiled binary will be called `admin-panel` and will be located in the
build folder.

## Usage

### Tabs

- Answer:
Gives the list of answers of the players for the current question.

- Question:
Set the current question in this tab.

- Login:
Use to login to the server (Use an admin account to check the answer and
change the questions).

- Option:
    * Change the address of the server
    * Change the location of the cookie file
