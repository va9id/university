from flask import g
from image_functionality import *

COLOURS = {
    "black": (0, 0, 0),
    "white": (255, 255, 255),
    "red": (255, 0, 0),
    "lime": (0, 255, 0),
    "blue": (0, 0, 255),
    "yellow": (255, 255, 0),
    "cyan" : (0, 255, 255),
    "magenta": (255, 0, 255),
    "gray": (128, 128, 128)
} 

def red_channel(img):
    filtered_img = copy(img)
    for h in range(0, get_height(img)):
        for w in range(0, get_width(img)):
            color = get_color(img, h, w)
            new_color = color[0]
            set_color(filtered_img, h, w, create_color(new_color, 0, 0))
    return filtered_img

def green_channel(img):
    filtered_img = copy(img)
    for h in range(0, get_height(img)):
        for w in range(0, get_width(img)):
            color = get_color(img, h, w)
            new_color = color[1]
            set_color(filtered_img, h, w, create_color(0, new_color, 0))
    return filtered_img

def blue_channel(img): 
    filtered_img = copy(img)
    for h in range(0, get_height(img)):
        for w in range(0, get_width(img)):
            color = get_color(img, h, w)
            new_color = color[2]
            set_color(filtered_img, h, w, create_color(0, 0, new_color))
    return filtered_img

def combine(red_img, green_img, blue_img):
    height = get_height(red_img)
    width = get_width(red_img)
    filtered_img = create_image(width, height)
    for y in range(0, height):
        for x in range(0, width):
            r = get_color(red_img, x, y)
            g = get_color(green_img, x, y)
            b = get_color(blue_img, x, y)
            set_color(filtered_img, x, y, create_color(r[0], g[1], b[2]))
    return filtered_img

def two_tone(image, colour1, colour2): 
    filtered_img = copy(image)
    tone1 = COLOURS.get(colour1)
    tone2 = COLOURS.get(colour2)
    new_colour1 = create_color(tone1[0], tone1[1], tone1[2])
    new_colour2 = create_color(tone2[0], tone2[1], tone2[2])
            
    for pixel in filtered_img:
        x, y, (r, g, b) = pixel 
        brightness = (r+g+b) // 3
        if 0 <= brightness <= 127: 
            set_color(filtered_img, x, y, new_colour1)   
        elif 128 <= brightness <= 255: 
            set_color(filtered_img, x, y, new_colour2) 
    return filtered_img

def three_tone(image, colour1, colour2, colour3): 
    filtered_img = copy(image)
    tone1 = COLOURS.get(colour1)
    tone2 = COLOURS.get(colour2)
    tone3 = COLOURS.get(colour2)
    new_colour1 = create_color(tone1[0], tone1[1], tone1[2])
    new_colour2 = create_color(tone2[0], tone2[1], tone2[2])
    new_colour3 = create_color(tone3[0], tone3[1], tone3[2])

    for pixel in filtered_img:
        x, y, (r, g, b) = pixel 
        brightness = (r+g+b) // 3
        if 0 <= brightness <= 84: 
            set_color (filtered_img, x, y, new_colour1)   
        elif 85 <= brightness <= 170: 
            set_color(filtered_img, x, y, new_colour2) 
        elif 171 <= brightness <= 255: 
            set_color(filtered_img, x, y, new_colour3)         
    return filtered_img

def extreme_contrast(img):
    filtered_img = copy(img)
    for h in range(0, get_height(img)):
        for w in range(0, get_width(img)):
            color = get_color(img, w , h)
            r = 255 if 0 <= color[0] <= 127 else 0
            g = 255 if 0 <= color[1] <= 127 else 0
            b = 255 if 0 <= color[2] <= 127 else 0
            set_color(filtered_img , w, h, create_color(r, g, b))
    return filtered_img
    
def sepia(img):
    filtered_img = copy(img)
    gray_img = grayscale(filtered_img)
    for h in range(0, get_height(gray_img)):
        for w in range(0, get_width(gray_img)):
            color = get_color(gray_img, w, h)
            if color[0] < 63:
                b = color[2] * 0.9
                r = color[0] * 1.1
            elif 63 <= color[0] <=191:
                b = color[2] * 0.85
                r = color[0] * 1.15
            elif color[0] > 191:
                b = color[2] * 0.93
                r = color[0] * 1.08               
            set_color(filtered_img, w, h, create_color(r, color[1], b))
    return filtered_img

def _adjust_component(val : int) -> int:
    if 0 <= val <= 63:
        return 31
    elif 64 <= val <= 127:
        return 95
    elif 128 <= val <= 191: 
        return 159
    elif 192 <=val <= 255:
        return 223
    else: 
        return 0

def posterize(img):
    filtered_img = copy(img)
    for pixel in img:
        x, y, (r, g, b) = pixel 
        r =_adjust_component(r)
        g =_adjust_component(g)
        b =_adjust_component(b) 
        set_color(filtered_img, x, y, create_color(r, g, b))
    return filtered_img

def detect_edges(img, threshold):
    filtered_img = copy(img)
    for w in range(0, get_width(img)):
        set_color(filtered_img, w, get_height(img) - 1, create_color(255, 255, 255))
    for h in range(0, get_height(img) - 1):
        for w in range(0, get_width(img)):
            color1 = get_color(img, w, h)
            color2 = get_color(img, w, h + 1)
            brightness1 = (color1[0] + color1[1] + color1[2]) / 3
            brightness2 = (color2[0] + color2[1] + color2[2]) / 3
            difference = abs(brightness2- brightness1)
            if difference > threshold:
                set_color(filtered_img, w, h, create_color(0, 0, 0))
            else:
                set_color(filtered_img, w, h, create_color(255, 255, 255))
    return filtered_img

def detect_edges_better(img, threshold):
    filtered_img = copy(img)
    height = get_height(img)
    width = get_width(img)
    set_color(filtered_img,  width - 1, height - 1, create_color(255, 255, 255))
    for h in range(0, height - 1):
        for w in range(0, width):
            color1 = get_color(img, w, h)
            color2 = get_color(img, w, h + 1)
            brightness1 = (color1[0] + color1[1] + color1[2]) / 3
            brightness2 = (color2[0] + color2[1] + color2[2]) / 3
            difference1 = abs(brightness1 - brightness2)
            if difference1 > threshold:
                set_color(filtered_img, w, h, create_color(0, 0, 0))
            else:
                set_color(filtered_img, w, h, create_color(255, 255, 255)) 

    for h in range(0, height):
        for w in range(0, width - 1):
            color3 = get_color(img, w, h)
            color4 = get_color(img, w + 1, h)
            brightness3 = (color3[0] + color3[1] + color3[2]) / 3
            brightness4 = (color4[0] + color4[1] + color4[2]) / 3
            difference2 = abs(brightness3 - brightness4)
            if difference2 > threshold:
                set_color(filtered_img, w, h, create_color(0, 0, 0))
            else:
                set_color(filtered_img, w, h, create_color(255, 255, 255))
    return filtered_img

def flip_vertical(image):
    filtered_img = copy(image)
    for w in range(get_width(image)):
        for h in range(get_height(image)):
            color = get_color(image, w, h)
            set_color(filtered_img, get_width(image) - w - 1, h, color)
    return filtered_img

def flip_horizontal(image): 
    filtered_image = copy(image)
    for h in range(get_height(image)):
        for w in range(get_width(image)):
            color = get_color(image, w, h)
            set_color(filtered_image, w, get_height(image) - 1 - h, color)    
    return filtered_image

def invert(img):
    filtered_img = copy(img)
    for w, h, (r, g, b) in img:
        color = create_color(255 - r, 255 - g, 255 - b)
        set_color(filtered_img, w, h, color)
    return filtered_img

def grayscale_from_red(img):
    filtered_img = copy(img)
    for w, h, (r, g, b) in img:
        color = create_color(r, r, r)
        set_color(filtered_img, w, h, color)       
    return filtered_img
        
def grayscale_from_green(img):
    filtered_img = copy(img)
    for w, h, (r, g, b) in img:
        color = create_color(g, g, g)    
        set_color(filtered_img, w, h, color)       
    return filtered_img

def grayscale_from_blue(img):
    filtered_img = copy(img)
    for w, h, (r, g, b) in img:
        color = create_color(b, b, b)      
        set_color(filtered_img, w, h, color)        
    return filtered_img

def grayscale(img):
    filtered_img = copy(img)
    for w, h, (r, g, b) in img:
        brightness = (r + g + b) // 3
        color = create_color(brightness, brightness, brightness)
        set_color(filtered_img, w, h, color)      
    return filtered_img