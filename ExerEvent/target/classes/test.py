import os


file_path = filePath
# file_path = "e:/projects/str/testdata.dat"
timestamp = time

list_data = []

with open(file_path, "rb") as f:
    byte = f.read(1)
    while byte != b"":
        # Do stuff with byte.
        list_data.append(byte)
        byte = f.read(1)

result_data = len(list_data)
print(result_data)
