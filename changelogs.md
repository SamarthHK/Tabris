Altered all classes that initialize to have a singleton so the init cant be ran more then once
Added middleware parsing for json files, json files need to contain a schema tag with the class name attatched to it or else middleware will return a error
Changed Request object to hold a flag for errors
Changed Request object to hold JsonHandler object
Untested stuff aswell