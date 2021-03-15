import json
import sys

SUCCESS = 'success'
FAIL = 'failure'


def is_sonar_event(event):
    if event is not None:
        check_run = event['check_run']
        if check_run is not None:
            app = check_run['app']
            if app is not None:
                name = app['name']
                if name == 'SonarCloud':
                    return True
    return False


def is_sonar_success(event):
    check_run = event['check_run']
    return check_run['conclusion'] == SUCCESS


input_file = open(sys.argv[1], 'r')
json_string = input_file.read()
obj = json.loads(json_string)
event = obj['event']
if is_sonar_event(event) and is_sonar_success(event):
    print(1)
else:
    print(0)
sys.exit(0)


