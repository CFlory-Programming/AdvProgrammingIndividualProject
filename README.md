# File Duplicate Detector

## Intention
- Computer cleanup and organization (MacOS and Windows)

##
If multiple of the same files were accidentally downloaded with different names, for example two identical word documents or photos on your computer, this program will scan whichever path you choose and show you which files are duplicates, which aren't, why they were flagged as duplicates, and the options to delete duplicates.

## Year Two Concepts
1. I/O and Excpetion Handling
    - Scan computer path and read file data with Input/Output
    - Exception handling to prevent crashing for folders without permissions
2. Collections
    - HashMaps (File's hash or size as a key. File path as the value. If key == doesExist, then a duplicate is found)
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
6. Inheritance or Packages
    - Specific sub-classes or seperate packages to handle the different file paths, system priviledges, and file extensions
