from subprocess import run as process_run
from sys import argv

ENV_FILE = "example.env"
COMPOSE_FILE = "docker-compose.yml"

MAIN_COMMAND = ["docker", "compose", "-f", COMPOSE_FILE, "--env-file", ENV_FILE]

def up():
    return MAIN_COMMAND + ["up", "--build"]

def deploy():
    return MAIN_COMMAND + ["up", "--build", "-d"]

def down():
    return MAIN_COMMAND + ["down", "--remove-orphans"]


func_args = {
    "shell": True,
}

first_cmd_arg = argv[1]

if first_cmd_arg == "up":
    func_args.update({"args": up()})
elif first_cmd_arg == "deploy":
    func_args.update({"args": deploy()})
else:
    func_args.update({"args": down()})

if func_args.get("args", None):
    process_run(**func_args)
