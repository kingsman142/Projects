var books = [];
var booksID = [];
var folders = [];
var playlistID;
var channelID;

function getBookmarks(){
    chrome.bookmarks.getTree(function(bookmarks){
        var input = window.document.getElementById("foldersForm").value;
        parseFolders(input);

        for(var j = 0; j < folders.length; j++){
            search_for_title(bookmarks, folders[j], null); //Collect all bookmarks in the "Music" folder and put them into the books array
        }

        console.log("books: " + books.length); //Print out the length of the books array

        gapi.client.setApiKey('AIzaSyDUDozQF2xXJd7nybrEhVYgWUsSA4BREWw');
        gapi.client.load('youtube', 'v3', function(){
            gapi.auth.authorize({
                client_id: "323168009404-b01satic25ad9nun2e2gd68e2j16u5oe.apps.googleusercontent.com",
                immediate: true,
                scope: "https://www.googleapis.com/auth/youtube"
            }, function(){
                createPlaylist();
            });
        });
    });
}

// Create a private playlist.
function createPlaylist() {
    var request = gapi.client.youtube.playlists.insert({
        part: 'snippet',
        resource: {
            snippet: {
                title: 'Test Playlist',
                description: 'A playlist storing your favorite songs! \nSize: ' + books.length,
                channelId: 'UC5LO-02iXbZgobVMzETTasg'
            }
        }
    });
    request.execute(function(response) {
        var result = response.result;
        if (result) {
            playlistID = result.id;
            for(var k = 0; k < booksID.length; k++){
                addToPlaylist(booksID[k], undefined, undefined);
            }
            console.log("PLAYLIST ID: " + playlistID);
        }
    });
}

// Add a video to a playlist. The "startPos" and "endPos" values let you
// start and stop the video at specific times when the video is played as
// part of the playlist.
function addToPlaylist(id, startPos, endPos) {
    console.log("adding id: " + id);
    var details = {
        videoId: id,
        kind: 'youtube#video'
    }
    if (startPos != undefined) {
        details['startAt'] = startPos;
    }
    if (endPos != undefined) {
        details['endAt'] = endPos;
    }
    var request = gapi.client.youtube.playlistItems.insert({
        part: 'snippet',
            snippet: {
                playlistId: playlistID,
                resourceId: {
                    kind: 'youtube#video',
                    videoId: id
                }
            }
    });
}

function search_for_title(bookmarks, title, parent){
    if(parent == null){ //First find the parent folder
        for(var i = 0; i < bookmarks.length; i++){ //Loop through all bookmarks
            if(bookmarks[i].title == title){ //If the bookmark title matches the title of the folder we're looking for ("Music"), proceed
                search_for_title(bookmarks[i].children, null, bookmarks[i].id); //Loop through all the bookmarks in the folder that we found
                return null;
            } else{
                if(bookmarks[i].children){ //If the bookmark is a folder, it has children
                    search_for_title(bookmarks[i].children, title, parent);
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

        return null;
    }
}

function findVideoID(url){
    var startSearch = false;
    var videoID = "";
    for(var i = 0; i < url.length; i++){
        if(startSearch) videoID += url[i];

        if(url[i] == '=') startSearch = true;
    }
    return videoID;
}

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

function parseFolders(names){
    var currName = "";
    var size = 0;

    for(var i = 0; i < names.length; i++){
        if(names[i] == ","){
            folders[size++] = currName;
            currName = "";
        } else{
            currName += names[i];
        }
    }

    folders[size] = currName;
}

window.onload = function(){
    window.document.getElementById("bookmarksButton").addEventListener('click', getBookmarks, true);
}
