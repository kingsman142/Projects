/*
James Hahn, 2016

I gained inspiration for this project from a problem of mine that I recognized:
I had too many Youtube bookmarks of my favorite songs, yet no way to play them all efficiently;
so, my solution was to make my own playlist so I can enjoy literally hundreds of hours of music (turns out I had ~1860 bookmarks, wow).
This program utilizes JavaScript to create a chrome extension, accessing a user's
bookmarks on the current computer. These bookmarks are then stored, and Google Chrome API
is used to execute scripts on a new chrome tab, which loops through the bookmarks.

Future ideas:
Hook up to Spotify API
Machine. Learning. ;)
*/

var books = [];
var booksID = [];
var folders = [];
var removeScrollbars = true;
var availableSongs = [];
var bannedSongs = [];

var current = 0;
var end = 0;

function recursePlaylistExec(tabs){
    if(removeScrollbars){
        chrome.tabs.executeScript(tabs[0].id, {
            code: "document.getElementsByTagName('html')[0].style.overflow = 'hidden';"
        }, function(){
            removeScrollbars = false;
        });
    }

    chrome.tabs.executeScript(tabs[0].id, {
        code: "var current = document.getElementsByClassName('ytp-progress-bar')[0].getAttribute('aria-valuenow'); var end = document.getElementsByClassName('ytp-progress-bar')[0].getAttribute('aria-valuemax'); [current,end]"
    },  function(results){
            try{
                current = results[0][0];
                end = results[0][1];
                if(current == end && end != 0){ // marks the end of the song
                    current = 0;
                    end = 0;
                    newURL = "https://www.youtube.com/watch?v=" + fetchRandomSong();
                    chrome.tabs.update(tabs[0].id, {
                        url: newURL
                    }, function(){
                        setTimeout(function(){ // navigate to the new song
                            current = 0;
                            end = 0;
                            removeScrollbars = true;
                            recursePlaylistExec(tabs);
                        }, 1000); // wait at least 1000 sounds before executing this code because we don't want to just refresh instantly
                    });
                } else{
                    setTimeout(function(){
                        recursePlaylistExec(tabs);
                    }, 1000);
                }
            } catch(e){
                current = 0;
                end = 0;
                newURL = "https://www.youtube.com/watch?v=" + fetchRandomSong();
                chrome.tabs.update(tabs[0].id, {
                    url: newURL
                }, function(){
                    setTimeout(
                        function(){
                            current = 0;
                            end = 0;
                            removeScrollbars = true;
                            recursePlaylistExec(tabs);
                        },
                    1000);
                });
            }
        }
    );
}

function fetchRandomSong(){
    var rand = Math.floor(Math.random() * availableSongs.length);
    var newSongIndex = availableSongs[rand];
    availableSongs.splice(rand, 1);
    console.log("Chose song at index: " + newSongIndex + ", ID: " + booksID[newSongIndex] + ", available songs: " + availableSongs.length);
    if(availableSongs.length <= 2){
        populateAvailableSongs();
        console.log("Resetting list of songs!");
    }
    return booksID[newSongIndex];
}

function removeBannedSongs(){
    const setDifference = (a, b) => new Set([...a].filter(x => !b.has(x)));
    var goodSongs = setDifference(booksID, bannedSongs); // remove the banned songs from the available songs
    booksID = Array.from(goodSongs);
}

function populateAvailableSongs(){
    removeBannedSongs();
    for(var i = 0; i < booksID.length; i++){
        availableSongs.push(i);
    }
}

// Main function to run the program
function startPlaylist(bookmarksId, tabs){
    booksID = bookmarksId;
    recursePlaylistExec(tabs);
}
