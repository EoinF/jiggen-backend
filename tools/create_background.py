import json

import requests
from config import BACKGROUND_FILE_PATH, BASIC_AUTH
from get_base_links import get_base_links
from upload_image import upload_image


def create_background(endpoint, extension, release_date=None, name="Untitled background",
                      tags=list()):
    payload = {
        'name': name,
        'extension': extension,
        'releaseDate': release_date.isoformat(),
        'tags': json.dumps(tags)
    }
    res = requests.post(endpoint, json=payload, auth=BASIC_AUTH)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}\n{res.text}')
        exit(1)

    return res.json(), res.headers


if __name__ == "__main__":
    links = get_base_links()

    background, headers = create_background(links['backgrounds'], 'png')
    background_link = background['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {background_link}')

    upload_image(image_link, BACKGROUND_FILE_PATH)
    print(f'Successfully uploaded image {BACKGROUND_FILE_PATH} at {image_link}')
