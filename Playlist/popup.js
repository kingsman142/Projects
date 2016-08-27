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

function fetchRandomSong(){
    var rand = Math.floor(Math.random() * booksID.length);
    return booksID[rand];
}

//Main function to run the program
function getBookmarks(){
    chrome.bookmarks.getTree(function(bookmarks){
        var input = window.document.getElementById("foldersForm").value;
        parseFolders(input);

        chrome.windows.create({
            url: "https://www.youtube.com/watch?v=" + fetchRandomSong(),
            type: 'popup',
            width: 465,
            height: 475,
        }, function(window){
            chrome.tabs.query({
                windowId: window.id
            }, function(tabs){
                newURL = "https://www.youtube.com/watch?v=" + fetchRandomSong();
                chrome.tabs.update(tabs[0].id, {
                    url: newURL
                }, function(){
                    var bgPage = chrome.extension.getBackgroundPage();
                    bgPage.startPlaylist(booksID, tabs);
                });
            });
        });

        for(var j = 0; j < folders.length; j++){
            searchForTitle(bookmarks, folders[j], null); //Collect all bookmarks in the "Music" folder and put them into the books array
        }

        console.log("===Total # of bookmarks: " + books.length + "===");
    });
}

//Traverses entire list of bookmarks to find all the folders containing music (specified by user)
//and then adds every Youtube bookmark to the books array
function searchForTitle(bookmarks, title, parent){
    if(parent == null){ //First find the parent folder
        for(var i = 0; i < bookmarks.length; i++){ //Loop through all bookmarks
            if(bookmarks[i].title == title){ //If the bookmark title matches the title of the folder we're looking for ("Music"), proceed
                searchForTitle(bookmarks[i].children, null, bookmarks[i].id); //Loop through all the bookmarks in the folder that we found
                return null;
            } else{
                if(bookmarks[i].children){ //If the bookmark is a folder, it has children
                    searchForTitle(bookmarks[i].children, title, parent);
                }
            }
        }
    } else if(title == null){ //Parent folder is found, now just traverse the bookmarks within
        var filled = books.length;

        for(var i = 0; i < bookmarks.length; i++){
            if(findWord("youtube.com", bookmarks[i].url)){
                books[filled] = bookmarks[i].title; //Assign all the bookmarks into the books array
                booksID[filled++] = findVideoID(bookmarks[i].url); //Find the video ID of the video and add it to the bookmarks ID array
            }
        }

        console.log("-----Folder: " + parent + " contains " + bookmarks.length + " songs-----");
        return null;
    }
}

//Takes a Youtube url and returns the video ID.
function findVideoID(url){
    var startSearch = false;
    var videoID = "";
    for(var i = 1; i < url.length; i++){
        if(startSearch) videoID += url[i];

        if(url[i] == '=' && url[i-1] == 'v') startSearch = true;
    }
    return videoID;
}

//Main purpose is to take a url and try to find
//the word "youtube" in it to make sure it's a youtube video
function findWord(word, url){
    var matches = 0;

    if(word == undefined) return false;

    for(var i = 0; i < url.length-word.length; i++){
        if(url[i] == word[matches]){
            matches++;
        } else{
            i = i - matches;
            matches = 0;
        }

        if(matches == word.length) return true;
    }

    return false;
}

//Take the input of the folders from the HTML form
//and parse every folder name, which is separated
//by a comma.
function parseFolders(names){
    folders = names.split(",");
    console.log(folders);
}

//Calls the main program into action once the window loads and the user
//clicks the "Make playlist!" button
window.onload = function(){
    window.document.getElementById("bookmarksButton").addEventListener('click', getBookmarks, true);
}
