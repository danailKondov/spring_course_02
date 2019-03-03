import React from 'react'
import ReactDOM from 'react-dom'
import {Route, Switch} from 'react-router-dom'
import { Router } from 'react-router-dom'
import { BrowserRouter } from 'react-router-dom';
import App from './components/App'
import Comments from "./components/Comments";

ReactDOM.render(
    <BrowserRouter>
        <Switch>
            <Route exact path="/" component={App} />
            <Route path="/comm" component={Comments} />
            <Route component={() => <h2>Ресурс не найден</h2>} />
        </Switch>
    </BrowserRouter>,
  document.getElementById('root')
);