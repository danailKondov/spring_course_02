import React from 'react'
import PropTypes from 'prop-types'
import './library.css'
import {postBook} from "./Service";

class AddBook extends React.Component {

    state = {
        addResult: null,
        authors: ' ',
        title: ' ',
        genre: ' '
    };

    handleSubmit = (e) => {
        e.preventDefault();
        e.stopPropagation();
        const { authors, title, genre } = this.state;
        postBook(authors, title, genre)
            .then(resp => {
                this.setState({
                    addResult: true,
                    authors: ' ',
                    title: ' ',
                    genre: ' '
                });
                return resp.json();
            })
            .then(book => {
                this.props.onAdd(book);
            })
            .catch(
                this.setState({addResult: false})
            );
    };

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    validate = () => {
        const { authors, title, genre } = this.state;
        if (authors.trim() && title.trim() && genre.trim()) {
            return true
        }
        return false
    };

    render() {

        const { authors, title, genre } = this.state;

        return(
            <div id='add'>
                <h2>Add new Book</h2>
                <form id='addform' className='form-horizontal'>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Authors
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
                            <label>Title
                                <input id='title'
                                       className='form-control'
                                       type='text'
                                       onChange={this.handleChange}
                                       value={title}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Genre
                                <input id='genre'
                                       className='form-control'
                                       type='text'
                                       onChange={this.handleChange}
                                       value={genre}
                                 />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <button
                                className='btn btn-default'
                                onClick={this.handleSubmit}
                                disabled={!this.validate()}>
                                Add Book
                            </button>
                        </div>
                    </div>
                    {
                        this.state.addResult === true ?
                            <p className='goodMsg'>Book was added successfully</p> :
                            this.state.addResult === false ?
                                <p className='badMsg'>Something's go wrong...</p> :
                                null
                    }
                </form><br/>
            </div>
        )
    }
}

AddBook.propTypes = {
    onAdd: PropTypes.func.isRequired
};

export {AddBook}