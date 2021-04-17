from github import Github
import json
import sys

SUCCESS = 'success'
FAILURE = 'failure'
MASTER_BRANCH = 'master'
USERNAME = 'goldy1992'
PROJECT = 'Mp3Player'


def get_latest_master_commit_ref():
    github = Github()
    user = github.get_user(USERNAME)
    repo = user.get_repo(PROJECT)
    master_branch = repo.get_branch(MASTER_BRANCH)
    return master_branch.commit.sha


def is_latest_master_commit(event_obj):
    commit_ref = event_obj['sha']
    expected_commit_ref = get_latest_master_commit_ref()
    result = commit_ref == expected_commit_ref

    print("master latest commit ref: " + expected_commit_ref)
    print("event commit ref: " + commit_ref)

    if result:
        print("Validating Quality Gate of " + MASTER_BRANCH)
    else:
        print("Incorrect commit reference in event")
    return result


def is_sonar_event(event_obj):
    result = False
    if event_obj is not None:
        check_run = event_obj['check_run']
        if check_run is not None:
            app = check_run['app']
            if app is not None:
                name = app['name']
                if name == 'SonarCloud':
                    result = True
    if result:
        print("SonarCloud event found.")
    else:
        print("Event is NOT a SonarCloud event.")
    return result


def is_sonar_success(event_obj):
    check_run = event_obj['check_run']
    result = check_run['conclusion'] == SUCCESS

    if result:
        print("Sonar Analysis was success.")
    else:
        print("Sonar Analysis was NOT success.")
    return result


print("Running Sonar Quality Gate Check")
input_file = open(sys.argv[1], 'r')
json_string = input_file.read()
input_file.close()
obj = json.loads(json_string)
event = obj['event']
if is_sonar_event(event) and is_sonar_success(event) and is_latest_master_commit(obj):
    print("Sonar checks passed.")
    print(1)
else:
    print("Sonar checks failed.")
    print(0)
sys.exit(0)
