import React from 'react'
import BookTable from './BookTable'
import {AddBook} from './Add'
import Modal from 'react-modal';
import './library.css'
import {getAllBooks} from "./Service";
import Login from "./Login";

Modal.setAppElement('#root');

export default class App extends React.Component {

    state = {
        books: [],
        isLoading: false,
        isShowComments: false,
        isAuthenticated: false,
        username: "",
        comments: []
    };

    componentDidMount() {
        this.fetchBooks();
    }

    fetchBooks = () => {
        this.setState({ isLoading: true });
        getAllBooks()
            .then(response => response.json())
            .then(books => this.setState({isLoading: false, books: books}));
    };

    handleAddBook = (book) => {
        const newBooks = [book, ...this.state.books];
        this.setState({books: newBooks});
    };

    handleDeleteBook = (id) => {
        const isNotId = book => book.id !== id;
        const updatedBooks = this.state.books.filter(isNotId);
        this.setState({books: updatedBooks});
    };

    handleCommentView = (comments) => {
        this.setState({comments, isShowComments: true});
    };

    closeCommentView = () => {
        this.setState({isShowComments: false});
    };

    handleLogin = (username) => {
        this.setState({username, isAuthenticated: true});
    };

    render() {
        const { books, isLoading, isShowComments, comments, isAuthenticated, username } = this.state;

        return (
            <React.Fragment>
                <div id="tablediv">
                    <h1>Library</h1>
                    {
                        isLoading ?
                            <p>Загружаю...</p>
                            : null
                    }
                    {
                        Array.isArray(books) ?
                        <BookTable
                            books={books}
                            onDelete={this.handleDeleteBook}
                            onCommentView={this.handleCommentView}
                            isAuthenticated={isAuthenticated}
                        />
                        : null
                    }
                </div>
                <AddBook
                    onAdd={this.handleAddBook}
                />
                <Modal
                    isOpen={isShowComments}
                    shouldCloseOnOverlayClick={true}
                    shouldCloseOnEsc={true}
                    onRequestClose={this.closeCommentView}
                    className="Modal"
                    overlayClassName="Overlay"
                    contentLabel="ModalLabel"
                >
                    <h1>Comments</h1>
                    { comments ?
                        comments.map((comment, i) => (
                            <div className="comment" key={"comment" + i}>
                                <table>
                                    <tr>
                                        <th>Date: </th><th>{comment.date}</th>
                                    </tr>
                                    <tr>
                                        <th>User: </th><th>{comment.user}</th>
                                    </tr>
                                    <tr>
                                        <th>Title: </th><th>{comment.title}</th>
                                    </tr>
                                    <tr>
                                        <th>Text: </th><th>{comment.text}</th>
                                    </tr>
                                </table>
                            </div>
                        ))
                        : <h3>No comments</h3>
                    }
                    <button
                        className='btn btn-default'
                        onClick={this.closeCommentView}
                    >
                        Close
                    </button>
                </Modal>
                {
                    isAuthenticated ?
                        <div className="loginContainer">
                            <p>User: {username}</p>
                        </div>
                        : <Login onLogin={this.handleLogin}/>
                }
            </React.Fragment>
        )
    }
}
