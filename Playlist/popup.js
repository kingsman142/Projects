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

        var request = new XMLHTTPRequest();
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
