import React from 'react'
import {Link, withRouter} from "react-router-dom";
import './library.css'
import {getComments} from "./Service";

export default class Comments extends React.Component {

    state = {
        comments: [],
        isLoading: false,
    };

    componentDidMount() {
        this.fetchComments(this.props.location.state.id);
    }

    fetchComments = (id) => {
        this.setState({ isLoading: true });
        getComments(id)
            .then(response => response.json())
            .then(comments => this.setState({isLoading: false, comments}));
    };

    render() {

        const { comments, isLoading } = this.state;

        return(
            <div className="component-box">
                <h1>Comments</h1>
                {
                    isLoading ?
                        <p>Загружаю...</p>
                        : null
                }
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
                <div id="back">
                    <Link className="btn btn-default" to="/"> Back </Link>
                </div>
            </div>
        )
    }


}

