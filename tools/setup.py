import datetime
import os
import sys
from os.path import isfile, join
from random import randint
from time import sleep

import dateutil.parser as date_parser
import requests
from config import TEMPLATE_FILE_PATH, BACKGROUND_FILE_PATH, RELEASE_DATE, TEMPLATE_DIRECTORY_PATH, NAMES_FILE
from create_background import create_background
from create_playable_puzzle import create_playable_puzzle
from create_template import create_template
from get_base_links import get_base_links
from helpers.upload_image import upload_image


def setup_template(templates_link, template_file_path, template_name='Untitled template'):
    template, headers = create_template(templates_link, template_file_path, template_name)
    template_link = template['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {template_link}')
    upload_image(image_link, template_file_path)
    print(f'Successfully uploaded image {template_file_path} at {image_link}')

    return template


def setup_templates(templates_link, templates_directory_path):
    names = open(NAMES_FILE).readlines()
    directory_files = [join(templates_directory_path, f) for f in os.listdir(templates_directory_path)
                       if isfile(join(templates_directory_path, f)) and f.endswith(".png")]

    for file_path in directory_files:
        name = names.pop(randint(0, len(names) - 1))
        setup_template(templates_link, file_path, name.strip())


def setup_background(backgrounds_link):
    release_date = date_parser.parse(RELEASE_DATE).replace(tzinfo=datetime.timezone.utc)

    background, headers = create_background(backgrounds_link, release_date)
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
            setup - Creates a template and a background
        """)
        exit(0)
    else:
        arg = sys.argv[1]
        if arg == 'test':
            print(get_base_links())
        elif arg == 'setup':
            links = get_base_links()

            _template = setup_template(links['templates'], TEMPLATE_FILE_PATH)
            _background = setup_background(links['backgrounds'])

            _generated_template = get_generated_template(_template['links']['generatedTemplates'])
            _playable_puzzle = setup_playable_puzzle(links['playablePuzzles'], _generated_template, _background)
        elif arg == 'setup_templates':
            links = get_base_links()

            setup_templates(links['templates'], TEMPLATE_DIRECTORY_PATH)
