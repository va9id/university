import os
import math

from tkinter import *
import tkinter.filedialog

import PIL.Image
import PIL.ImageTk

IMAGE_DIRECTORY = "images"

IMAGE_FILE_FORMATS = ['.bmp', '.gif', '.jpg', '.jpeg', '.png', '.tif', '.tiff']

def _adjust_component(comp):
    comp = int(comp)
    return max(0, min(255, comp))

class Color(tuple):
    """An RGB color.

    When an instance is created, the RGB component values are quietly adjusted,
    as required, to ensure that they are ints in the range 0..255, inclusive.

    Examples:
      Color(120, 60, 200) yields the color (120, 60, 200)
      Color(-120, 60, 280) yields the color (0, 60, 255)
      Color(120.0, 60.5, 200.2) yields the color (120, 60, 200)
    """
    __slots__ = () 

    def __new__(_cls, red, green, blue):
        return tuple.__new__(_cls, (_adjust_component(red),
                                    _adjust_component(green),
                                    _adjust_component(blue)))

    @classmethod
    def _make(cls, t):
        return tuple.__new__(cls, t)

    def __repr__(self):
        return 'Color(red={0[0]}, green={0[1]}, blue={0[2]})'.format(self)


class Image(object):
    """
    A Image is a wrapper for an instance of PIL's Image class.
    Supported image formats include: JPEG, GIF, TIFF, PNG and BMP.

    To load an image from a file:
       image = Image(a_filename)

    To create a blank image with specified dimensions:
        image = Image(width=width_in_pixels, height=height_in_pixels)

    By default, the blank image's color is white. A different image color can be
    specified with a Color object:
        image = Image(width=width_in_pixels, height=height_in_pixels
                      color=Cimpl.Color(red, green, blue))

    To duplicate an image:
        original = Image(...)
        duplicate = Image(image=original)
    """

    def __init__(self, filename=None, image=None,
                 width=None, height=None, color=Color(255, 255, 255)):
        if filename is not None: # Load image from file
            self.pil_image = PIL.Image.open(f"{IMAGE_DIRECTORY}/{filename}").convert("RGB")
            self.filename = filename
        elif image is not None:  # Copy an image
            self.pil_image = image.pil_image.copy()
            self.filename = None
        elif width is None and height is None and color is None:
            raise TypeError('Image(): called with no arguments?')
        elif width is None or height is None:
            raise TypeError('Image(): missing width or height argument')
        elif width > 0 and height  > 0:  # create a blank image
            self.pil_image = PIL.Image.new(mode="RGB", size=(width, height),
                                           color=tuple(color))
            self.filename = None
        else:
            raise ValueError('Image(): width and height must be > 0')
        self.zoomfactor = 1 
        self.pixels = self.pil_image.load() 

    def copy(self):
        dup = Image(image=self)
        return dup

    def set_zoom(self, factor):
        if isinstance(factor, int) and factor > 0:
            self.zoomfactor = factor
        else:
            raise ValueError("factor must be a positive integer")

    def get_width(self):
        return self.pil_image.size[0]

    def get_height(self):
        return self.pil_image.size[1]

    def get_filename(self):
        return self.filename

    def __iter__(self):
        for h in range(0, self.get_height()):
            for w in range(0, self.get_width()):
                col = Color._make(self.pixels[w, h])
                yield w, h, col

    def get_color(self, x, y):
        return Color._make(self.pixels[x, y])

    def set_color(self, x, y, color):
        if not isinstance(color, Color):
            raise TypeError('Parameter color is not a Color object')
        self.pixels[x, y] = tuple(color)

    def write_to(self, filename):
        if filename:
            ext = os.path.splitext(filename)[-1]
            if ext == '':
                raise ValueError('Filename has no extension')
            #Extension cannot be mixed case
            if ext in IMAGE_FILE_FORMATS or \
                   (ext.isupper() and ext.lower() in IMAGE_FILE_FORMATS):
                self.pil_image.save(filename)
                #self.set_filename_and_title(filename)
            else:
                raise ValueError("%s is not a supported image file format." \
                                  % ext)
        else:
            raise ValueError("Parameter filename is None.")

    def _zoom_image(self):
        copy = Image(width=self.get_width() * self.zoomfactor,
                       height=self.get_height() * self.zoomfactor,
                       color=Color(255, 255, 255))
        for x, y, col in self:
            scaled_x = x * self.zoomfactor
            scaled_y = y * self.zoomfactor
            for j in range(self.zoomfactor):
                for i in range(self.zoomfactor):
                    copy.set_color(scaled_x + i, scaled_y + j, col)
        return copy

    def show(self):
        root = Tk()
        pil_image = self.pil_image
        if self.zoomfactor != 1:
            # Make an enlarged copy of this image
            pil_image = self._zoom_image().pil_image
        if self.filename is None:
            view = ImageViewer(root, pil_image)
        else:
            title = os.path.basename(self.filename)
            view = ImageViewer(root, pil_image, title)
        root.lift()
        root.attributes('-topmost',True)
        root.after_idle(root.attributes,'-topmost',False)
        root.mainloop()
        
        

#---------------------------------------------------
# ImageViewer

class ImageViewer(object):
    def __init__(self, master, pil_image, title = "New Image"):
        """Initialize an image viewer (a Tk window) with parent widget master.
        pil_image is bound to the instance of PIL.Image.Image that contains
        the image to be displayed.
        """
        master.title(title)
        image_width = pil_image.size[0]
        image_height = pil_image.size[1]
        self.canvas = Canvas(master,
                             width=image_width,
                             height=image_height)
        self.photo_image = PIL.ImageTk.PhotoImage(pil_image)
        self.canvas.create_image(image_width // 2,
                                 image_height // 2,
                                 image = self.photo_image)
        self.canvas.pack()
        master.resizable(0, 0) # Window not resizable

#---------------------------------------------------
# "Global" Colour functions

def create_color(red, green, blue):
    return Color(red, green, blue)

def distance(color1, color2):
    """
    Return the Euclidean distance between two Color objects.
    """
    r1, g1, b1 = color1
    r2, g2, b2 = color2
    return math.sqrt((r1 - r2) ** 2 + (g1 - g2) ** 2 + (b1 - b2) ** 2)

#---------------------------------------------------------------------------
# "Global" Image functions

def load_image(filename):
    return Image(filename)

def create_image(width, height, color=Color(255, 255, 255)):
    return Image(width=width, height=height, color=color)

def copy(pict):
    return pict.copy()

def get_width(pict):
    return pict.get_width()

def get_height(pict):
    return pict.get_height()

def get_color(pict, x, y):
    return pict.get_color(x, y)

def set_color(pict, x, y, color):
    pict.set_color(x, y, color)

def save_as(pict, filename=None):
    """
    Save Image pict to the specified file. If no filename is supplied,
    first prompt the user to interactively choose a directory and
    filename.
      save_as(pict, 'mypicture.jpg') saves pict to mypicture.jpg
      save_as(pict) asks the user to choose the directory and filename
    """
    if not filename:
        # The suggested name for the file is the image's current filename,
        # if it has one; otherwise, use 'untitled'.
        if pict.get_filename():
            base = os.path.basename(pict.get_filename())
            initial = os.path.splitext(base)[0]
        else:
            initial = 'untitled'
        filename = choose_save_filename(initial)

    if filename:
        pict.write_to(filename)

def save(pict):
    """ Save Image pict to its file, overwriting the existing file.
    If this Image doesn't have a corresponding filename; i.e., this
    instance has not yet been written to a file, the user will be prompted
    to provide a filename.
    """
    name = pict.get_filename()
    if name:
        pict.write_to(name)
    else:
        save_as(pict)

def set_zoom(pict, factor):  
    pict.set_zoom(factor)

def show(pict):
    pict.show()

#---------------------------------
# "Global" File Dialogues

IMAGE_FILE_TYPES = [('All files', '.*'),
                    ('BMP', '.bmp'),
                    ('GIF', '.gif'),
                    ('PNG', '.png'),
                    ('TIFF', '.tif'),
                    ('TIFF', '.tiff'),
                    ('JPG', '.jpg'),
                    ('JPEG', '.jpeg')]

def choose_save_filename(initial=''):
    root = Tk()
    root.withdraw()
    path = tkinter.filedialog.asksaveasfilename(
        initialfile=initial, 
        defaultextension='.jpg'
    )
    root.destroy()
    return path

def choose_file():
    root = Tk()
    root.withdraw()
    path = tkinter.filedialog.askopenfilename(
        filetypes=IMAGE_FILE_TYPES, 
        initialdir="/images"
    )
    root.destroy()
    return path

