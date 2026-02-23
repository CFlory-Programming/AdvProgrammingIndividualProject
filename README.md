# CarbonCopy (CC)

## Intention
- Computer cleanup and organization by finding replica files

## Description
- If multiple replica files were accidentally downloaded with different names, for example two word documents with the same content, this program will scan whichever directory you choose and show you which files are replicas, which aren't, why they were flagged as duplicates, and the options to delete duplicates.

## Installation Instructions
1. Click on CarbonCopy.exe
2. Download CarbonCopy.exe
3. Open with admin privileges

## Feature List
- File hash detection
- File content detection
- File metadata detection (Date created, last updated)
- Supports images, videos, word documents, google documents, and more!
- Exportable Logs (.txt report of what was deleted)
- Warning prompts with an overview before deleting any files

## Platforms
- MacOS
- Windows

## Year Two Concepts
1. I/O and Excpetion Handling
    - Scan computer path and read file data with Input/Output
    - Exception handling to prevent crashing for folders without permissions
2. Collections
    - HashMaps (File's hash or size as a key. File path as the value. If the key already exists, then a duplicate is found)
    - Lists (Display the results on the GUI)
3. GUI Controls
    - Buttons to trigger the scan
    - Text box to input file paths
    - List / scroll pane to show results
4. Sorting and Filtering
    - Sort duplicates by size or date
    - Filter duplicates by file type (.jpg , .docx)
5. Recursion
    - Use recursion to scan the entire chosen directory, including sub-folders, until every file is reached
  
### 📐 UML Diagram
![UML](https://github.com/CFlory-Programming/AdvProgrammingIndividualProject/blob/main/resources/UML.png?raw=true)
6. Inheritance or Packages
    - Specific sub-classes or seperate packages to handle the different file paths, system priviledges, and file extensions
