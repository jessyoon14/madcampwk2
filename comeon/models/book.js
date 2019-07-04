var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var bookSchema = new Schema({
    name: String,
    phnumber: String,
    published_date: String
});

module.exports = mongoose.model('book', bookSchema);
