import React from 'react'
import PropTypes from 'prop-types'
import {Link, withRouter} from "react-router-dom";
import './library.css'
import {deleteById} from "./Service";

class BookTable extends React.Component {

    state = {
        books: [],
    };

    static defaultProps = {
        books: []
    };

    static getDerivedStateFromProps(props, state) {
        let books = [...props.books];
        return {
            books: books,
        }
    }

    componentDidMount() {
        this.setState({books: this.props.books})
    }

    handleDelete = (id) => {
        deleteById(id)
            .then(resp => {
                if (resp.ok) {
                    this.props.onDelete(id);
                }
            })
            .catch(error => console.error('Error:', error));
    };

    handleComments = (id) => {
        this.props.history.push({
            pathname: '/comm',
            state: { id }
        })
    };

    render() {

        let books = this.state.books;

        return(
            <table className="table" id="bookstable">
                <thead>
                <tr>
                    <th>Book id</th><th>Title</th><th>Authors</th><th>Genre</th><th>Action</th>
                </tr>
                </thead>
                <tbody id="tablebody">
                    {
                        books.map((book, i) => (
                            <tr key={"tableRow" + i}>
                                <td>{book.id}</td>
                                <td>{book.title}</td>
                                <td>{book.authors}</td>
                                <td>{book.genre}</td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.props.onEditView(book)}>
                                        Edit
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.handleDelete(book.id)}>
                                        Delete
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.handleComments(book.id)}>
                                        View comments
                                    </button>
                                </td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        )
    }
}

BookTable.propTypes = {
    books: PropTypes.array.isRequired,
    onDelete: PropTypes.func.isRequired,
    onEditView: PropTypes.func.isRequired
};

export default withRouter(BookTable);