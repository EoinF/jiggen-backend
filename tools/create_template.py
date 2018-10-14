import requests
from config import TEMPLATE_FILE_PATH
from get_base_links import get_base_links
from helpers.upload_image import upload_image


def create_template(endpoint, image_file_path='template.jpg', name="Untitled template"):
    extension = image_file_path.split('.')[-1]
    payload = {
        'name': name,
        'extension': extension
    }
    res = requests.post(endpoint, json=payload)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}')
        exit(1)

    return res.json(), res.headers


if __name__ == "__main__":
    links = get_base_links()

    template, headers = create_template(links['templates'])
    template_link = template['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {template_link}')

    upload_image(image_link, TEMPLATE_FILE_PATH)
    print(f'Successfully uploaded image {TEMPLATE_FILE_PATH} at {image_link}')
