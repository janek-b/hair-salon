# _hair-salon_

#### _hair-salon, 03-31-2017_

#### By _**Janek Brandt**_

## Description
_This project is a web based management app for a hair salon. It allows a manager to manage the stylists working at the salon. A manager can add a stylists when they are hired, update their information if needed and delete them when they stop working at the salon. A manager can also add and remove clients who visit the salon and assign them to a stylist._


## Specifications

| Behavior                   | Input Example     | Output Example    |
| -------------------------- | -----------------:| -----------------:|
| Add a new stylist | Jennifer | Jennifer |
| Get a stylists name | stylistName | Joe |
| Change a stylists name | Martha | Martha |
| Remove a stylist | stylistRemove | null |
| Add a new client | Becky | Becky |
| Get a clients name | clientName | Becky |
| Assign a client to a stylist | Becky assignStylist | Martha |
| Change a clients name | Jessica | Jessica |
| Remove a client | clientRemove | null |
| Get all clients for stylist | Martha | Jessica |
| Assign a client to a random stylist | Jessica | Brenda |
| Re-assign a client to a random stylist if their current stylist if removed | stylistRemove Brenda | Martha | 

## Setup/Installation Requirements

* _Clone the repository_
* _In a separate terminal window start a postgres server_
* _Connect to postgres with psql and type_
```
# CREATE DATABSE hair_salon
```
* _Exit psql and type the following command from the project's root directory_
```
$ psql hair_salon < hair_salon.sql
```
* _Run the command 'gradle run'_
* _Open browser and go to localhost:4567_


### License

Copyright (c) 2017 **_Janek Brandt_**

This software is licensed under the MIT license.
