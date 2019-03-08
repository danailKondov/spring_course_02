export function postBook(authors, title, genre) {
    return fetch('/api/books/', {
        method: 'post',
        headers: {'Content-Type':'application/json'},
        credentials: 'include',
        body: JSON.stringify({
            authors,
            title,
            genre
        })
    })
}

export function getAllBooks() {
    return fetch('/api/books/', {
        credentials: 'include'
    })
}

export function deleteById(id) {
    return fetch('/api/books/' + id, {
        method: 'delete',
        credentials: 'include',
    })
}

export function updateTitleById(id, title) {
    return fetch('/api/books/?id=' + id + '&title=' + title, {
        method: 'put',
        credentials: 'include',
    })
}

export function processLogin(username, password) {
    return fetch('/perform_login', {
        method: 'post',
        headers: {
            "Content-type": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        body: 'username=' + username + '&password=' + password
    })
}