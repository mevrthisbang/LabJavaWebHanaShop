CREATE DATABASE LAB1HANASHOP
USE LAB1HANASHOP
CREATE TABLE ACCOUNT
(
    username varchar(10) PRIMARY KEY,
	password varchar(20),
	fullname varchar(30),
	email varchar(100) UNIQUE,
	phone varchar(12) UNIQUE,
	address varchar(200),
	favor varchar(200),
	role varchar(10),
)
CREATE TABLE FOODCATEGORY
(
    cateID varchar(10) PRIMARY KEY,
	name varchar(30),
)
CREATE TABLE FOODANDDRINK
(
    itemID varchar(10) PRIMARY KEY,
	name varchar(30),
	price float,
	img varchar(200),
	description varchar(500),
	status varchar(10) DEFAULT 'Active', 
	quantity int,
	createBy varchar(10) FOREIGN KEY REFERENCES Account(username),
	createDate datetime DEFAULT GETDATE(),
	category varchar(10) FOREIGN KEY REFERENCES FOODCATEGORY(cateID),
	counter int,
	updateBy varchar(10) FOREIGN KEY REFERENCES Account(username),
	updateDate datetime, 
)
CREATE TABLE ORDERS
(
    orderID varchar(20) PRIMARY KEY,
	customer varchar(10) FOREIGN KEY REFERENCES ACCOUNT(username),
	buyerName varchar(30),
	buyDate datetime DEFAULT GETDATE(),
	paymentMethod varchar(10),
	phone varchar(12),
	shipAddress varchar(200),
	status varchar(20),
)
CREATE TABLE ORDERDETAIL
(
    orderlineID varchar(30) PRIMARY KEY,
	orderID varchar(20) FOREIGN KEY REFERENCES ORDERS(orderID),
	foodID varchar(10) FOREIGN KEY REFERENCES FOODANDDRINK(itemID),
	quantity int, 
	price float,
)
CREATE TRIGGER trg_Order
ON ORDERDETAIL AFTER INSERT AS 
BEGIN
	UPDATE FOODANDDRINK
	SET quantity = FOODANDDRINK.quantity - (
		SELECT quantity
		FROM inserted
		WHERE foodID = FOODANDDRINK.itemID
	), counter=FOODANDDRINK.counter+1
	FROM FOODANDDRINK
	JOIN inserted ON FOODANDDRINK.itemID = inserted.foodID
END
GO
INSERT INTO ORDERDETAIL(orderlineID, orderID, foodID, quantity, price)
VALUES('OD_linhvtt_1_4', 'OD_linhvtt_1', 'FD_4', 6, 4.99)
Select top 8 itemID, name, price, img
From FOODANDDRINK
Where category IN(
Select distinct category
From FOODANDDRINK
Where itemID IN(Select foodID
From ORDERDETAIL
Where orderID IN(Select orderID
From ORDERS
Where customer='linhvtt'))) and itemID NOT IN(Select foodID
From ORDERDETAIL
Where orderID IN(Select orderID
From ORDERS
Where customer='linhvtt')) and status='Active' and quantity>0 