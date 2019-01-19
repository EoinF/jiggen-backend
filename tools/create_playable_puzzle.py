import requests


def create_playable_puzzle(endpoint, generated_template_id, background_id, release_date):

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
