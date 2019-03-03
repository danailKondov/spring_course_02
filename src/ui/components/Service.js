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

export function updateBook(book) {
    return fetch('/api/books/', {
        method: 'put',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify(book)
    })
}

export function getComments(id) {
    return fetch('/api/books/' + id + '/comment')
}