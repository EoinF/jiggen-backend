import requests

from config import BASIC_AUTH


def create_playable_puzzle(endpoint, generated_template_id, background_id, release_date):
    payload = {
        'generatedTemplateId':  generated_template_id,
        'backgroundId': background_id,
        'releaseDate': release_date.isoformat()
    }

    res = requests.post(endpoint, json=payload, auth=BASIC_AUTH)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}\n{res.text}')
        exit(1)

    return res.json()
