var books = [];

function search_for_title(bookmarks, title, parent){
    if(parent == null){ //First find the parent folder
        for(var i = 0; i < bookmarks.length; i++){
            if(bookmarks[i].title == title){
                search_for_title(bookmarks[i].children, null, bookmarks[i].id);
                console.log("BOOKS LENGTH: " + books.length);
            } else{
                if(bookmarks[i].children){
                    var id = search_for_title(bookmarks[i].children, title);
                    if(id) return id;
                }
            }
        }
    } else if(title == null){ //Parent folder is found, now just traverse the bookmarks within
        for(var i = 0; i < bookmarks.length; i++){
            books[i] = bookmarks[i].title;
            console.log("books[" + i + "]: " + books[i]);
        }
    }

    return false;
}

function getBookmarks(){
    console.log("Bookmarks!");

    chrome.bookmarks.getTree(function(bookmarks){
        search_for_title(bookmarks, "Music", null);
    });

    console.log("books: " + books.length);
    for(var i = 0; i < books.length; i++){
        console.log("ITEM: " + books[i]);
    }
}

getBookmarks();
