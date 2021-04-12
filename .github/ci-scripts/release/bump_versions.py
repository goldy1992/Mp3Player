# requires: pip install pyjavaproperties

import sys
from pyjavaproperties import Properties

VERSION_NAME = 'version.name'
VERSION_CODE = 'version.code'

file = sys.argv[1]
new_version = sys.argv[2]
props = Properties()
props.load(open(file))
props[VERSION_NAME] = new_version
props[VERSION_CODE] = str(int(props['version.code']) + 1)
props.store(open(file, 'w'))






