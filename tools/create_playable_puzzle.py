import datetime

import dateutil.parser as date_parser
import requests
from config import RELEASE_DATE


def create_playable_puzzle(endpoint, generated_template_id, background_id):
    release_date = date_parser.parse(RELEASE_DATE).replace(tzinfo=datetime.timezone.utc)

    payload = {
        'generatedTemplate': {
            'id': generated_template_id
        },
        'background': {
            'id': background_id
        },
        'releaseDate': release_date.isoformat()
    }
    res = requests.post(endpoint, json=payload)

    if res.status_code != 201:
        print(f'Failed request to {endpoint}\n{res}')
        exit(1)

    return res.json()
