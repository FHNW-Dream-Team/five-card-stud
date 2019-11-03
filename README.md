# Five Card Stud

A little known game

## Features

+ Add players to game
+ Add up to 10 players (logical maximum)
+ Give users a username or generate one
+ Remove players from game
+ Select which to remove
+ Figure out what type each hand is
+ Show who the winner is of each round
+ Resize window depending on player count
+ Nice design: background, drop shadow for each card, more space between each card, added icon to all windows
+ Tie-break handling for HighCard, OnePair and TwoPair - for the rest, show all winning players that have the same hand type

## Installation

Use Intellij to run code

## Usage

```python
java -jar "Whatever.jar"
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Changes

**by Manuele Vaccari**
+ Initialized Project and prepared all files
+ Added function to add player to lobby
+ Let the first two players also choose a username
+ Added function to remove players from the lobby
+ Add an icon to all windows
+ Added shadow to cards
+ Close program when user cancels "Adding Player" dialog after opening the game
+ Restyled player pane
+ Made sure players would be added by using the free space "below" (using FlowPane)
+ Added a sorting mechanism for isStraight evaluation
+ Define max size a window can be resized according to player count

**by Sasa Trajkova**
+ Added shadow color
+ Added documentation
+ Added possibility to add players with generated names
+ Added hands type evaluation and tests
    + isThreeOfAKind
    + isStraight
    + isFlush
    + isFullHouse
    + isFourOfAKind
    + isStraightFlush
+ Simplified the sorting method using Comparator
+ Evaluated the winner and showed the winner(s)
+ Handled tie-breaks w/ a lot of redundant code :)

## License
[MIT](LICENSE.txt)
