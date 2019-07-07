// routes/index.js

module.exports = function(app, Book, Personalc, Schedule)
{
    // GET ALL BOOKS
    app.get('/api/books', function(req,res){
        Book.find(function(err, books){
            if(err) return res.status(500).send({error: 'database failure'});
            res.json(books);
        })
    });

    // GET PERSONAL BOOKS
    app.get('/api/books/:email', function(req,res){
        Book.findOne({email: req.params.email}, function(err, book){
            if(err) return res.status(500).send({error: 'database failure'});
            if(!book) return res.status(404).json({error: 'book not found'});
            res.json(book);
        })
    });
    //// GET PERSONAL SCHEDULES
    //app.get('/api/books/schedule/:email', function(req,res){
    //    Book.findOne({email: req.params.email}, function(err, book){
    //        if(err) return res.status(500).send({error: 'database failure'});
    //        if(!book) return res.status(404).json({error: 'book not found'});
    //        res.json(book);
    //    })
    //});

    // GET SINGLE contact
    //app.get('/api/books/:email', function(req, res){
    //   Book.findOne({_id: req.params.book_id}, function(err, book){
    //        if(err) return res.status(500).json({error: err});
    //        if(!book) return res.status(404).json({error: 'book not found'});
    //        res.json(book);
    //    })
    //});

    // GET BOOK BY AUTHOR
    //app.get('/api/books/author/:author', function(req, res){
    //    Book.find({author: req.params.author}, {_id: 0, title: 1, published_date: 1},  function(err, books){
    //        if(err) return res.status(500).json({error: err});
    //        if(books.length === 0) return res.status(404).json({error: 'book not found'});
    //        res.json(books);
    //    })
    //});

    // CREATE ACCOUNT
    app.post('/api/books', function(req, res){
        Book.find;
        var book = new Book();
        book.email = req.body.email;
        book.contact_list = [];
        book.image_list = [];
        book.schedule_list = [];
        
        console.log("book title : "+book.email );

        book.save(function(err){
            if(err){
                console.error(err);
                res.json({result: 0});
                return;
            }    
            res.json({result: 1});    
        });
    });
    
    //CREATE PERSONAL CONTACT
    app.post('/api/books/:email', function(req, res){
        Book.findOne({email: req.params.email}, function(err, book){
            if(err) return res.status(500).json({error: err});
            if(!book) return res.status(404).json({error: 'book not found'});
            var personalc = new Personalc();
            personalc.name = req.body.name;
            personalc.phnumber = req.body.phnumber;
            personalc.date = req.body.date;
            book.contact_list.push(personalc);
            //res.json(book);
        

            book.save(function(err){
                if(err){
                    console.error(err);
                    res.json({result: 0});
                    return;
                }    
                res.json({result: 1});    
            });
        });
    });

    //post new schedule one certain day
    app.post('/api/books/schedule/:email', function(req, res){
        Book.findOne({email: req.params.email}, function(err, book){
            if(err) return res.status(500).json({error: err});
            if(!book) return res.status(404).json({error: 'book not found'});
            var schedulec = new Schedule();
            schedulec.date = req.body.date;
            schedulec.schedule = req.body.schedule;
            
            book.schedule_list.push(schedulec);
            //res.json(book);
        

            book.save(function(err){
                if(err){
                    console.error(err);
                    res.json({result: 0});
                    return;
                }    
                res.json({result: 1});    
            });
        });
    });

    //ADD NEW IMAGE NAME
    app.post('/api/books/image/:email', function(req, res){
        Book.findOne({email: req.params.email}, function(err, book){
            if(err) return res.status(500).json({error: err});
            if(!book) return res.status(404).json({error: 'book not found'});

            book.image_list.push(req.body.image);

            //res.json(book);
        

            book.save(function(err){
                if(err){
                    console.error(err);
                    res.json({result: 0});
                    return;
                }    
                res.json({result: 1});    
            });
        });
    });


    // UPDATE THE BOOK
    app.put('/api/books/:email', function(req, res){
        Book.findById(req.params.book_id, function(err, book){
            if(err) return res.status(500).json({ error: 'database failure' });
            if(!book) return res.status(404).json({ error: 'book not found' });
    
            if(req.body.contact_list) book.contact_list = req.body.contact_list;

            book.save(function(err){
                if(err) res.status(500).json({error: 'failed to update'});
                res.json({message: 'book updated'});
            });
    
        });
    
    });

    // DELETE BOOK
    app.delete('/api/books/:email', function(req, res){
        Book.findOne({email: req.params.email}, function(err, book){
            if(err) return res.status(500).json({error: err});
            if(!book) return res.status(404).json({error: 'book not found'});
            
            for(var i in book.contact_list){
                if(book.contact_list._id == req.body.id){
                    break;
                }
            }
            book.contact_list.splice(i,1)
            //res.status(404).json({_id: req.body.id})
            //book.contact_list.remove({_id: req.body.id}, function(err, output){
            //    if(err) return res.status(500).json({error: "database failure"});
            //    
            //})
            book.save(function(err){
                if(err) res.status(500).json({error: 'failed to update'});
                res.json({message: 'book updated'});
            });
            
        })
    });
}