var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var personalSchema = new Schema({
    image: String,
    name : String,
    phnumber : String,
    date : String
})


var scheduleSchema = new Schema({

    date: String,
    schedule: String
})


var bookSchema = new Schema({
    image: String,
    email: String,
    contact_list: [personalSchema],
    image_list: [],
    schedule_list: [scheduleSchema]
});



module.exports.one = mongoose.model('book', bookSchema);
module.exports.two = mongoose.model('personalc', personalSchema);
module.exports.three = mongoose.model('schedule', scheduleSchema);

