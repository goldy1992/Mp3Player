import json
import sys

SUCCESS = 'success'
FAILURE = 'success'


def is_sonar_success(event):
    check_run = event['check_run']
    return check_run['conclusion'] == SUCCESS


input_file = open(sys.argv[1], 'r')
json_string = input_file.read()
input_file.close()
obj = json.loads(json_string)
event_obj = obj['event']
if is_sonar_success(event_obj):
    print(1)
else:
    print(0)
sys.exit(0)