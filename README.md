# comics-api
<p>
  This project works as a kind of <a href="https://martinfowler.com/bliki/ReportingDatabase.html">Reporting Database</a> 
  for information contained in the <a href="https://developer.marvel.com/docs">Marvel API</a>.
</p>
<p>Provides info about comics collaborators and characters companions via a REST API.<p>

## Building and running the app
### Requirements
<ul>
  <li>Java 17</li>
  <li>PostgresSQL database</li>
</ul>

### Configuration
<ol>
    <li>
        <p>Make sure you have Java 17 installed on your system.</p>
        <p>
            <pre><code>
echo $JAVA_HOME
            </code></pre>
        </p>
        <p>
            if not, refer to this <a href="https://techviewleo.com/install-java-openjdk-on-ubuntu-linux/">tutorial</a> for help.
        </p>
    </li>
    <li>
        Make sure that you have a postgreSQL database ready, edit relevant properties in <code>application.properties</code> file if necessary
    </li>
    <li>
        Run
        <pre><code>
bash assemble.sh
        </code></pre>
        to build the application
    </li>
    <li>
        Run the app with
        <pre><code>
bash avengers.sh DB_USER_NAME DB_USER_PASS MARVEL_PUBLIC_KEY MARVEL_PRIVATE_KEY
        </code></pre>
        You can optionally edit the properties in <code>avengers.sh</code> instead of passing them as parameters
    </li>
</ol>
