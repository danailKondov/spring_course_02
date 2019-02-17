export function postBook(authors, title, genre) {
    fetch('/add', {
        method: 'post',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({
            authors,
            title,
            genre
        })
    })
}

export function getAllBooks() {
    return fetch('/all')
}

export function deleteById(id) {
    return fetch('/delete?id=' + id, {
        method: 'delete'
    })
}

export function updateTitleById(id, title) {
    return fetch('/update?id=' + id + '&title=' + title, {
        method: 'put'
    })
}