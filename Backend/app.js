const express = require("express");
var mongo = require('mongodb');
const request = require("request");
const csv = require('csvtojson');
const bodyParser = require("body-parser");
const CircularJSON = require('circular-json');
const assert = require('assert');
const app = express();

app.listen(8080, () => {
    console.log('Server started!');
    console.log('on port 8080');
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
        //      db.close();
        // });
        dbo.collection("covid_data").find({}).toArray(function(err, result) {
            if (err) throw err;
            // console.log(result);
            res.json(result);
            db.close();
        });
    });
});
app.get("/api/covid_data_by_country", (req, res, next) => {
    // Connect to Mongo DB database
    MongoClient.connect(url, function(err, db) {
        assert.equal(null, err);
        console.log("Connected correctly to server");
        var dbo = db.db("mydb");
        // insertDocuments('03-06-2020.csv', dbo, function () {
        //      db.close();
        // });
        dbo.collection("covid_data").aggregate(
            // Pipeline
            [
                // Stage 1
                {
                    $group: {
                        // enter query here
                        _id: "$Country/Region",
                        totalConfirmed: { $sum: "$Confirmed" },
                        totalDeaths: { $sum: "$Deaths" },
                        totalRecovered: { $sum: "$Recovered" }
                    }
                },
            ]
        ).toArray(function(err, results) {
            res.json(results);
        });
    });
});
app.get("/api/covid_data_by_state", (req, res, next) => {
    // Connect to Mongo DB database
    const country = req.query.country;
    MongoClient.connect(url, function(err, db) {
        assert.equal(null, err);
        console.log("Connected correctly to server");
        var dbo = db.db("mydb");
        // insertDocuments('03-06-2020.csv', dbo, function () {
        //      db.close();
        // });
        dbo.collection("covid_data").aggregate(
            // Pipeline
            [
                // Stage 1
                {
                    $match: {
                        // enter query here
                        "Country/Region": country, // Mainland China
                    }
                },
                // Stage 2
                {
                    $group: {
                        // enter query here
                        _id: "$Province/State",
                        totalConfirmed: { $sum: "$Confirmed" },
                        totalDeaths: { $sum: "$Deaths" },
                        totalRecovered: { $sum: "$Recovered" }
                    }
                },
            ]
        ).toArray(function(err, results) {
            res.json(results);
        });
    });
});

app.get("/api/autocomplete", (req, res, next) => {
  const input = req.query.input;
  const auto_complete_url =
    "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
    encodeURI(input) +
    "&types=geocode&language=en&key=" +
    GOOGLE_API_KEY;
  request(auto_complete_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    var text_dic = new Array();
    var predictions = data.predictions;
    var length = 5;
    if (predictions.length < 5) {
      length = predictions.length;
    }
    for (i = 0; i < length; i++) {
      var prediction = predictions[i];
      var main_text = prediction.structured_formatting.main_text;
      text_dic[i] = main_text;
    }
    res.json(text_dic);
  });
});
