import requests

from config import BASE_ENDPOINT


def get_base_links(endpoint=BASE_ENDPOINT):
    return get_base_resource(endpoint)['links']

def get_base_resource(endpoint=BASE_ENDPOINT):
	return requests.get(endpoint).json()

if __name__ == '__main__':
    print(get_base_links())
