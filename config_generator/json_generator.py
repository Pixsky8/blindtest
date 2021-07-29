import json
from pathlib import Path
import shlex
import subprocess
import yaml

"""
Dependencies:
* pip
    - youtube-dl
    - pyyaml
"""


class SongData:
    theme: str
    title: str
    author: str
    link: str
    note: str

    def __init__(self, theme, title, author, link, note):
        self.theme = theme
        self.title = title
        self.author = author
        self.link = link
        self.note = note

    @staticmethod
    def load_from_yaml(values, theme):
        title = values.get("title")
        author = values.get("author")
        link = values.get("link")
        note = values.get("note")
        return SongData(theme, title, author, link, note)

    def __repr__(self):
        return "%s(title=%r, author=%r, link=%r, note=%r)" % (
            self.__class__.__name__, self.title, self.author, self.link, self.note)


class QuestionData:
    id: int
    theme: str
    audio: bool
    audioFile: str
    question: str

    def __init__(self, id: int, theme: str, audioFile: str):
        self.id = id
        self.theme = theme
        self.audio = True
        self.audioFile = audioFile
        self.question = ""


def find_files() -> list:
    """
    Return the list of config files.
    """
    for path in Path('.').rglob('*.yaml'):
        yield path


def fix_url(url: str) -> str:
    i = 0
    while i < len(url):
        if (url[i] == "?" or url[i] == "="):
            url = url[:i] + "\\" + url[i:]
            i += 1
        i += 1
    print(url)
    return url


def download_file(link: str, base_path: str) -> str:
    """
    Download music from youtube link
    Returns the path of the file
    """
    print("Downloading \"" + link + "\"")
    print("Path: " + base_path)
    result = subprocess.run(args=["youtube-dl", "-f", "bestaudio",
                                  "--print-json", "-o", "/tmp/" +
                                  "%(title)s_%(id)s.%(ext)s", "-x",
                                  "--audio-format", "mp3", link],
                            capture_output=True)

    if result.returncode != 0:
        print("Could not download " + link)
        print(result.stdout)
        print(result.stderr)
        return None

    json_data = json.loads(result.stdout)
    old_file_name = json_data.get("_filename")
    file_name = str(Path(
        old_file_name).parents[0]) + "/" + Path(old_file_name).stem + ".mp3"

    new_path = base_path + "/" + Path(file_name).name

    #subprocess.Popen(["mv", file_name, new_path])

    return new_path


def main():
    questions = []
    for file_str in find_files():
        print(file_str)
        file = open(file_str)
        data = file.read()
        yaml_data = yaml.safe_load(data)
        for title in yaml_data.keys():
            print("\n" + title)
            for song_yaml in yaml_data.get(title):
                song = SongData.load_from_yaml(song_yaml, title)
                print(song)
                new_question = QuestionData(len(questions),
                                            song.theme,
                                            "/data/" + download_file(song.link, str(file_str.parents[0])))
                questions.append(new_question.__dict__)
                print('"""\n', str(json.dumps(questions)), '\n"""\n')
        print("\n")


main()
