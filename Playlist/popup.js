/*
James Hahn, 2016

I gained inspiration for this project from a problem of mine that I recognized:
I had too many Youtube bookmarks of my favorite songs, yet no way to play them all efficiently;
so, my solution was to make my own playlist so I can enjoy literally hundreds of hours of music (turns out I had ~1860 bookmarks, wow).
This program utilizes JavaScript to create a chrome extension, accessing a user's
bookmarks on the current computer. These bookmarks are then stored, and the Youtube API
is used to create a Youtube playlist from those bookmarks so they can easily be listened
to at any given time (still working on this part). Future features may include access to
the Spotify API in order to sync a user's Spotify songs and their Youtube bookmarks.

Future ideas:
Make a modified playlistName/Allow the user to specify the playlistName
Updated GUI so it looks pretty
Hook up to Spotify API
Increase efficiency
*/

var books = [];
var booksID = [];
var folders = [];
var playlistID;
var channelID;
var playlistName = "Testing"

//Main function to run the program
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

//Create a public playlist.
function createPlaylist() {
    //Create the XHR (XMLHttpRequest) for the call
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
    //Perform the XHR from above
    request.execute(function(response) {
        var result = response.result;
        var details = {
            kind: 'youtube#video',
            videoId: 'OcE8YWdGtnI'
        }
        if(result){
            playlistID = result.id;
            console.log("PLAYLIST ID: " + playlistID);
            //For right now, I am trying to limit asynchronous calls because I found that
            //many XHRs fail and my program becomes wonky.  So, I just decided to make recursive calls.
            //It takes approximately 12 minutes to add 1877 songs to a playlist.
            addToPlaylist(booksID[0], undefined, undefined, 0, 0);
        }
    });
}

//Add a video to a playlist. The "startPos" and "endPos" values let you
//start and stop the video at specific times when the video is played as
//part of the playlist.
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

//Traverses entire list of bookmarks to find all the folders containing music (specified by user)
//and then adds every Youtube bookmark to the books array
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

//Calls the main program into action once the window loads and the user
//clicks the "Get Bookmarks!" button
window.onload = function(){
    window.document.getElementById("bookmarksButton").addEventListener('click', getBookmarks, true);
}
