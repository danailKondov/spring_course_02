import React from 'react'
import ReactDOM from 'react-dom'
import {Route, Switch} from 'react-router-dom'
import { Router } from 'react-router-dom'
import { BrowserRouter } from 'react-router-dom';
import EditBook from './components/Edit'
import App from './components/App'

ReactDOM.render(
    <BrowserRouter>
        <Switch>
            <Route exact path="/" component={App} />
            <Route path="/edit" component={EditBook} />
            <Route component={() => <h2>Ресурс не найден</h2>} />
        </Switch>
    </BrowserRouter>,
  document.getElementById('root')
);