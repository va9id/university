# Command Line Image Editor
Apply up to 9 filters to an image of choice. Filters modify image pixels, returning a copy of the filtered image which can be saved.

## Prerequisites 
- [Python3+](https://www.python.org/downloads/)
- pip 20.0.2 or later
	- Unix installation:
		```powershell
		python -m ensurepip --upgrade
		```	
	- Windows installation: 
		```powershell
		py -m ensurepip --upgrade
		```
- [Pillow](https://pypi.org/project/Pillow/)
	```powershell
	python3 -m pip install --upgrade pip
	python3 -m pip install --upgrade Pillow
	```
## Usage
- Add desired images to `images` directory
- Execute the main program with the following command: 
```bash
python3 main.py
```
- You will be shown a prompt of all the program's commands: 
	```
	----------------------------    
	L)oad image	S)ave-as	Q)uit    
	2)-tone	3)-tone	X)treme contrast	T)int sepia	P)osterize    
	E)dge detect	I)mproved edge detect	V)ertical flip	H)orizontal flip    
	----------------------------
	```
- Begin by loading an image with the **L**oad command. The image you choose will be previewed to you. **Close the preview to continue**
![Image](https://github.com/vahido9/cli-image-editor/blob/main/images/dog1.jpg "Loaded Image"). 
- Apply desired filter(s) to the image image: 
- The filtered image then pops up:   
![Filtered Image](https://github.com/vahido9/cli-image-editor/blob/main/images/extremeContractDog1.jpg "Filtered Image")
- You can continue applying more filters or save your filtered image in your destination of choice.

### Alternative - Bash script
- Batch script is under `/batch/batch_script.txt` andcan be modified: 
	- The syntax of the script is shown in the following table and example: 

Image name | Name to Save Image As | Filter code(s)
-----------|-----------------------|--------------
dog1.jpg   | dog1WithFilters       | X T 3 E

- Filter codes are as follows: **2**-tone, **3**-tone, **X**treme contrast, **T**int sepia, **P**osterize, **E**dge detect, **I**mproved edge detect, **V**ertical flip, **H**orizontal flip.
