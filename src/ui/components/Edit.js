import React from 'react'
import {Link, withRouter} from "react-router-dom";
import './library.css'
import {updateBook} from "./Service";

export default class EditBook extends React.Component {

    state = {
        isSuccessfulUpdate: null,
        id: " ",
        title: " ",
        authors: " ",
        genre: " ",
        comments: []
    };

    componentDidMount() {
        const {id, title, authors, genre, comments} = this.props.bookToEdit;
        this.setState({
            id,
            title,
            authors,
            genre,
            comments
        });
    }

    handleUpdate = (e) => {
        e.preventDefault();
        e.stopPropagation();
        const {id, authors, title, genre, comments } = this.state;
        const book = {id, title, authors, genre, comments};
        updateBook(book)
            .then(() => {
                this.setState({isSuccessfulUpdate: true});
                this.props.onUpdate(book);
            })
            .catch(() => this.setState({isSuccessfulUpdate: false}));
    };

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    validate = () => {
        const { authors, title, genre } = this.state;
        return !!(authors.trim() && title.trim() && genre.trim());
    };

    render() {

        const {id, title, authors, genre, isSuccessfulUpdate} = this.state;

        return(
            <div>
                <div id="edit">
                    <h2>Edit book </h2>
                    <form id="editform" className="form-horizontal">
                        <div className='form-group'>
                            <div className='col-sm-10'>
                                <label>Id
                                    <input id='id'
                                           className='form-control'
                                           type='text'
                                           value={id}
                                           readOnly={true}
                                    />
                                </label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-10">
                                <label>Enter new title
                                    <input id="title"
                                           className="form-control"
                                           type="text"
                                           value={title}
                                           onChange={this.handleChange}
                                           required/>
                                </label>
                            </div>
                        </div>
                        <div className='form-group'>
                            <div className='col-sm-10'>
                                <label>Enter new authors
                                    <input id='authors'
                                           className='form-control'
                                           type='text'
                                           onChange={this.handleChange}
                                           value={authors}
                                    />
                                </label>
                            </div>
                        </div>
                        <div className='form-group'>
                            <div className='col-sm-10'>
                                <label>Enter new genre
                                    <input id='genre'
                                           className='form-control'
                                           type='text'
                                           onChange={this.handleChange}
                                           value={genre}
                                    />
                                </label>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-10">
                                <button
                                    className='btn btn-default'
                                    disabled={!this.validate()}
                                    onClick={(e) => this.handleUpdate(e)}>
                                    Update
                                </button>
                            </div>
                        </div>
                    </form><br/>
                    {
                        isSuccessfulUpdate === true ?
                            <p className='goodMsg'>Book was updated successfully</p> :
                            isSuccessfulUpdate === false ?
                                <p className='badMsg'>Something's go wrong...</p> :
                                null
                    }
                </div>
            </div>
        )
    }
}