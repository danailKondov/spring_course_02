import React from 'react'
import {Link, withRouter} from "react-router-dom";
import './library.css'
import {updateTitleById} from "./Service";

export default class EditBook extends React.Component {

    state = {
        newTitle: "",
        isSuccessfulUpdate: null
    };

    handleUpdate = (bookId, e) => {
        e.preventDefault();
        e.stopPropagation();
        updateTitleById(bookId, this.state.newTitle)
            .then(() => this.setState({isSuccessfulUpdate: true}))
            .catch(() => this.setState({isSuccessfulUpdate: false}));
    };

    handleChange = (e) => {
        this.setState({ newTitle: e.currentTarget.value })
    };

    validate = () => !!this.state.newTitle.trim();

    render() {

        const {newTitle, isSuccessfulUpdate} = this.state;
        const bookId = this.props.location.state.id;

        return(
            <div>
                <div id="edit">
                    <h2>Edit book title</h2>
                    <form id="editform" className="form-horizontal">
                            <div className="form-group">
                                <div className="col-sm-10">
                                    <label>Enter new title
                                        <input id="title"
                                               className="form-control"
                                               type="text"
                                               value={newTitle}
                                               onChange={this.handleChange}
                                               required/>
                                    </label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-10">
                                    <button
                                        className='btn btn-default'
                                        disabled={!this.validate()}
                                        onClick={(e) => this.handleUpdate(bookId, e)}>
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

                <div id="back">
                    <Link className="btn btn-default" to="/"> Back </Link>
                </div>
            </div>
        )
    }
}