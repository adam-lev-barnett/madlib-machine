# Madlib Machine
Madlib Machine is a CLI-based program that ingests any .txt file, turns it into a madlib, and fills in the blanks with user input. 
## What is a Madlib?
Madlibs are stories in which some words are replaced with a blank and the removed word's part of speech. The madlibber is prompted to come up with replacement words that match the parts of speech of each removed word. The concept comes from activity books by Leonard Stern and Roger Price, first published in 1958.

### Example 
**Original text**: "Greetings, person. I ran to the gym today and chewed some gum. Do you want a baloney sandwich?"  
**Blanked text**: "[pluralNoun], [noun]. I [verbPast] to the [noun] [noun] and [verbPast] some [noun]. Do you [verb] a [noun] [noun]"  
**Filled text**: "Potatoes, cowboy. I tested to the space moon and folded some napkins. Do you carry a banana cabbage?"

### Program / Madlib terminology
- **Madlibifiable word**: any word with a part of speech the program is designed to detect and potentially blank (nouns, adjectives, verbs, adverbs, etc.)
- **Blanked**: a word that is removed from the original text and replaced with a text box representing it's part of speech (ie., "[noun]")
- **Fill-in**: replace the blanked words (which are now part of speech blocks) with replacement words
- **Skipper**: a user-defined number that tells the program to skip every *n* madlibifiable words before blanking the next one.

## Features
- Creates a blanked madlib file from any uploaded .txt file, blanking every *skipper*-th word
- Prompts user to replace removed words by providing each removed word's part of speech and accepting user's response as the replacement word
- Produces filled-in madlib as .txt file
- Collections in Madlib.java and MadlibBlanker.java to determine blankable parts of speech (like gerunds) and words that should never be blanked (verbs like "is")
- Automated jUnit testing for madlib blanking, filling, and file creation/validation (jUnit generates and then destroys temporary files to store generated madlibs and comparison text)

## Screenshots and examples (full text excerpts available in example folder)
**Note**: blanked and filled madlibs do not include line breaks right now. I intend to fix this in future releases.
<br>
<img width="675" height="349" alt="Screenshot 2025-10-27 at 6 30 10 PM" src="https://github.com/user-attachments/assets/5e91ea49-149a-47ec-bdc3-46909e46ff32" />
<img width="807" height="390" alt="Screenshot 2025-10-27 at 6 45 12 PM" src="https://github.com/user-attachments/assets/3ffc2486-42a1-417e-8bcd-273d1957936f" />
<br>

### Original text excerpt from Mary Shelley's *Frankenstein*
As I said this I suddenly beheld the figure of a man, at some distance, advancing towards me with superhuman speed. He bounded over the crevices in the ice, among which I had walked with caution; his stature, also, as he approached, seemed to exceed that of a man. I was troubled; a mist came over my eyes, and I felt a faintness seize me; but I was quickly restored by the cold gale of the mountains. I perceived, as the shape came nearer (sight tremendous and abhorred!) that it was the wretch whom I had created. I trembled with rage and horror, resolving to wait his approach and then close with him in mortal combat. He approached; his countenance bespoke bitter anguish, combined with disdain and malignity, while its unearthly ugliness rendered it almost too horrible for human eyes. But I scarcely observed this; rage and hatred had at first deprived me of utterance, and I recovered only to overwhelm him with words expressive of furious detestation and contempt.
<br>
### Blanked text excerpt
As I said this I suddenly beheld the [noun] of a man, at some distance, advancing towards me with superhuman [noun]. He bounded over the crevices in the ice, among which I had walked with [noun]; his stature, also, as he approached, [verbPast] to exceed that of a man. I was troubled; a mist [verbPast] over my eyes, and I felt a faintness [verb] me; but I was quickly restored by the cold gale of the [pluralNoun]. I perceived, as the shape came nearer( [noun] tremendous and abhorred!) that it was the wretch whom I had created. I [verbPast] with rage and horror, resolving to wait his [noun] and then close with him in mortal [noun]. He approached; his countenance bespoke [adjective] anguish, combined with disdain and malignity, while its [adjective] ugliness rendered it almost [adverb] horrible for human eyes. But I [adverb] observed this; rage and hatred had at [adjective] deprived me of utterance, and I recovered only to [verb] him with words expressive of furious [noun] and contempt.
<br>
### Filled in text excerpt
As I said this I suddenly beheld the **cheese curd** of a man, at some distance, advancing towards me with superhuman **penguin**. He bounded over the crevices in the ice, among which I had walked with **whoopie cushion**; his stature, also, as he approached, **boogied** to exceed that of a man. I was troubled; a mist **wiggled** over my eyes, and I felt a faintness **sneeze me**; but I was quickly restored by the cold gale of the **toes**. I perceived, as the shape came nearer (**top hat** tremendous and abhorred!) that it was the wretch whom I had created. I **exploded** with rage and horror, resolving to wait his **lobster** and then close with him in mortal **taco**. He approached; his countenance bespoke **fuzzy** anguish, combined with disdain and malignity, while its **stinky** ugliness rendered it almost **lovingly** horrible for human eyes. But I **stupidly** observed this; rage and hatred had at **bumpy** deprived me of utterance, and I recovered only to **squeak** him with words expressive of furious **mop bucket** and contempt. 
<br><br>

## Requirements
- Java 17+
- Maven 3.6.0+
- [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/) (automatically included via Maven)

## Installation
```bash
# Clone the repository
git clone https://github.com/adam-lev-barnett/madlib-machine.git

# Navigate to the project directory
cd madlib-machine

# Build the project (compiles code and packages jar file)
mvn clean package

# Run the program from target directory
java -jar target/madlib-machine-MadlibMachine-1.0.jar
```

## Project structure
```bash
src/  
├── Madlib.java # Orchestrates madlib generation, filling, and String storage
├── MadlibBlanker.java # Creates blanked madlib from source text
├── MadlibFiller.java # Fills in blanked madlib with user-input replacement words
├── TextAnnotater.java # Applies part-of-speech tagging / annotations via CoreNLP
├── TextAnnotationProperties.java # Manages annotation rules and settings via CoreNLP
├── TextFileLoader.java # Loads text files from disk and converts contents to Strings
└── CLI.java # Command-line interface for user interaction  

tests/
├── MadlibBlankerTest.java
└── MadlibFillerTest.java
```
### API documentation
[View the Javadoc](https://adam-lev-barnett.github.io/madlib-machine/)

### Updating madlibification rules
- To update the parts of speech that are parsed and possibly blanked, uncomment/comment the posMap in Madlib.java
- To update the words that should never be blanked, regardless of part of speech, uncomment/comment the wordsToSkip map in MadlibBlanker.java

## Testing
To run all jUnit tests:
```bash
mvn test
```
### Tests include
- **MadlibBlankerTest**: validates correct madlib blanking and file generation
- **MadlibFillerTest**: verifies correct word substitution and file generation

## Future improvements
- Maintaining line breaks during madlibification
- Upload already-blanked madlib and skip the initial blanking stage
- Store Madlib object, containing original, blanked, and filled-in texts as json

## License
This project is licensed under the MIT License
