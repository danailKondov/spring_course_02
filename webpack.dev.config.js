const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');

module.exports = {
    entry: './src/ui/index.js',
    devtool: 'inline-source-map',
    output: {
        path: path.resolve(__dirname),
        filename: 'bundle.js',
        libraryTarget: 'umd'
    },

    devServer: {
        contentBase: path.resolve(__dirname) + '/src/ui',
        compress: true,
        port: 9000,
        host: 'localhost',
        open: true,
        before: (app) => {
            app.get('/api/books/', (req, res) => res.send([
                {
                    id: '1',
                    title: 'Привяу',
                    authors: 'testAut',
                    genre: 'testGen',
                    comments: [
                        {
                            title: 'title1',
                            user: 'user1',
                            text: 'text1',
                            date: '15-02-2019'
                        },
                        {
                            title: 'title2',
                            user: 'user2',
                            text: 'text2',
                            date: '15-02-2019'
                        },
                        {
                            title: 'title3',
                            user: 'user13',
                            text: 'text3',
                            date: '15-02-2019'
                        },
                        {
                            title: 'title4',
                            user: 'user4',
                            text: 'text4',
                            date: '15-02-2019'
                        },

                            ]},
                {
                    id: '2',
                    title: 'Test',
                    authors: 'testAut',
                    genre: 'testGen',
                    comments: [{
                        title: 'title',
                        user: 'user',
                        text: 'text',
                        date: '16-02-2019'
                    }]}
            ]));
            app.post('/api/books/', (req, res) => res.send(
                {id: '3', title: 'testTitle3', authors: 'testAut3', genre: 'testGen3', comments: []}
            ));
            app.delete('/api/books/1', (req, res) => res.sendStatus(200));
            app.put('/update', (req, res) => res.sendStatus(200))
        }
    },

    module: {
        loaders: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env', 'react'],
                        plugins: [
                            require("babel-plugin-transform-class-properties")
                        ]
                    }
                }
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(png|jpg|woff|woff2|eot|ttf|svg)$/,
                loader: 'url-loader?limit=100000'
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'src/ui/index.html'
        })
    ]
};
