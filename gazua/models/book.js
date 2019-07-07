var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var personalSchema = new Schema({
    name : String,
    phnumber : String,
    date : String
})

var bookSchema = new Schema({
    email: String,
    contact_list: [personalSchema],
    image_list: []
});



module.exports.one = mongoose.model('book', bookSchema);
module.exports.two = mongoose.model('personalc', personalSchema);
