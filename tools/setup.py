import sys
from time import sleep

import requests
from config import TEMPLATE_FILE_PATH, BACKGROUND_FILE_PATH
from create_background import create_background
from create_playable_puzzle import create_playable_puzzle
from create_template import create_template
from get_base_links import get_base_links
from helpers.upload_image import upload_image


def setup_template(templates_link):
    template, headers = create_template(templates_link)
    template_link = template['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {template_link}')
    upload_image(image_link, TEMPLATE_FILE_PATH)
    print(f'Successfully uploaded image {TEMPLATE_FILE_PATH} at {image_link}')

    return template


def setup_background(backgrounds_link):
    background, headers = create_background(backgrounds_link)
    background_link = background['links']['self']
    image_link = headers['Location']
    print(f'Successfully created template at {background_link}')
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
    playable_puzzle = create_playable_puzzle(playable_puzzles_link, generated_template['id'], background['id'])

    print(f'''Successfully created playable puzzle at {playable_puzzle['links']['self']} with 
        generated template {generated_template['id']} and background {background['id']}
    ''')
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

            template = setup_template(links['templates'])
            background = setup_background(links['backgrounds'])

            generated_template = get_generated_template(template['links']['generatedTemplates'])
            playable_puzzle = setup_playable_puzzle(links['playablePuzzles'], generated_template, background)
