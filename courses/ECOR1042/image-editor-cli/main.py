import os 

from image_functionality import load_image, save_as, show
from filters import * 

def get_input() -> str:
    cmd = input(menu()).upper()
    while cmd not in ALL_COMMANDS:
        print("\tNo such command") 
        cmd = input(menu()).upper()
    else:
        return cmd

def load(image): 
    filename = choose_file()
    filename = os.path.basename(filename)
    #filename = input("Enter the name of an image file: ")
    image = load_image(filename)
    show(image)   
    if image == None: 
        print("Please load an image before applying a filter") 
    return image 
    
def save(image):
    if image == None:
        print("No image to save") 
    else:
        save_as(image)
        print("Your image was saved")

def two(image):  
    if image == None: 
        print("No image loaded") 
    else: 
        image = two_tone(image, 'yellow', 'cyan') 
        show(image)         
    return image 

def three(image):  
    if image == None: 
        print("No image loaded")
    else: 
        image = three_tone(image, 'yellow', 'magenta', 'cyan') 
        show(image)        
    return image 

def xtreme(image): 
    if image == None: 
        print("No image loaded")    
    else: 
        image = extreme_contrast(image) 
        show(image) 
    return image 

def tint(image):  
    if image == None: 
        print("No image loaded")     
    else: 
        image = sepia(image) 
        show(image) 
    return image 


def post(image):      
    if image == None: 
        print("No image loaded")     
    else: 
        image = posterize(image) 
        show(image) 
    return image


def edge(image):   
    if image == None: 
        print("No image loaded")
    else:
        threshold_val = int(input("Choose a threshold value: "))
        image = detect_edges(image, threshold_val ) 
        show(image) 
    return image 

def improve(image):   
    if image == None: 
        print("No image loaded") 
    else: 
        threshold_val = int(input("Choose a threshold value: "))
        image = detect_edges_better(image, threshold_val ) 
        show(image) 
    return image 

def vert(image):   
    if image == None: 
        print("No image loaded") 
    else:   
        image = flip_vertical(image)
        show(image) 
    return image 

def horz(image): 
    if image == None: 
        print("No image loaded")  
    else: 
        image = flip_horizontal(image)
        show(image) 
    return image   

def showImg(image):
    show(image)
    return 

def menu(): 
    menu_text = "----------------------------\
    \nL)oad image\tS)ave-as\tQ)uit\
    \n2)-tone\t3)-tone\tX)treme contrast\tT)int sepia\tP)osterize\
    \nE)dge detect\tI)mproved edge detect\tV)ertical flip\tH)orizontal flip\
    \n----------------------------\n: "    
    return menu_text

# All commands 
ALL_COMMANDS = { "L":load, "S": save, "2":two, "3":three, "X":xtreme, "T":tint,
             "P":post, "E":edge, "I":improve, "V":vert, "H":horz, "Q":None} 

if __name__ == "__main__": 
    #Mainscript 
    image = None 
    run_program = True 
    stop_program = False

    print("Below are the following commands. Please choose one and begin!")

    while run_program:
        cmd = get_input()
        if cmd != 'Q':
            image = ALL_COMMANDS[cmd](image)
        elif cmd == "Q": 
            print("Quitting program")
            run_program = stop_program 
        