import requests
from config import BASIC_AUTH


def upload_image(endpoint, image_file_path):
    data = open(image_file_path, 'rb').read()

    res = requests.put(url=endpoint, data=data, auth=BASIC_AUTH)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}\n{res.text}')
        exit(1)
