import requests
import re
import json
import pandas as pd

keywords = ['surgical mask', 'medical alcohol', 'thermometer', 'hand sanitizer', 'gloves', 'facial tissue', 'cup noodle']
def get_detail(keyword):
	keyword = keyword
	keyword_escape = re.escape(keyword)
	url = 'http://35.236.4.22:8080/api/ebay_search_keyword?keyword=' + keyword_escape
	r = requests.get(url)
	results = []
	findItemsByKeywordsResponse = json.loads(r.text)['findItemsByKeywordsResponse'][0]
	serach_results = findItemsByKeywordsResponse['searchResult']
	time = findItemsByKeywordsResponse['timestamp']
	for serach_result in serach_results:
		items = serach_result['item']
		for item in items:
			item_dict = {}
			item_dict['currency'] = item['sellingStatus'][0]['convertedCurrentPrice'][0]['@currencyId']
			item_dict['price'] = item['sellingStatus'][0]['convertedCurrentPrice'][0]['__value__']
			item_dict['time'] = time
			results.append(item_dict)
	return results

keyword_results = []
for keyword in keywords:
	item_dict = {}
	results = get_detail(keyword)
	json_file = json.dumps(results)
	df = pd.read_json (json_file)
	item_dict['keyword'] = keyword
	item_dict['std'] = df['price'].std()
	item_dict['mean'] = df['price'].mean()
	item_dict['min'] = df['price'].min()
	item_dict['max'] = df['price'].max()
	item_dict['time'] = df['time'].any()
	keyword_results.append(item_dict)

with open('ebay_data.json', 'w') as outfile:
    json.dump(keyword_results, outfile)
	

