var http = require('http');
var url = require('url');
var x,y;
var setted=false;

http.createServer(function(request,response){
    var baseUrl = url.parse(request.url).pathname;
    if(baseUrl=='/upload'){
        var q = url.parse(request.url, true).query;
        x = q.x;
        y = q.y;
        setted=true;
        response.end("UPDATE SUCCESS: "+x+","+y)
    }else{
        if(setted){
            response.end(x+","+y)
        }else{
            response.end("Location unknown");
        }
    }
}).listen(80);