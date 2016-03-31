var books = [];
var booksID = [];
var folders = [];
var playlistID;
var channelID;
var playlistName = "Testing"

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
                scope: "https://www.googleapis.com/auth/youtube.force-ssl"
            }, function(){
                createPlaylist();
            });
        });
    });
}

// Create a public playlist.
function createPlaylist() {
    var request = gapi.client.youtube.playlists.insert({
        part: 'snippet,status',
        resource:{
            snippet:{
                title: playlistName,
                description: 'A playlist storing your favorite songs! \nSize: ' + booksID.length,
            },
            status:{
                privacyStatus: 'public'
            }
        }
    });
    request.execute(function(response) {
        var result = response.result;
        var details = {
            kind: 'youtube#video',
            videoId: 'OcE8YWdGtnI'
        }
        if(result){
            playlistID = result.id;
            console.log("PLAYLIST ID: " + playlistID);
            //for(var k = 0; k < booksID.length; k++){
            addToPlaylist(booksID[0], undefined, undefined, 0, 0);
        }
    });
}

// Add a video to a playlist. The "startPos" and "endPos" values let you
// start and stop the video at specific times when the video is played as
// part of the playlist.
function addToPlaylist(id, startPos, endPos, k, failures) {
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
    var keepGoing = false;
    var request = undefined;
    do{
        request = gapi.client.youtube.playlistItems.insert({
            part: 'snippet',
            resource:{
                snippet:{
                    playlistId: playlistID,
                    resourceId:{
                        kind: 'youtube#video',
                        videoId: id
                    }
                }
            }
        });
        if(request != undefined){
            request.execute(function(response){
                //console.log("object: " + response['kind'] + ", adding id: " + id + " to playlist " + playlistID);
                //console.log(response);
                //console.log(response.code);
                if(response.code == 404 || response.code == 403) failures++;
                var successRate = ((k-failures)/k)*100.0;
                var failureRate = (failures/k)*100.0;
                console.log(k + " / " + booksID.length + " completed: " + (k-failures) + "/" + k + " successes (" + successRate + ") and " + failures + "/" + k + " failures (" + failureRate + ")");
                if(k < booksID.length-1) addToPlaylist(booksID[k+1], undefined, undefined, k+1, failures);
            });
        }
    } while(keepGoing);
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
    for(var i = 1; i < url.length; i++){
        if(startSearch) videoID += url[i];

        if(url[i] == '=' && url[i-1] == 'v') startSearch = true;
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
