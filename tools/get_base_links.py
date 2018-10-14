import requests

from config import BASE_ENDPOINT


def get_base_links(endpoint=BASE_ENDPOINT):
    res = requests.get(endpoint)
    return res.json()['links']


if __name__ == '__main__':
    print(get_base_links())
