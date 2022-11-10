import sys

signing_key_file_path = sys.argv[1]
base_64_key = sys.argv[2]

print(f'signing key file path: {signing_key_file_path}')

with open(signing_key_file_path, "w", encoding="utf-8") as f:
    print("file created and opened successfully")
    f.write(base_64_key)

f.closed