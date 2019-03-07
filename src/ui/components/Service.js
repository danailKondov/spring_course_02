export function postBook(authors, title, genre) {
    return fetch('/api/books/', {
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
    return fetch('/api/books/')
}

export function deleteById(id) {
    return fetch('/api/books/' + id, {
        method: 'delete'
    })
}

export function updateTitleById(id, title) {
    return fetch('/api/books/?id=' + id + '&title=' + title, {
        method: 'put'
    })
}