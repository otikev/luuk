#!/usr/bin/env python3

import requests
from requests.auth import HTTPBasicAuth
import json
from pathlib import Path

file_ids = ''
headers={'LuukToken': 'kjhkjadshfkjadsfnkj348989fdj32498f8jh','DryRun':'false'}
# Upload file

f = open('sample.csv', 'rb')

files = {"file": ("sample.csv", f,'text/csv')}

resp = requests.post("http://localhost:8080/bulk/csv/catalog", files=files, headers=headers )
print (resp.text)
print ("status code " + str(resp.status_code))

if resp.status_code == 200:
    print ("Success")
else:
    print ("Failure")
