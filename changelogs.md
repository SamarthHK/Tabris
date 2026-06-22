Altered JsonHandler to support writing json strings <br/>
Now when writing json string or altering the class that is given from JsonHandler you can feed it back in via setInstance <br/>
Request objects now support JSON, you can feed the JSON in when constructing the object. <br/>
Added a error tag to request objects, now when a error is hit you can just check error flag true and when the object is serialized in getResponse the SERVER_ERROR is returned<br/>
Also will be remembering to add line breaks in my changelogs so everything inst mushed into one

