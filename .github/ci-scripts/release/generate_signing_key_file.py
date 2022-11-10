import sys

signing_key_file_path = sys.argv[1]
base_64_key = sys.argv[2]

with open(signing_key_file_path, "w+", encoding="utf-8") as f:
    f.write(base_64_key)

f.closed