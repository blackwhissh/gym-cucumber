# gym-cucumber

## How to use

Create database hibernate_admin.

Use admin profile (to automatically load admin user with username "admin" and password "admin").

Login as admin with the following request:
    POST localhost:8085/auth/v1/user/login
    {
    "username": "admin",
    "password": "admin"
    }

Register new trainer:
    POST localhost:8085/gym/v1/register/trainer
    {
    "firstName": "trainer",
    "lastName": "trainer",
    "specialization": "AGILITY"
    }
    Add authorization header.

Add new training:
    POST localhost:8085/gym/v1/admin/training/add
    {
    "loginDTO":{
        "username":"admin",
        "password":"admin"
    },
    "traineeUsername":"trainee.trainee",
    "trainerUsername":"trainer.trainer0",
    "trainingName":"My Third Training",
    "trainingDate":"2024-05-18T12:30:00",
    "duration": 4,
    "trainingType": 3
    }
    Add authorization header.

### For further usages use swagger documentation


