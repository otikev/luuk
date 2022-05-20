#!/usr/bin/env python3

import requests
from requests.auth import HTTPBasicAuth
import json
from pathlib import Path

file_ids = ''
headers={'LuukToken': '','DryRun':'true'}
# Upload file

f = open('sample.csv', 'rb')

files = {"file": ("sample.csv", f,'text/csv')}

resp = requests.post("https://luukatme.herokuapp.com/bulk/csv/catalog", files=files, headers=headers )
print (resp.text)
print ("status code " + str(resp.status_code))

if resp.status_code == 200:
    print ("Success")
else:
    print ("Failure")
