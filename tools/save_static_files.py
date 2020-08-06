
from config import BASE_ENDPOINT
from config import JIGGEN_BASE_PATH
import re, os
import requests
import shutil
import json

links_traversed = []


def save_static_links(subdirectory, resource_or_list, links_traversed = []):
	if isinstance(resource_or_list, list):
		for resource in resource_or_list:
			save_resource(subdirectory, resource, links_traversed)
	else:
		save_resource(subdirectory, resource_or_list, links_traversed)

	directory = f"{JIGGEN_BASE_PATH}{subdirectory}"
	os.makedirs(directory, exist_ok=True)
	with open(f"{directory}/resource.json", 'w+') as f:
	    f.write(json.dumps(resource_or_list))


def get_extension(href):
	extension_types = ['.png', '.atlas', 'jpg']

	for extension_type in extension_types:
		if href.endswith(extension_type):
			return extension_type
	
	return '.json'

def save_resource(subdirectory, resource, links_traversed):
	for name, href_or_list in resource['links'].items():
		hrefs = href_or_list if isinstance(href_or_list, list) else [href_or_list]
		
		for idx, href in enumerate(hrefs):
			resource_path =	href.replace(BASE_ENDPOINT, '')
			extension = get_extension(href)
			updated_href = href.replace(BASE_ENDPOINT, 'https://api.jiggen.app')
			if extension == '.json':
				if href not in links_traversed:
					links_traversed.append(href)
					sub_resource = requests.get(href).json()
					save_static_links(resource_path, sub_resource)

				updated_href += '/resource.json'

			if isinstance(href_or_list, list):
				resource['links'][name][idx] = updated_href
			else:
				resource['links'][name] = updated_href
