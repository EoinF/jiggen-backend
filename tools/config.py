import os

from requests.auth import HTTPBasicAuth

# BASE_ENDPOINT = "https://api.jiggen.app"
BASE_ENDPOINT = "http://localhost:4567"
TEMPLATE_FILE_PATH = "data/template.jpg"
BACKGROUND_FILE_PATH = "data/background.jpg"
TEMPLATE_DIRECTORY_PATH = "D:\Dropbox\Dropbox\Current\Jiggen\generated-templates"
BACKGROUNDS_DIRECTORY_PATH = "D:/Dropbox/Dropbox/Current/Jiggen/backgrounds"
NAMES_FILE = "data/greek-gods+goddesses.txt"
USED_NAMES_FILE = "data/used.txt"

RELEASE_DATE = '2019-01-19T12:21:30'

PUZZLE_OF_THE_DAY_START_DATE = '2019-02-09T04:00:00'

BASIC_AUTH = HTTPBasicAuth(os.environ['AUTH_USERNAME'], os.environ['AUTH_PASSWORD'])
