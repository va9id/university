from main import *
from image_functionality import load_image, save_as, show

SAVE_LOCATION = "batch/images"
#filename = input("\nPlease enter the batch filename you wish to use\n")
filename = "batch/batch_script.txt"

with open (filename, 'r') as reader: 
    for line in reader.readlines():
        items = line.split(" ")
        image = load_image(items.pop(0))
        save_name = items.pop(0)
        show(image)
        for cmd in items:
            cmd = cmd.strip()
            image = ALL_COMMANDS[cmd.upper()](image)
        save_as(image, f"{SAVE_LOCATION}/{save_name}")

print(f"Your filtered images are saved in \'{SAVE_LOCATION}/...\'")
        
  