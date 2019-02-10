import copy
import datetime
import json
import os
import random
import sys
import time
from os.path import isfile, join
from random import randint
from time import sleep

import dateutil.parser as date_parser
import requests
from config import TEMPLATE_FILE_PATH, BACKGROUND_FILE_PATH, RELEASE_DATE, TEMPLATE_DIRECTORY_PATH, \
    NAMES_FILE, USED_NAMES_FILE, BACKGROUNDS_DIRECTORY_PATH, PUZZLE_OF_THE_DAY_START_DATE
from create_background import create_background
from create_playable_puzzle import create_playable_puzzle
from create_template import create_template
from get_base_links import get_base_links
from upload_image import upload_image


def setup_template(templates_link, template_file_path, template_name='Untitled template'):
    template, headers = create_template(templates_link, template_file_path.split('.')[-1], template_name)
    template_link = template['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {template_link}')
    upload_image(image_link, template_file_path)
    print(f'Successfully uploaded image {template_file_path} at {image_link}')

    return template


def setup_templates(templates_link, templates_directory_path):
    original_names = open(NAMES_FILE).read().splitlines()
    used_names = open(USED_NAMES_FILE).read().splitlines()

    names = [n for n in original_names if n not in used_names]
    names_remaining = copy.copy(names)
    directory_files = [join(templates_directory_path, f) for f in os.listdir(templates_directory_path)
                       if isfile(join(templates_directory_path, f)) and f.endswith(".png")]

    for file_path in directory_files:
        name = names_remaining.pop(randint(0, len(names_remaining) - 1))
        setup_template(templates_link, file_path, name.strip())
        # Don't overload the server with requests
        time.sleep(1)

    with open(USED_NAMES_FILE, 'w') as f:
        f.write("\n".join([n for n in names if n not in names_remaining]))


def find_file_with_extension(original_path, extension):
    path_without_extension = "".join((original_path.split('.')[:-1]))
    return f'{path_without_extension}.{extension}'


def setup_backgrounds(backgrounds_link, release_date):
    metadata_files = open(f'{BACKGROUNDS_DIRECTORY_PATH}/order.txt')

    background_metadata_paths = [f'{BACKGROUNDS_DIRECTORY_PATH}/{line}' for line in metadata_files.read().splitlines()]
    meta_data_list = [json.loads(open(path).read()) for path in background_metadata_paths]
    meta_data_with_paths = zip(background_metadata_paths, meta_data_list)

    meta_data_with_image_paths = [(find_file_with_extension(path, data['extension']), data)
                                  for (path, data) in meta_data_with_paths]

    def _setup_background(path, data, release_date):
        background, headers = create_background(backgrounds_link, data["extension"], release_date, data["name"], data["tags"])
        image_link = headers['Location']
        upload_image(image_link, path)
        return background, release_date

    def release_dates(r):
        date = r
        while True:
            yield date
            date += datetime.timedelta(days=1)

    meta_data_with_image_paths_and_release_dates = zip(meta_data_with_image_paths, release_dates(release_date))

    return [_setup_background(path, data, release_d)
            for ((path, data), release_d) in meta_data_with_image_paths_and_release_dates]


def setup_background(backgrounds_link, release_date):
    background, headers = create_background(backgrounds_link, BACKGROUND_FILE_PATH.split('.')[-1], release_date)
    background_link = background['links']['self']
    image_link = headers['Location']
    print(f'Successfully created background at {background_link}')
    upload_image(image_link, BACKGROUND_FILE_PATH)
    print(f'Successfully uploaded image {BACKGROUND_FILE_PATH} at {image_link}')

    return background


def get_generated_template(generated_templates_link):
    generated_templates = []
    retries = 10
    while len(generated_templates) == 0 and retries > 0:
        generated_templates = requests.get(url=generated_templates_link).json()
        print(f'No generated template yet. Sleeping for {100 * retries}ms...')
        sleep(0.1 * retries)
        retries -= 1

    return generated_templates[0]


def setup_playable_puzzle(playable_puzzles_link, generated_template, background):
    release_date = date_parser.parse(RELEASE_DATE).replace(tzinfo=datetime.timezone.utc)
    playable_puzzle = create_playable_puzzle(playable_puzzles_link, generated_template['id'], background['id'],
                                             release_date)

    print(f"""Successfully created playable puzzle 
at {playable_puzzle['links']['self']} with
    Generated Template {generated_template['id']}
    and Background {background['id']}""")
    return playable_puzzle


if __name__ == '__main__':
    if len(sys.argv) == 1:
        print("Usage: python {sys.argv[0]} (command)")
        print("""Commands: 
            test - Fetches and prints the base resource
            setup - Creates a template, a background and a playable puzzle
            setup_templates - Creates templates for each image in the configured templates folder
            setup_puzzles - Creates a playable puzzle for each image in the configured images folder
        """)
        exit(0)
    else:
        arg = sys.argv[1]
        if arg == 'test':
            print(get_base_links())
        elif arg == 'setup':
            links = get_base_links()

            _template = setup_template(links['templates'], TEMPLATE_FILE_PATH)
            _background = setup_background(links['backgrounds'],
                                           date_parser.parse(RELEASE_DATE).replace(tzinfo=datetime.timezone.utc))

            _generated_template = get_generated_template(_template['links']['generatedTemplates'])
            _playable_puzzle = setup_playable_puzzle(links['playablePuzzles'], _generated_template, _background)
        elif arg == 'setup_templates':
            links = get_base_links()

            setup_templates(links['templates'], TEMPLATE_DIRECTORY_PATH)
        elif arg == 'setup_puzzles':
            links = get_base_links()
            playable_puzzles_endpoint = links['playablePuzzles']

            templates = requests.get(links['generatedTemplates']).json()
            templates_enhanced = [requests.get(t["links"]["self"]).json() for t in templates]
            templates_enhanced.sort(key=lambda t: len(t["vertices"]))

            print('Found the following templates with piece counts:')
            print([len(t["vertices"]) for t in templates_enhanced])
            print()

            buckets = {
                "0-20": [t for t in templates_enhanced if len(t["vertices"]) <= 20],
                "21-80": [t for t in templates_enhanced if 20 < len(t["vertices"]) <= 80],
                "81-200": [t for t in templates_enhanced if 80 < len(t["vertices"]) <= 200]
            }

            base_release_date = date_parser.parse(PUZZLE_OF_THE_DAY_START_DATE).replace(tzinfo=datetime.timezone.utc)
            print(f'Creating puzzles starting from {base_release_date}')

            backgrounds = setup_backgrounds(links['backgrounds'], base_release_date)

            for _background, _release_date in backgrounds:
                background_id = _background["id"]

                print(f'Uploading 3 playable puzzles for {_background["name"]} for {_release_date}')

                create_playable_puzzle(playable_puzzles_endpoint, random.choice(buckets["0-20"])["id"], background_id,
                                       _release_date)
                create_playable_puzzle(playable_puzzles_endpoint, random.choice(buckets["21-80"])["id"], background_id,
                                       _release_date)
                create_playable_puzzle(playable_puzzles_endpoint, random.choice(buckets["81-200"])["id"], background_id,
                                       _release_date)
                time.sleep(1)

