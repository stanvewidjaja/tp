---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `LocationListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Location` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a location).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Location` objects (which are contained in a `UniqueLocationList` object).
* stores the currently 'selected' `Location` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Location>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Location` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Location` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th location in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new location. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the location was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the location being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### Substring Matching in Find Command

#### Implementation

The substring matching feature for the `find` command enables users to search for locations by matching any substring of their name, rather than requiring full word matches.

**Design Overview:**

The implementation involves three key components:

1. **StringUtil** - Low-level utility class that handles substring matching
   * Added new method: `containsSubstringIgnoreCase(String sentence, String substring)`
   * Performs case-insensitive substring matching using `String.toLowerCase().contains()`
   * Validates input (null checks, empty string checks)

2. **NameContainsKeywordsPredicate** - Filtering logic at the model layer
   * Located in `seedu.address.model.location` package (works with `Location` objects)
   * Modified `test()` method to use `containsSubstringIgnoreCase()` instead of `containsWordIgnoreCase()`
   * Maintains OR search logic: multiple keywords return matching locations if ANY keyword matches
   * Each keyword can now be a substring

3. **FindCommand** - Command execution layer
   * No changes needed; works seamlessly with the updated predicate
   * Reports filtered results to the UI through the model's filtered location list

**Sequence Flow:**

```
User Input: "find Jo"
    ↓
FindCommandParser → Creates FindCommand with NameContainsKeywordsPredicate(["Jo"])
    ↓
FindCommand.execute() → Calls Model.updateFilteredLocationList(predicate)
    ↓
NameContainsKeywordsPredicate.test(location) → For each location, checks:
    - containsSubstringIgnoreCase("John Restaurant", "Jo") → true ✓
    - containsSubstringIgnoreCase("Jane Cafe", "Jo") → false ✗
    ↓
Result: "John Restaurant" is included in filtered list
```

**Key Changes:**

| Component | Old Behavior | New Behavior |
|-----------|-------------|-------------|
| `StringUtil` | `containsWordIgnoreCase()` only (full word match) | Added `containsSubstringIgnoreCase()` (substring match) |
| `NameContainsKeywordsPredicate` | Uses `containsWordIgnoreCase()` with `Person` | Uses `containsSubstringIgnoreCase()` with `Location` |
| Find Command | `find Hans` ❌ matches partial name | `find Han` ✓ matches `Hans Restaurant` |

#### Design Considerations:

**Aspect: Substring vs. Full Word Matching**

* **Alternative 1 (current choice):** Substring matching (case-insensitive)
  * Pros: More flexible search; users can find locations with partial input (e.g., "Jo" matches "John's Restaurant", "Johan's Cafe", "Joust Arena")
  * Pros: Simple to implement using `String.contains()`
  * Cons: May return more results than user expects (e.g., "e" matches many location names)

* **Alternative 2:** Full word matching only (previous implementation)
  * Pros: More precise results; reduces false positives
  * Cons: Less flexible; requires exact word matches
  * Cons: Inconvenient for users who don't remember exact names

* **Alternative 3:** Regex-based matching
  * Pros: Maximum flexibility for complex patterns
  * Cons: Higher complexity; potential performance overhead
  * Cons: Poor user experience for non-technical users

**Aspect: Search Scope**

* **Current choice:** Search only in location names
  * Pros: Focused search; reduces noise
  * Cons: Cannot search by address, phone, email, tags, etc.
  * Future enhancement: Support for multi-field search (e.g., find by category tags, address, or distance)

#### Testing Strategy:

Comprehensive test coverage includes:

1. **Unit Tests** (`StringUtilTest`):
   - Substring at prefix, middle, suffix positions
   - Case-insensitive matching
   - Empty string and null validation

2. **Component Tests** (`NameContainsKeywordsPredicateTest`):
   - Single and multiple substring keywords
   - OR logic verification
   - Non-matching scenarios

3. **Integration Tests** (`FindCommandTest`, `FindCommandParserTest`):
   - End-to-end find command execution
   - Parser correctly handles substring keywords
   - Multiple locations matching different keywords
   - Test data includes typical locations (e.g., restaurants, attractions, hotels)


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a backpacker or traveller
* has a need to systematically catalogue multiple travel destinations and points of interest
* prefers lightweight desktop tools over visual-heavy travel apps
* can type fast and values high-efficiency, keyboard-driven workflows
* prefers typing to mouse interactions
* is comfortable using CLI applications for structured and searchable itinerary planning

**Value proposition**: It allows for much more efficient searching for destinations and planning routes between points, and a much more accessible and seamless UI for users to list, edit or delete the destinations (by contacts and address) they are interested in for overseas trips, leisure or social visits.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                    | I want to …​                                                                  | So that I can…​                                                             |
|----------|----------------------------|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| `* * *`  | user                       | add a new record with its address and contact details                         | have a central record of where to go next                                   |
| `* * *`  | user                       | delete an existing record                                                     | remove unwanted entries                                                     |
| `* * *`  | user                       | exit the program                                                              | -                                                                           |
| `* * *`  | user                       | save my data                                                                  | revisit the app                                                             |
| `* * *`  | user                       | edit details of a saved place                                                 | correct or update information                                               |
| `* * *`  | experienced user           | use up and down arrows to echo my past commands                               | quickly retry commands that have a minor mistake                            |
| `* * *`  | user                       | save my friends’ addresses                                                    | easier meetups and not have to store them separately from my travels        |
| `* * *`  | student                    | handle my classwork that requires moving to mobile places                     | have a better plan without being confused about where to go next            |
| `* *`    | user                       | add keywords/notes to specific addresses                                      | remember the details easily                                                 |
| `* *`    | experienced user           | set shortcuts for my commands                                                 | use the app more efficiently                                                |
| `* *`    | user                       | mark my destinations with dates                                               | get an overview of the whole day                                            |
| `* *`    | food explorer              | manage restaurant recommendations                                             | easily keep track of their addresses and opening hours                      |
| `* *`    | food explorer              | group restaurant recommendations                                              | sort them out by preference/some other metric                               |
| `* *`    | new user                   | view a help message explaining the keyboard commands                          | quickly learn how to use the app                                            |
| `* *`    | tech-savvy user            | use a single keyboard command to search for all "sightseeing" spots           | find my next destination fast and easily                                    |
| `* *`    | cautious solo traveller    | store emergency contacts (embassy, hospital, local police)                    | access them quickly in urgent situations                                    |
| `* *`    | user                       | change the application's colour (light/dark mode) and contrast                | read the content comfortably                                                |
| `* *`    | user                       | pin important locations                                                       | quickly see my highest-priority entries                                     |
| `* *`    | user with poor eyesight    | change my font size                                                           | see the content clearly                                                     |
| `* *`    | user                       | edit phone numbers or addresses easily                                        | outdated contact details do not mislead me                                  |
| `* *`    | clumsy user                | undo my previous actions                                                      | rectify any accidental commands                                             |
| `* *`    | frequent traveller         | store different time zones                                                    | avoid making calls at inappropriate timings                                 |
| `* *`    | solo traveller             | mark places as favorites                                                      | prioritize places I do not want to miss                                     |
| `* *`    | user                       | archive places I have already visited                                         | keep my active list uncluttered                                             |
| `* *`    | user                       | search for keywords in my contacts/addresses                                  | find one/several particular addresses without manual searching              |
| `* *`    | user                       | tag different places based on groups (restaurants, attractions, hotels)       | filter for the type of location I am looking for                            |
| `* *`    | concerned user             | mark destination information as verified or unverified                        | remember which destinations I have personally confirmed                     |
| `*`      | user                       | group my destinations logically or manually                                   | view related destinations together                                          |
| `*`      | user                       | sort saved places by distance                                                 | optimize my walking route and avoid backtracking                            |
| `*`      | frequent planner/traveller | reliably set repeat trips (e.g. every second Sunday I'm going home)           | maintain regular routines like visiting family or planning frequent outings |
| `*`      | user                       | attach notes to the places I’ve been                                          | remember if I enjoyed the place, or never want to go back                   |
| `*`      | user                       | see my destinations in a schedule view                                        | easily visualize my itinerary                                               |
| `*`      | user                       | attach and record my expenses                                                 | manage my budget                                                            |
| `*`      | parent                     | organize my trip together with different users (family, friends, tour agency) | allow them to contribute ideas/routes for my trip                           |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a location**

**MSS**

1. User inputs the details of a location.
2. User submits the details to the system.
3. AddressMe confirms the addition.
4. AddressMe shows the updated list.
Use case ends.

**Extensions**

* 2a. The given details are invalid.
* 2a1. AddressMe shows an error message, and if it can handle gracefully with incomplete data, will show the details it managed to add.
Use case resumes at step 3.


* 2b. The given location name is invalid.
* 2b1. AddressMe shows an error message and informs the user it is unable to add the entry.
Use case ends.

---

**Use case: Shorten/specify own commands to enhance usage speed**

**MSS**

1. User types a command to open a "shortcut" menu.
2. AddressMe opens the shortcut menu with two input bars.
3. User types a desired shortcut for a desired command (e.g., Command: `list t/restaurant`, Shortcut: `ls r`).
4. User confirms the choice.
5. AddressMe shows all shortcuts before prompting the user to exit the shortcut menu.
6. User either goes back to add another shortcut or exits the menu.
Use case ends.

**Extensions**

* 3a. The command for the shortcut is invalid.
* 3a1. When confirming the choice, AddressMe prompts the user that the command is invalid.
* 3a2. AddressMe lets the user back to the menu with the invalid command.
Use case resumes at step 3.

---

**Use case: Edit a location**

**MSS**

1. User requests to list locations using the list command.
2. AddressMe shows a list of locations.
3. User requests to edit a specific location in the list by providing its index and the new details to be updated.
4. AddressMe updates the location and shows a success message with the updated details.
Use case ends.

**Extensions**

* 2a. The list is empty.
Use case ends.
* 3a. The given index is invalid (out of range or non-numeric).
* 3a1. AddressMe shows an error message: "Invalid index. Please enter a valid location index."
Use case resumes at step 2.


* 3b. The provided email format is invalid.
* 3b1. AddressMe shows an error message regarding the invalid email.
Use case resumes at step 2.


* 3c. The provided phone number format is invalid.
* 3c1. AddressMe shows an error message regarding the invalid phone number.
Use case resumes at step 2.


* 3d. The edited details result in a duplicate entry (it matches an existing entry's name + phone/email).
* 3d1. AddressMe rejects the edit, leaves the original record unchanged, and shows an error message.
Use case resumes at step 2.

---

**Use case: List all locations**

**MSS**

1. User enters the list command.
2. AddressMe shows a list of locations.
Use case ends.

**Extensions**

* 2a. The list is empty.
* 2a1. System informs the user the list is empty.
Use case ends.

---

**Use case: Save and exit**

**MSS**

1. User enters the save/exit command.
2. System saves the data.
3. System closes the application.
Use case ends.

**Extensions**

---

**Use case: Delete one or more locations**

**MSS**

1. User requests to list locations.
2. AddressMe shows a list of locations.
3. User requests to delete one or more locations.
4. AddressMe deletes all specified locations.
Use case ends.

**Extensions**

* 2a. The list of locations is empty.
* 2a1. AddressMe informs the user that there are no locations to delete.
Use case ends.


* 3a. At least one given index is invalid.
* 3a1. AddressMe shows an error message.
* 3a2. AddressMe lists the available locations again.
Use case resumes at step 2.


* 3b. Duplicate indices are provided (e.g., `delete 2 2`).
* 3b1. AddressMe shows an error message.
* 3b2. AddressMe lists the available locations again.
Use case resumes at step 2.


* 5a. An error occurs during deletion.
* 5a1. AddressMe informs the user that the deletion failed.
Use case ends.


* *a. At any time, the user cancels the delete operation.
* *a1. AddressMe aborts the delete operation.
Use case ends.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 locations without a noticeable sluggishness in performance for typical usage.
3. Should display a list of 1000 entries under 0.5 seconds when searching or using the “list” command.
4. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
5. Should work well for standard screen resolutions 1920x1080 and higher of 100% and 125% scales.
6. Should be usable for resolutions 1280x720 and higher and for screen scales 150%.


*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, macOS, and Linux.
* **Location**: A saved place in AddressMe together with its contact and address details.
* **Tag**: A label used to group similar saved locations.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a location

1. Deleting a location while all locations are being shown

   1. Prerequisites: List all locations using the `list` command. Multiple locations in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 1 2`<br>
      Expected: First and second contacts are deleted from the list. Number of deleted locations shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No location is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `delete 1 1`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
