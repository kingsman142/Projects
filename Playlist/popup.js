var books = [];

function getBookmarks(){
    console.log("Bookmarks!");

    books = function(){chrome.bookmarks.getTree(function(bookmarks){
        search_for_title(bookmarks, "Music", null, books);
    })}

    books();

    for(var i = 0; i < books.length; i++){
        console.log("ITEM: " + books[i]);
    }
    console.log("books: " + books.length);
}

function search_for_title(bookmarks, title, parent){
    if(parent == null){ //First find the parent folder
        for(var i = 0; i < bookmarks.length; i++){
            if(bookmarks[i].title == title){
                //console.log("MAIN --- BOOKSARR: " + booksArr.length + " , books length: " + books.length);
                console.log("MAIN --- books length: " + books.length);
                search_for_title(bookmarks[i].children, null, bookmarks[i].id);
                //console.log("BOOKS LENGTH: " + booksArr.length + " , books length: " + books.length);
                console.log("books length: " + books.length);
                //return booksArr;
            } else{
                if(bookmarks[i].children){
                    //console.log("CHILDREN --- BOOKSARR: " + booksArr.length + " , books length: " + books.length);
                    console.log("CHILDREN --- books length: " + books.length);
                    //booksArr = [];
                    search_for_title(bookmarks[i].children, title, parent);
                    //this.books = [];
                    //if(booksArr) return booksArr;
                }
            }
        }
    } else if(title == null){ //Parent folder is found, now just traverse the bookmarks within
        for(var i = 0; i < bookmarks.length; i++){
            console.log("booksArr[" + i + "] = " + bookmarks[i].title + " , books length: " + books.length);
            books[i] = bookmarks[i].title;
        }
        //return booksArr;
    }

    //return false;
}

getBookmarks();

console.log("FINAL BOOKS: " + books.length);

/*var arr = [];

function changArr(){
    loopArr();

    for(var i = 0; i < arr.length; i++){
        console.log("ITEM: " + arr[i]);
    }
    console.log("arr: " + arr.length);
}

function loopArr(){
    arr[1] = 999;
}

changArr();*/
