var books = [];
var folders = [];

function getBookmarks(){
    chrome.bookmarks.getTree(function(bookmarks){
        var input = window.document.getElementById("foldersForm").value;
        parseFolders(input);

        for(var j = 0; j < folders.length; j++){
            search_for_title(bookmarks, folders[j], null); //Collect all bookmarks in the "Music" folder and put them into the books array
        }

        console.log("books: " + books.length); //Print out the length of the books array
        for(var i = 0; i < books.length; i++){ //Once all bookmarks are added to the books array, loop through all of them
            //console.log("ITEM: " + books[i]);
        }

        gapi.client.setApiKey('AIzaSyDUDozQF2xXJd7nybrEhVYgWUsSA4BREWw');
        gapi.client.load('youtube', 'v3', function(){console.log("LOADED")});

        var xhr = new XMLHttpRequest();

        /*// Define some variables used to remember state.
        var playlistId, channelId;

        // After the API loads, call a function to enable the playlist creation form.
        function handleAPILoaded() {
          enableForm();
        }

        // Enable the form for creating a playlist.
        function enableForm() {
            $('#playlist-button').attr('disabled', false);*/


        // Create a private playlist.
        function createPlaylist() {
            var request = gapi.client.youtube.playlists.insert({
                part: 'snippet,status',
                resource: {
                  snippet: {
                    title: 'Test Playlist',
                    description: 'A private playlist created with the YouTube API'
                  },
                  status: {
                    privacyStatus: 'private'
                  }
                }
            });
            request.execute(function(response) {
                var result = response.result;
                if (result) {
                  playlistId = result.id;
                  $('#playlist-id').val(playlistId);
                  $('#playlist-title').html(result.snippet.title);
                  $('#playlist-description').html(result.snippet.description);
                } else {
                  $('#status').html('Could not create playlist');
                }
            });
        }

        /*// Add a video ID specified in the form to the playlist.
        function addVideoToPlaylist() {
            addToPlaylist($('#video-id').val());
        }

        // Add a video to a playlist. The "startPos" and "endPos" values let you
        // start and stop the video at specific times when the video is played as
        // part of the playlist. However, these values are not set in this example.
        function addToPlaylist(id, startPos, endPos) {
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
                resource: {
                  snippet: {
                    playlistId: playlistId,
                    resourceId: details
                  }
                }
            });
            request.execute(function(response) {
                $('#status').html('<pre>' + JSON.stringify(response.result) + '</pre>');
            });
        }*/
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
            if(findWord("youtube.com", bookmarks[i].url)) books[filled++] = bookmarks[i].title; //Assign all the bookmarks into the books array
        }

        return null;
    }
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
