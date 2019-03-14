import React from 'react'
import PropTypes from 'prop-types'
import {Link, withRouter} from "react-router-dom";
import './library.css'
import {deleteById} from "./Service";

class BookTable extends React.Component {

    state = {
        books: [],
        isAuthenticated: false
    };

    static defaultProps = {
        books: []
    };

    static getDerivedStateFromProps(props, state) {
        let books = [...props.books];
        let isAuthenticated = props.isAuthenticated;
        return {
            books,
            isAuthenticated
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

    handleEdit = (id) => {
        this.props.history.push({
            pathname: '/edit',
            state: { id }
        })
    };

    render() {

        const {books, isAuthenticated} = this.state;

        return(
            <table className="table" id="bookstable">
                <thead>
                <tr>
                    <th>Book id</th><th>Title</th><th>Authors</th><th>Genre</th><th>Action</th>
                </tr>
                </thead>
                <tbody id="tablebody">
                    {
                        books.map(({id, title, authors, genre, comments}, i) => (
                            <tr key={"tableRow" + i}>
                                <td>{id}</td>
                                <td>{title}</td>
                                <td>{authors}</td>
                                <td>{genre}</td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.handleEdit(id)}
                                        disabled={!isAuthenticated}
                                    >
                                        Edit
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.handleDelete(id)}
                                        disabled={!isAuthenticated}>
                                        Delete
                                    </button>
                                </td>
                                <td>
                                    <button
                                        className='btn btn-default'
                                        onClick={() => this.props.onCommentView(comments)}>
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
    onCommentView: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool.isRequired
};

export default withRouter(BookTable);