# 201Project

## Running the Project

### Frontend

```
npm install
npm run dev
```

### Backend

```
cd backend
mvn clean spring-boot:run
```

---

## Connecting to the AWS MySQL Database

Since we are no longer using MySQL, if you want to access the MySQL database via terminal:

```
mysql -u groupuser -p -h 3.148.188.152 -P 3306
```

> When prompted, enter the password: `papa201!`

If your terminal does not recognize the `mysql` command, install MySQL with:

```
brew install mysql
```

---

## Example SQL Commands

Once logged in, you can run:

```
SHOW DATABASES;
USE STUDY_GROUP_MATCHER;
SELECT * FROM Users;
```
