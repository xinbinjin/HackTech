const express = require("express");
var mongo = require('mongodb');
const request = require("request");
const csv = require('csvtojson');
const bodyParser = require("body-parser");

assert = require('assert');

const app = express();

app.listen(8081, () => {
    console.log('Server started!');
    console.log('on port 8081');
});

var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";

app.use(bodyParser.json());

app.use((req, res, next) => {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  res.setHeader(
    "Access-Control-Allow-Methods",
    "GET, POST, PATCH, DELETE, OPTIONS"
  );
  next();
});
// Get 
app.get("/api/covid_daily_data", (req, res, next) => {
	// Connect to Mongo DB database
	MongoClient.connect(url, function(err, db) {
		assert.equal(null, err);
		console.log("Connected correctly to server");
		var dbo = db.db("mydb");
		// insertDocuments('03-06-2020.csv', dbo, function () {
		// 	db.close();
		// });
		dbo.collection("COVID_Daily").find({}).toArray(function(err, result) {
	    if (err) throw err;
	    // console.log(result);
	    res.json(result);
	    db.close();
	  });
	});
});
