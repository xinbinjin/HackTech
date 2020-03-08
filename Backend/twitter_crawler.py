from TwitterSearch import *
import time
import json

try:
    tso = TwitterSearchOrder()
    tso.set_keywords(['coronavirus', 'New York'])
    los_angeles_results = []
    count = 0

    ts = TwitterSearch(
        consumer_key = 'SNqqY6LLpmJkv8ezwvaFokNi3',
        consumer_secret = 'JMNi2Oe9qc0BSSsFRMyQmisQO8ulLSED5JUAbZzZsoJwalymgF',
        access_token = '1236228026578264066-e3PxiubejXPYy12Aqc5Ap5YSLhX9JI',
        access_token_secret = 'IsQwQEDdQBaKHKprDxUtY07ipYEkfG5Y5rMvUkpfCnpwG'
     )

    def my_callback_closure(current_ts_instance): # accepts ONE argument: an instance of TwitterSearch
        queries, tweets_seen = current_ts_instance.get_statistics()
        if queries > 0 and (queries % 5) == 0: # trigger delay every 5th query
            time.sleep(60) # sleep for 60 seconds

    for tweet in ts.search_tweets_iterable(tso, callback=my_callback_closure):
        count += 1
        tweet_dict = {}
        tweet_dict['id'] = tweet['id']
        tweet_dict['user'] = tweet['user']['screen_name']
        tweet_dict['text'] = tweet['text']
        tweet_dict['date'] = tweet['created_at']
        los_angeles_results.append(tweet_dict)
        print (count)
        if count == 10000:
            break
    los_angeles_result_json = json.dumps(los_angeles_results)
    f = open("los_angeles_tweets.txt", "w")
    f.write(los_angeles_result_json)
    f.close()
    print ("closed")
except TwitterSearchException as e:
    print(e)