import requests


def upload_image(endpoint, image_file_path):
    data = open(image_file_path, 'rb').read()

    res = requests.put(url=endpoint, data=data)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}')
        exit(1)
