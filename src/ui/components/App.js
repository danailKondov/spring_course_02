import React from 'react'
import BookTable from './BookTable'
import {AddBook} from './Add'
import Modal from 'react-modal';
import './library.css'
import {getAllBooks} from "./Service";
import EditBook from "./Edit";

Modal.setAppElement('#root');

export default class App extends React.Component {

    state = {
        books: [],
        isLoading: false,
        isShowEdit: false,
        bookToEdit: {}
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

    handleEditView = (bookToEdit) => {
        this.setState({bookToEdit: bookToEdit, isShowEdit: true});
    };

    closeEditView = () => {
        this.setState({isShowEdit: false});
    };

    handleUpdateBook = (updatedBook) => {
        const filteredBooks = this.state.books.filter(book => book.id !== updatedBook.id); // нет id в приходящей буке?
        const updBooks = [updatedBook, ...filteredBooks];
        this.setState({books: updBooks})
    };

    render() {
        const { books, isLoading, isShowEdit, bookToEdit } = this.state;

        return (
            <React.Fragment>
                <div className="component-box">
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
                            onEditView={this.handleEditView}
                        />
                        : null
                    }
                </div>
                <AddBook
                    onAdd={this.handleAddBook}
                />
                <Modal
                    isOpen={isShowEdit}
                    shouldCloseOnOverlayClick={true}
                    shouldCloseOnEsc={true}
                    onRequestClose={this.closeEditView}
                    className="Modal"
                    overlayClassName="Overlay"
                    contentLabel="ModalLabel"
                >
                    <EditBook
                        bookToEdit={bookToEdit}
                        onUpdate={this.handleUpdateBook}
                    />
                    <button
                        className='btn btn-default'
                        onClick={this.closeEditView}
                    >
                        Close
                    </button>
                </Modal>
            </React.Fragment>
        )
    }
}
