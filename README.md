# fhnw-five-card-stud

A little known game

## Getting Started

+ Add players to game
+ Add up to 10 players (logical maximum)
+ Give users a username or generate one
+ Remove players from game
+ Select which player to remove
+ Figure out what type each hand is
+ Show who the winner is of each round
+ Resize window depending on player count
+ Nice design: background, drop shadow for each card, more space between each card, added icon to all windows, scrollbar
+ Show each player what hand type they have, including: HighCard, OnePair, TwoPair, Three-of-a-kind, Straight (Low and High Straight), Flush, Full House, Four-of-a-kind and Straight flush
+ Tie-break handling for HighCard, OnePair and TwoPair - for the rest, show all winning players that have the same hand type

### Installing

Use Intellij to run code
Or download the jar and run it in the console with one command

## Usage

```powershell
java -jar FiveCardStud.jar
```

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
+ Added a sorting mechanism for "isStraight" evaluation (including low and high straight)
+ Define max size a window can be resized according to player count
+ Fix "evaluateWinner" function not always working
+ Added documentation

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
+ Evaluated the winner(s) and showed the winner(s)
+ Handled tie-breaks w/ a lot of redundant code :(

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/D3strukt0r/fhnw-chat/tags). 

## Authors

* **Manuele Vaccari** - *Initial work* - [D3strukt0r](https://github.com/D3strukt0r)
* **Sasa Trajkova** - *Logic* - [sasatrajkova](https://github.com/sasatrajkova)

See also the list of [contributors](https://github.com/D3strukt0r/fhnw-chat/contributors) who participated in this project.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.txt](LICENSE.txt) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
