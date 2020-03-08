const express = require("express");
var mongo = require('mongodb');
const request = require("request");
const csv = require('csvtojson');
const bodyParser = require("body-parser");
const assert = require('assert');
const app = express();
const querystring = require("querystring");

const GOOGLE_API_KEY = 'AIzaSyD3a9LPGJGo8aEE2_AS5FOPEtvb7ZD60PE';
const EBAY_API_KEY = 'hacktech-COVIDASH-PRD-569eabc89-36681d5b';

app.listen(8080, () => {
    console.log('Server started!');
    console.log('on port 8080');
});


var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";

// generate_geo_location()

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

app.get("/api/autocomplete", function(req, res) {
    var input = req.query.input;
    var url =
        "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
        input +
        "&types=(cities)&language=en&key=AIzaSyD3a9LPGJGo8aEE2_AS5FOPEtvb7ZD60PE";
    request(url, function(error, responde, body) {
        if (!error && responde.statusCode == 200) {
            body = JSON.parse(body);
            res.send(body);
        }
    });
});

app.get("/api/location_city", function(req, res) {
    var city = req.query.city;
    var url =
        "https://maps.googleapis.com/maps/api/geocode/json?address=" +
        city +
        "&key=AIzaSyD3a9LPGJGo8aEE2_AS5FOPEtvb7ZD60PE";
    request(url, function(error, responde, body) {
        if (!error && responde.statusCode == 200) {
            body = JSON.parse(body);
            res.send(body);
        }
    });
});

app.get("/api/ebay_search_keyword", (req, res, next) => {
    var query = req.query.keyword;
    const ebay_query = ebay_search_query(query)
    request(ebay_query, function(error, response, body) {
        var data = body;
        data = JSON.parse(data);
        // console.log(data);
        res.json(data);
    });
});

app.get("/api/location2cityname", (req, res, next) => {
    var lat = req.query.lat;
    var lng = req.query.lng;
    var url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat
    + "," + lng + "&language=EN&key=" + GOOGLE_API_KEY;
    request(url, function(error, response, body) {
        var data = body;
        data = JSON.parse(data);
        var length = data.results.length;
        if ((length - 4) >= 0) {
            var state = data.results[length-4].formatted_address;
        } else {
       	    var state = data.results[0].formatted_address;
	}
        var address = state;
        res.json(address);
    });
});

app.get('/api/nearest_location', (req, res, next) => {
    var lat = parseInt(req.query.lat);
    var lng = parseInt(req.query.lng);
    MongoClient.connect(url, function(err, db) {
        assert.equal(null, err);
        console.log("Connected correctly to server");
        var dbo = db.db("mydb");
        // insertDocuments('03-06-2020.csv', dbo, function () {
        //      db.close();
        // });
        var collection = dbo.collection("covid_data");
        // collection.updateMany({}, {$rename: {'Latitude': 'coords.lat', 'Longitude': 'coords.lon'}}, false, true)
        collection.ensureIndex({"coords" : "2d"});
        collection.find({
            "coords": { "$near": [lat, lng] },
            }).toArray(function(err, result) {
                if (err) throw err;
                res.json(result[0])
                db.close();
        });
    });
});

function ebay_search_query(keyword) {
    // Construct the request
    // Replace MyAppID with your Production AppID
    var url = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-NAME=FindingService&SERVICE-VERSION=1.0.0&GLOBAL-ID=EBAY-US&SECURITY-APPNAME=" + EBAY_API_KEY + "&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=" + keyword;
    return url
}

function generate_geo_location() {
    MongoClient.connect(url, function(err, db) {
        assert.equal(null, err);
        console.log("Connected correctly to server");
        var dbo = db.db("mydb");
        // insertDocuments('03-06-2020.csv', dbo, function () {
        //      db.close();
        // });
        var collection = dbo.collection("covid_data")
        collection.updateMany({}, {$rename: {'Latitude': 'coords.lat', 'Longitude': 'coords.lon'}}, false, true)
        collection.createIndexes({"coords" : "2d"}, {"min":-1000, "max":1000});
        collection.find({
            "coords": { "$near": [41, 120] },
            }).toArray(function(err, result) {
                if (err) throw err;
                // console.log(result);
                console.log(result[0])
                db.close();
        });
    });
}
