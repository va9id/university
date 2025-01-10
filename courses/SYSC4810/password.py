import os, hashlib, re

SPECIAL_CHARACTERS = {"!", "@", "#", "$", "%", "?", "*"}
COMMON_FORMATS = [
    # Calendar date formats
    r"\d{4}-\d{2}-\d{2}",
    r"\d{2}-\d{2}-\d{4}",
    # License plate formats
    r"\b[A-Z0-9]{1,8}\b",
    r"\b[A-Z]{2}-\d{2}-\d{2}\b",  # AA-99-99 format
    r"\b\d{2}-[A-Z]{2}-\d{2}\b",  # 99-AA-99 format
    r"\b\d{2}-\d{2}-[A-Z]{2}\b",  # 99-99-AA format
    # Phone number formats
    r"\b\d{3}[-.\s]?\d{3}[-.\s]?\d{4}\b",
    r"\b\(\d{3}\)[-.\s]?\d{3}[-.\s]?\d{4}\b",
    r"\b\+\d{1,4}[-.\s]?\d{1,}\b",
    # Sin number formats
    r"\d{3}-\d{2}-\d{4}",
    r"\d{3}\.\d{2}\.\d{4}",
]


def valid_password(username: int, password: str, exclusions: list = None) -> bool:
    if not (8 <= len(password) <= 12):
        print("Password must be between 8 and 12 characters")
        return False

    if not any(c.isupper() for c in password):
        print("Password must contain an uppercase character")
        return False

    if not any(c.islower() for c in password):
        print("Password must contain a lowercase character")
        return False

    if not any(c.isdigit() for c in password):
        print("Password must contain a numerical digit")
        return False

    if not any(c in SPECIAL_CHARACTERS for c in password):
        print("Password must contain a special character {!, @, #, $, %, ?, *}")
        return False

    if username in password:
        print("Password cannot contain the username")
        return False

    if any(re.match(format, password) for format in COMMON_FORMATS):
        print("Password cannot be in a numerical format")
        return False

    if any(weak_pass.lower() in password.lower() for weak_pass in exclusions):
        print("Password is too weak")
        return False

    return True


def check_username_exists(username: str) -> bool:
    with open("etc/passwd.txt", "r") as passwd_file:
        for line in passwd_file:
            record = line.strip().split(":")
            if record and record[0] == username:
                return True
    return False


def get_user_record(username: str) -> list:
    with open("etc/passwd.txt", "r") as passwd_file:
        for line in passwd_file:
            record = line.strip().split(":")
            if record and record[0] == username:
                return record
    return []


def login(username: str, password: str) -> bool:
    with open("etc/passwd.txt", "r") as passwd_file:
        for line in passwd_file:
            record = line.strip().split(":")
            if record and record[0] == username:
                if hash_password(record[1], password) == record[2]:
                    return True
    return False


def write_to_passwd(username: str, password: str, role: str) -> None:
    salt = str(os.urandom(32).hex())  # 32 bytes = 256 bits
    hashed_pass = hash_password(salt, password)
    record = f"{username}:{salt}:{hashed_pass}:{role}\n"
    with open("etc/passwd.txt", "a") as passwd_file:
        passwd_file.write(record)


def hash_password(salt: str, password: str) -> str:
    return hashlib.sha256((salt + password).encode()).hexdigest()


def get_exclusions() -> list:
    with open("etc/exclusions.txt", "r") as exclusions:
        return exclusions.read().splitlines()


def add_exclusion(exclusion: str) -> None:
    with open("etc/exclusions.txt", "a") as exclusions:
        exclusions.write(f"{exclusion}\n")
