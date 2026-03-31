<p align="center">
  <img src="https://github.com/CFlory-Programming/AdvProgrammingIndividualProject/blob/main/images/CarbonCopyLogo.png?raw=true" alt="Logo" width="300"/>
</p>

# CarbonCopy (CC)

## Intention
- Computer cleanup and organization by finding duplicate files

## Description
- If multiple replica files were accidentally downloaded, for example two text files with the same content, this program will scan whichever directory you choose and show you which files are replicas, which aren't, why they were flagged as duplicates, and the option to delete the duplicates but keep the first downloaded version.

## Feature List
- File byte (content) detection
- File size detection
- File metadata detection (last updated)
- Supports text documents ```(.txt, .doc, .docx, .gdoc)```
- Exportable Logs (.txt report of what was deleted)
- Warning prompts with an overview before deleting any files

## Platforms
- MacOS
- Windows

## Year Two Concepts
1. **I/O and Excpetion Handling**
    - Scan computer path and read file data with Input/Output
    - Exception handling to prevent crashing for folders without permissions
2. **Collections**
    - HashMaps (File's hash or size as a key. File path as the value. If the key already exists, then a duplicate is found)
    - ArrayLists (Display the results on the GUI)
3. **GUI Controls**
    - Buttons to trigger the scan
    - Text box to input file paths
    - List / scroll pane to show results
4. **Sorting and Filtering**
    - Find which duplicate file was created first and not delete it
5. **Recursion**
    - Use recursion to scan the entire chosen directory, including sub-folders, until every file is reached
7. **Ehanced For Loops**
    - Getting all the duplicate files and assigning them to a group variable
    - Displaying all the duplicate files in their respective groups
    - Getting all the files in a directory

## How to Run the Project From an Executable

1. Click the green ```Code``` button at the top
2. Click ```Download ZIP```
3. Unzip the file
4. Navigate to the ```src``` directory in your file explorer
5. Navigate to the folder with the name containing your specific device's operating system and chipset
6. Run the executable
    - For **Windows**, double click ```CarbonCopy.exe``` to **run**
    - For **Mac**, double click ```CarbonCopy.app``` to **run**
    - For **Linux and Raspberry Pi**, double click ```CarbonCopy.sh``` to **run**

## How to Run the Project From the Terminal

1. Click the green ```Code``` button at the top
2. Click ```Download ZIP```
3. Unzip the file
4. Navigate to the project directory in a new terminal
5. Type ```javac -cp lib/core.jar src/*.java``` to **compile**
6. Run the program
    - For **Windows**, type ```java -cp "lib/core.jar;src" Main``` to **run**
    - For **Mac and Linux**, type ```java -cp "lib/core.jar:src" Main``` to **run**

### 📐 UML Diagram
![UML](https://github.com/CFlory-Programming/AdvProgrammingIndividualProject/blob/main/images/UML.png?raw=true)
