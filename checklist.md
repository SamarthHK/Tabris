<h1>Current plans for HTTP server in order</h1>
<li>
<ol> COMPLETED Enable Multi-threading using blocking IO and spawning new threads</ol>
<ol> COMPLETED Logger</ol>
<ol> COMPLETED Switch to thread pools</ol>
<ol> SKIPPED Maybe switch to virtual threads or skip for now</ol>
<ol> COMPLETED SOMEWHAT Create environment variables and add support for loading/ accessing them <br/> Actually i just added support for propertie files Uhh need to add DI in future</ol>
<ol> COMPLETED Create JSON support</ol>
<ol> COMPLETED sidetrack, Create a initializer interface and make every dependency that needs to be initialized extend that</ol>
<ol> SKIPPED create a lambda in main that takes a arraylist of the initializers and executes them all in order</ol>
<ol> SKIPPED Create JSON configs and </ol>
<ol> add JSON support to HTTP server request parsing (Middleware) </ol>
<ol>Create JSON responses</ol>
<ol>Session handling etc.</ol>
<ol>Create a sub-package to identify ID within HTML to inject certain links/ data</ol>
<ol>Create a HTML page for viewing image with image id: ${image-link}</ol>
<ol>Create logic/ wiring to serve HTML page with image link injected into HTML</ol>
<ol>Add DI for PropLoader file so I can have multiple propertie files</ol>
<ol> Destroy the ImageReciever god class and split it up into a few classes</ol>
</li>