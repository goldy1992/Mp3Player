import json
import sys


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


input_file = open(sys.argv[1], 'r')
json_string = input_file.read()
input_file.close()
obj = json.loads(json_string)
event_obj = obj['event']
if is_sonar_event(event_obj):
    print(1)
else:
    print(0)
sys.exit(0)


