var books = new Array();

function search_for_title(bookmarks, title, parent, booksArr){
    if(parent == null){ //First find the parent folder
        for(var i = 0; i < bookmarks.length; i++){
            if(bookmarks[i].title == title){
                search_for_title(bookmarks[i].children, null, bookmarks[i].id, booksArr);
                console.log("BOOKS LENGTH: " + booksArr.length);
                return booksArr;
            } else{
                if(bookmarks[i].children){
                    var id = search_for_title(bookmarks[i].children, title);
                    if(id) return id;
                }
            }
        }
    } else if(title == null){ //Parent folder is found, now just traverse the bookmarks within
        for(var i = 0; i < bookmarks.length; i++){
            booksArr[i].push(bookmarks[i].title);
            //console.log("books[" + i + "]: " + this.books[i]);
        }
        return booksArr;
    }

    return false;
}

function getBookmarks(){
    console.log("Bookmarks!");

    chrome.bookmarks.getTree(function(bookmarks){
        search_for_title(bookmarks, "Music", null, books);
    });

    for(var i = 0; i < this.books.length; i++){
        console.log("ITEM: " + this.books[i]);
    }
    console.log("books: " + this.books.length);
}

getBookmarks();
