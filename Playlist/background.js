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
*/

var books = [];
var booksID = [];
var folders = [];

var current = 0;
var end = 0;

function recursePlaylistExec(tabs){
    chrome.tabs.executeScript(tabs[0].id, {
        code: "var current = document.getElementsByClassName('ytp-progress-bar')[0].getAttribute('aria-valuenow'); var end = document.getElementsByClassName('ytp-progress-bar')[0].getAttribute('aria-valuemax'); [current,end]"
    },  function(results){
            try{
                current = results[0][0];
                end = results[0][1];
                console.log("current: " + current + "     ;     finish: " + end);
                if(current == end && end != 0){
                    current = 0;
                    end = 0;
                    newURL = "https://www.youtube.com/watch?v=" + fetchRandomSong();
                    console.log("STARTING NEW SONG: " + current + " and " + end + "     ;     new song: " + newURL);
                    chrome.tabs.update(tabs[0].id, {
                        url: newURL
                    }, function(){
                        setTimeout(function(){ current = 0; end = 0; recursePlaylistExec(tabs); }, 1000);
                    });
                } else{
                    setTimeout(function(){ console.log("Recursing"); recursePlaylistExec(tabs); }, 1000);
                }
            } catch(e){
                console.log(e);
                current = 0;
                end = 0;
                newURL = "https://www.youtube.com/watch?v=" + fetchRandomSong();
                console.log("STARTING NEW SONG AFTER ERROR: " + current + " and " + end + "     ;     new song: " + newURL);
                chrome.tabs.update(tabs[0].id, {
                    url: newURL
                }, function(){
                    setTimeout(function(){ current = 0; end = 0; recursePlaylistExec(tabs); }, 1000);
                });
            }
        }
    );
}

function fetchRandomSong(){
    var rand = Math.floor(Math.random() * booksID.length);
    return booksID[rand];
}

//Main function to run the program
function startPlaylist(bookmarksId, tabs){
    booksID = bookmarksId;
    recursePlaylistExec(tabs);
}
