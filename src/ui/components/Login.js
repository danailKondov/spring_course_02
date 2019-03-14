import React from 'react'
import PropTypes from 'prop-types'
import {processLogin} from "./Service";

export default class Login extends React.Component {

    state = {
        username: "",
        password: "",
        isAuthenticated: null
    };

    handleChange = (e) => {
        const { id, value } = e.currentTarget;
        this.setState({ [id]: value })
    };

    handleSubmit = (e) => {
        e.preventDefault();
        e.stopPropagation();
        const { username, password } = this.state;
        processLogin(username, password)
            .then(resp => {
                if (resp.status === 200) {
                    this.setState({isAuthenticated: true});
                    this.props.onLogin(username);
                }
            })
            .catch(
                this.setState({isAuthenticated: false})
            );
    };

    render() {

        const {username, password} = this.state;

        return (
            <div className="loginContainer">
                <h4>Login page</h4>
                <form className='form-horizontal'>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>User name
                                <input id='username'
                                       className='form-control'
                                       type='text'
                                       onChange={this.handleChange}
                                       value={username}
                                       required={true}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <label>Title
                                <input id='password'
                                       className='form-control'
                                       type='text'
                                       onChange={this.handleChange}
                                       value={password}
                                />
                            </label>
                        </div>
                    </div>
                    <div className='form-group'>
                        <div className='col-sm-10'>
                            <button
                                className='btn btn-default'
                                onClick={this.handleSubmit}>
                                Enter
                            </button>
                        </div>
                    </div>
                </form>
                {
                    this.state.isAuthenticated === true ?
                        <p className='goodMsg'>Login successfully</p> :
                        this.state.isAuthenticated === false ?
                            <p className='badMsg'>Something's go wrong...</p> :
                            null
                }
            </div>
        )
    }
}

Login.propTypes = {
    onLogin: PropTypes.func.isRequired
};